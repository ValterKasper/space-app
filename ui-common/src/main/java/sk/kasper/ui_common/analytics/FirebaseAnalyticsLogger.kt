package sk.kasper.ui_common.analytics

import android.content.Context
import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import dagger.hilt.android.qualifiers.ApplicationContext

class FirebaseAnalyticsLogger(@ApplicationContext private val context: Context) : AnalyticsLogger {

    override fun log(event: String, attributes: Map<String, String>) {
        val bundle = Bundle()
        for ((key, value) in attributes) {
            bundle.putString(key, value)
        }

        FirebaseAnalytics.getInstance(context).logEvent(event, bundle)
    }

}