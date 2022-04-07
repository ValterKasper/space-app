package sk.kasper.ui_common.analytics

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import sk.kasper.base.init.AppInitializer
import sk.kasper.ui_common.BuildConfig
import javax.inject.Inject

internal class AnalyticsInitializer @Inject constructor(
    @ApplicationContext private val context: Context
) : AppInitializer {

    override fun init() {
        if (!BuildConfig.DEBUG) {
            Analytics.plant(FirebaseAnalyticsLogger(context))
        }
    }

}