package sk.kasper.base

interface Flags {

    val isDebug: Boolean
    val apiKey: String
    val syncIntervalHours: Long
    val bootstrapResponseApi: Boolean
    val bootstrapResponseApiFileName: String

}