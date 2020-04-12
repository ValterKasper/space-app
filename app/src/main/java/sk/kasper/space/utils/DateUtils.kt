package sk.kasper.space.utils

import org.threeten.bp.Instant
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneId
import timber.log.Timber
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

fun String.toTimeStamp(): Long = try {
    // e.g June 4, 2010 18:45:00 UTC
    val format = SimpleDateFormat("MMM dd, yyyy HH:mm:ss z", Locale.US)
    format.parse(this)!!.time
} catch (e: ParseException) {
    Timber.e(e, "String.toTimeStamp")
    0
}

fun Long.toLocalDateTime(): LocalDateTime =
        LocalDateTime.ofInstant(Instant.ofEpochMilli(this), ZoneId.systemDefault())

fun LocalDateTime.toTimeStamp(): Long =
        this.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()