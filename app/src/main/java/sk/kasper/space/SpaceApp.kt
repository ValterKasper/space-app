package sk.kasper.space

import android.app.Activity
import android.app.Application
import android.app.Service
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import com.crashlytics.android.Crashlytics
import com.google.firebase.analytics.FirebaseAnalytics
import com.jakewharton.threetenabp.AndroidThreeTen
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import dagger.android.HasServiceInjector
import io.fabric.sdk.android.Fabric
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
import sk.kasper.space.sync.SyncJobService
import timber.log.Timber
import javax.inject.Inject

// todo precisti kod
// todo rozchod CI na githube
open class SpaceApp: Application(), HasActivityInjector, HasServiceInjector {

    @Inject
    lateinit var checker: LaunchNotificationChecker

    @Inject
    lateinit var dispatchingActivityInjector: DispatchingAndroidInjector<Activity>

    @Inject
    lateinit var dispatchingServiceInjector: DispatchingAndroidInjector<Service>

    @Inject
    lateinit var remoteApi: RemoteApi

    @Inject
    lateinit var database: Database

    @Inject
    lateinit var settingsManager: SettingsManager

    override fun activityInjector(): AndroidInjector<Activity> {
        return dispatchingActivityInjector
    }

    override fun serviceInjector(): AndroidInjector<Service> {
        return dispatchingServiceInjector
    }

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
            FirebaseAnalytics.getInstance(this).setAnalyticsCollectionEnabled(false)
        } else {
            Fabric.with(this, Crashlytics())
            Timber.plant(CrashReportingTree())
            Analytics.plant(FirebaseAnalyticsLogger(this))
        }

        createAppComponent().inject(this)

        // todo cez app lifecycle
        checker.registerForLaunchChanges()

        SyncJobService.startPeriodicLaunchesSyncJob(this)

        AppCompatDelegate.setDefaultNightMode(settingsManager.nightMode)

        settingsManager.addSettingChangeListener { settingKey ->
            if (settingKey == SettingKey.NIGHT_MODE || settingKey == SettingKey.NIGHT_MODE_PRE_Q) {
                AppCompatDelegate.setDefaultNightMode(settingsManager.nightMode)
            }
        }

        Timber.d("api key " + BuildConfig.API_KEY)
    }
}

class CrashReportingTree : Timber.Tree() {

    override fun log(priority: Int, tag: String?, message: String?, t: Throwable?) {
        if (priority == Log.WARN || priority == Log.VERBOSE || priority == Log.DEBUG || priority == Log.INFO) {
            return
        }

        Crashlytics.setInt("priority", priority)
        Crashlytics.setString("tag", tag)
        Crashlytics.setString("message", message)

        if (t == null) {
            Crashlytics.logException(Exception(message))
        } else {
            Crashlytics.logException(t)
        }
    }

}
