package sk.kasper.space.notification

import org.threeten.bp.Duration
import org.threeten.bp.LocalDateTime

data class LaunchNotificationInfo(
        val id: Long,
        val rocketId: Long?,
        val rocketName: String?,
        val videoUrl: String?,
        val missionName: String?,
        val launchDateTime: LocalDateTime,
        val timeoutAfter: Duration = Duration.ofMinutes(30))