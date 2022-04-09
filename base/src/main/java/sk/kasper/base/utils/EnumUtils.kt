package sk.kasper.base.utils

// TODO I: there should room support for enums
inline fun <reified T : Enum<T>> safeEnumValue(value: String, default: T): T = try {
    enumValueOf(value)
} catch (e: Exception) {
    default
}

inline fun <reified T : Enum<T>> enumValueOrNull(value: String): T? = try {
    enumValueOf<T>(value)
} catch (e: Exception) {
    null
}
