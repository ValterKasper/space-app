package sk.kasper.base.init

import android.util.Log
import com.google.firebase.crashlytics.FirebaseCrashlytics
import sk.kasper.base.BuildConfig
import sk.kasper.base.logger.Logger
import timber.log.Timber
import javax.inject.Inject

internal class LoggingInitializer @Inject constructor() : AppInitializer {

    override fun init() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
            // TODO D: refactor
            Logger.e = { msg, e ->
                if (msg.isEmpty()) {
                    Timber.e(e)
                } else {
                    if (e == null) {
                        Timber.e(msg)
                    } else {
                        Timber.e(e, msg)
                    }
                }
            }
            Logger.d = { msg ->
                Timber.d(msg)
            }
        } else {
            Timber.plant(CrashReportingTree())
        }
    }

    class CrashReportingTree : Timber.Tree() {

        override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
            if (priority == Log.WARN || priority == Log.VERBOSE || priority == Log.DEBUG || priority == Log.INFO) {
                return
            }

            val crashlytics = FirebaseCrashlytics.getInstance()

            crashlytics.setCustomKey("priority", priority)
            tag?.let {
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


}