package sk.kasper.ui_common.analytics

class EmptyAnalyticsLogger: AnalyticsLogger {

    override fun log(event: String, attributes: Map<String, String>) {
    }

}