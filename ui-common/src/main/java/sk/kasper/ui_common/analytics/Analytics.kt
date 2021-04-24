package sk.kasper.ui_common.analytics

import com.google.firebase.analytics.FirebaseAnalytics
import timber.log.Timber

class Analytics {

    companion object {

        private var analyticsLogger: AnalyticsLogger = EmptyAnalyticsLogger()

        fun log(event: String, attributes: Map<String, String>) {
            attributes.forEach {
                Timber.d("$event: (${it.key}: ${it.value})")
            }
            analyticsLogger.log(event, attributes)
        }

        fun plant(logger: AnalyticsLogger) {
            analyticsLogger = logger
        }

    }

    class Event {

        companion object {
            const val SELECT_CONTENT = "select_content"
            const val LAUNCH_NOTIF_TAP = "launch_notification_tap"
            const val SETTING_SHOW_BEFORE = "setting_show_before"
            const val WATCH_LIVE = "watch_live_tap"
            const val SELECT_LAUNCH_GALLERY_PHOTO = "select_launch_gallery_photo"
        }

    }

    class Param {

        companion object {
            const val ITEM_ID = FirebaseAnalytics.Param.ITEM_ID
            const val CONTENT_TYPE = FirebaseAnalytics.Param.CONTENT_TYPE
            const val ITEM_NAME = FirebaseAnalytics.Param.ITEM_NAME
            const val VALUE = FirebaseAnalytics.Param.VALUE
            const val ROCKET_ID = "rocket_id"
        }

    }

}