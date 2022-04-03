package sk.kasper.base

internal object FlagsImpl : Flags {

    override val isDebug: Boolean
        get() = BuildConfig.DEBUG

    override val apiKey: String
        get() = BuildConfig.API_KEY

    override val synIntervalHours: Long
        get() = BuildConfig.SYNC_INTERVAL_HOURS
}