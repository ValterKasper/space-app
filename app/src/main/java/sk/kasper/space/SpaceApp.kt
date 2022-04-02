package sk.kasper.space

import android.app.Application
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.work.Configuration
import androidx.work.WorkManager
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.jakewharton.threetenabp.AndroidThreeTen
import dagger.hilt.android.HiltAndroidApp
import sk.kasper.space.api.RemoteApi
import sk.kasper.space.database.Database
import sk.kasper.space.notification.showLaunchNotificationJob.LaunchNotificationChecker
import sk.kasper.space.sync.SyncWorker
import sk.kasper.ui_common.analytics.Analytics
import sk.kasper.ui_common.analytics.FirebaseAnalyticsLogger
import sk.kasper.ui_common.settings.SettingKey
import sk.kasper.ui_common.settings.SettingsManager
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
open class SpaceApp : Application() {

    @Inject
    lateinit var checker: LaunchNotificationChecker

    @Inject
    lateinit var remoteApi: RemoteApi

    @Inject
    lateinit var database: Database

    @Inject
    lateinit var carDatabase: sk.kasper.database.SpaceRoomDatabase

    @Inject
    lateinit var settingsManager: SettingsManager

    @Inject
    lateinit var workManagerConfiguration: Configuration

    override fun onCreate() {
        AndroidThreeTen.init(this)

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
            FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(false)
        } else {
            Timber.plant(CrashReportingTree())
            Analytics.plant(FirebaseAnalyticsLogger(this))
        }

        super.onCreate()

        ProcessLifecycleOwner.get().lifecycle.addObserver(checker)

        WorkManager.initialize(this, workManagerConfiguration)

        SyncWorker.startPeriodicWork(this)

        AppCompatDelegate.setDefaultNightMode(settingsManager.nightMode)

        settingsManager.addSettingChangeListener { settingKey ->
            if (settingKey == SettingKey.NIGHT_MODE) {
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
