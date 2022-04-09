package sk.kasper.base.logger

object Logger {


    private var loggingStrategy: LoggingStrategy = object : LoggingStrategy {

        override fun tag(tag: String): Logger {
            return this@Logger
        }

    }

    fun setLoggingStrategy(strategy: LoggingStrategy) {
        loggingStrategy = strategy
    }

    fun e(msg: String) {
        loggingStrategy.e(msg)
    }

    fun e(e: Exception) {
        loggingStrategy.e(e)
    }

    fun d(msg: String) {
        loggingStrategy.d(msg)
    }

    fun tag(msg: String): Logger {
        return loggingStrategy.tag(msg)
    }

}

interface LoggingStrategy {

    fun e(msg: String) {}

    fun e(e: Exception) {}

    fun d(msg: String) {}

    fun tag(tag: String): Logger

}

