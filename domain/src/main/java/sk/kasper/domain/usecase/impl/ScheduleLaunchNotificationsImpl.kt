package sk.kasper.domain.usecase.impl

import org.threeten.bp.Duration
import sk.kasper.base.notification.EnqueueLaunchNotification
import sk.kasper.domain.usecase.GetCurrentLocalDateTime
import sk.kasper.domain.usecase.ScheduleLaunchNotifications
import sk.kasper.entity.Launch
import javax.inject.Inject

internal class ScheduleLaunchNotificationsImpl @Inject constructor(
    private val enqueueLaunchNotification: EnqueueLaunchNotification,
    private val getCurrentLocalDateTime: GetCurrentLocalDateTime,
) : ScheduleLaunchNotifications {

    companion object {
        private val MIN_DURATION_AFTER_NOW_FOR_SCHEDULING: Duration = Duration.ofHours(2)
        private val MAX_DURATION_AFTER_NOW_FOR_SCHEDULING: Duration = Duration.ofDays(7)
        private val MIN_DURATION_BEFORE_LAUNCH_TO_SHOW_NOTIFICATION: Duration = Duration.ofMinutes(60)
    }

    override operator fun invoke(launches: List<Launch>) {
        launches
            .filter { it.accurateTime && it.accurateDate }
            .filter {
                getCurrentLocalDateTime().plus(MIN_DURATION_AFTER_NOW_FOR_SCHEDULING).isBefore(it.launchDateTime)
            }
            .filter { getCurrentLocalDateTime().plus(MAX_DURATION_AFTER_NOW_FOR_SCHEDULING).isAfter(it.launchDateTime) }
            .forEach {
                enqueueLaunchNotification(
                    it.id,
                    it.launchDateTime.minus(MIN_DURATION_BEFORE_LAUNCH_TO_SHOW_NOTIFICATION)
                )
            }
    }

}