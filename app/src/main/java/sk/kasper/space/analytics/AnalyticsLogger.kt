package sk.kasper.space.analytics

interface AnalyticsLogger {

    fun log(event: String, attributes: Map<String, String>)

}