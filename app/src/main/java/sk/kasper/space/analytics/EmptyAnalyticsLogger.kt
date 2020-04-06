package sk.kasper.space.analytics

class EmptyAnalyticsLogger: AnalyticsLogger {

    override fun log(event: String, attributes: Map<String, String>) {
    }

}