package sk.kasper.base.utils

import org.threeten.bp.Instant
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneId
import sk.kasper.base.logger.Logger
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

fun String.toTimeStamp(): Long = try {
    val format = SimpleDateFormat("MMM dd, yyyy HH:mm:ss z", Locale.US)
    format.parse(this)!!.time
} catch (e: ParseException) {
    Logger.e(e)
    0
}

fun Long.toLocalDateTime(): LocalDateTime =
    LocalDateTime.ofInstant(Instant.ofEpochMilli(this), ZoneId.systemDefault())

fun LocalDateTime.toTimeStamp(): Long =
        this.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()