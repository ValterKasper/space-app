package sk.kasper.base

interface Flags {

    val isDebug: Boolean
    val apiKey: String
    val synIntervalHours: Long
    val bootstrapResponseApi: Boolean
    val bootstrapResponseApiFileName: String

}