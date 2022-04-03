package sk.kasper.base.notification

import org.threeten.bp.LocalDateTime

fun interface EnqueueLaunchNotification {
    operator fun invoke(launchId: String, dateTimeNotification: LocalDateTime)
}