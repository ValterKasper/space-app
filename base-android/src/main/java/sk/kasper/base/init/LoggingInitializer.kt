package sk.kasper.base.init

import android.util.Log
import com.google.firebase.crashlytics.FirebaseCrashlytics
import sk.kasper.base.BuildConfig
import sk.kasper.base.logger.Logger
import sk.kasper.base.logger.LoggingStrategy
import timber.log.Timber
import javax.inject.Inject

internal class LoggingInitializer @Inject constructor() : AppInitializer {

    override fun init() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        } else {
            Timber.plant(CrashReportingTree())
        }

        Logger.setLoggingStrategy(object : LoggingStrategy {

            private var currentTag: String? = null

            override fun e(e: Exception) = withTag {
                Timber.e(e)
            }

            override fun e(msg: String) = withTag {
                Timber.e(msg)
            }

            override fun d(msg: String) = withTag {
                Timber.d(msg)
            }

            override fun tag(tag: String): Logger {
                currentTag = tag
                return Logger
            }

            private fun withTag(block: () -> Unit) {
                currentTag?.let {
                    Timber.tag(currentTag)
                }
                block()
                currentTag = null
            }
        })
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