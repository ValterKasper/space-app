package sk.kasper.base

internal object FlagsImpl : Flags {

    override val isDebug: Boolean
        get() = BuildConfig.DEBUG

    override val apiKey: String
        get() = BuildConfig.API_KEY

    override val synIntervalHours: Long
        get() = BuildConfig.SYNC_INTERVAL_HOURS

    override val bootstrapResponseApi: Boolean
        get() = BuildConfig.BOOTSTRAP_RESPONSE_API

    override val bootstrapResponseApiFileName: String
        get() = BuildConfig.BOOTSTRAP_RESPONSE_API_FILE_NAME

}