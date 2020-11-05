package sk.kasper.space

import android.app.Application
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.work.Configuration
import androidx.work.WorkManager
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.jakewharton.threetenabp.AndroidThreeTen
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import sk.kasper.space.analytics.Analytics
import sk.kasper.space.analytics.FirebaseAnalyticsLogger
import sk.kasper.space.api.RemoteApi
import sk.kasper.space.database.Database
import sk.kasper.space.di.AppComponent
import sk.kasper.space.di.AppModule
import sk.kasper.space.di.DaggerAppComponent
import sk.kasper.space.notification.showLaunchNotificationJob.LaunchNotificationChecker
import sk.kasper.space.settings.SettingKey
import sk.kasper.space.settings.SettingsManager
import sk.kasper.space.sync.SyncWorker
import timber.log.Timber
import javax.inject.Inject

open class SpaceApp: Application(), HasAndroidInjector {

    @Inject
    lateinit var checker: LaunchNotificationChecker

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>

    @Inject
    lateinit var remoteApi: RemoteApi

    @Inject
    lateinit var database: Database

    @Inject
    lateinit var settingsManager: SettingsManager

    @Inject
    lateinit var workManagerConfiguration: Configuration

    override fun androidInjector(): AndroidInjector<Any> = dispatchingAndroidInjector

    protected open fun createAppComponent(): AppComponent {
        return DaggerAppComponent.builder()
                .application(this)
                .appModule(AppModule(this))
                .build()
    }

    override fun onCreate() {
        super.onCreate()

        AndroidThreeTen.init(this)

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
            FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(false)
        } else {
            Timber.plant(CrashReportingTree())
            Analytics.plant(FirebaseAnalyticsLogger(this))
        }

        createAppComponent().inject(this)

        ProcessLifecycleOwner.get().lifecycle.addObserver(checker)

        WorkManager.initialize(this, workManagerConfiguration)

        SyncWorker.startPeriodicWork(this)

        AppCompatDelegate.setDefaultNightMode(settingsManager.nightMode)

        settingsManager.addSettingChangeListener { settingKey ->
            if (settingKey == SettingKey.NIGHT_MODE || settingKey == SettingKey.NIGHT_MODE_PRE_Q) {
                AppCompatDelegate.setDefaultNightMode(settingsManager.nightMode)
            }
        }
    }
}

class CrashReportingTree : Timber.Tree() {

    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        if (priority == Log.WARN || priority == Log.VERBOSE || priority == Log.DEBUG || priority == Log.INFO) {
            return
        }

        val crashlytics = FirebaseCrashlytics.getInstance()

        crashlytics.setCustomKey("priority", priority)
        tag?.let{
            crashlytics.setCustomKey("tag", tag)
        }
        crashlytics.setCustomKey("message", message)

        if (t == null) {
            crashlytics.recordException(Exception(message))
        } else {
            crashlytics.recordException(t)
        }
    }

}
