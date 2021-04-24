package sk.kasper.ui_common.analytics

interface AnalyticsLogger {

    fun log(event: String, attributes: Map<String, String>)

}