package sk.kasper.space.utils

inline fun <reified T : Enum<T>> safeEnumValueOf(value: String, default: T): T = try {
        enumValueOf(value)
    } catch (e: Exception) {
        default
    }
