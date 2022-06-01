package sk.kasper.base.init

import com.google.firebase.crashlytics.FirebaseCrashlytics
import sk.kasper.base.BuildConfig
import javax.inject.Inject

internal class CrashlyticsInitializer @Inject constructor() : AppInitializer {

    override fun init() {
        if (BuildConfig.DEBUG) {
            // FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(false)
        }
    }

}