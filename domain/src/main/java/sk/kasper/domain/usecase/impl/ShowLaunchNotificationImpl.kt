package sk.kasper.domain.usecase.impl

import org.threeten.bp.Duration
import org.threeten.bp.LocalDateTime
import sk.kasper.base.SettingsManager
import sk.kasper.base.logger.Logger
import sk.kasper.base.notification.LaunchNotificationInfo
import sk.kasper.base.notification.NotificationsHelper
import sk.kasper.domain.usecase.GetCurrentLocalDateTime
import sk.kasper.domain.usecase.GetLaunch
import sk.kasper.domain.usecase.ShowLaunchNotification
import javax.inject.Inject

interface T

internal class ShowLaunchNotificationImpl @Inject constructor(
    private val getLaunch: GetLaunch,
    private val notificationsHelper: NotificationsHelper,
    private val settingsManager: SettingsManager,
    private val getCurrentLocalDateTime: GetCurrentLocalDateTime,
) : ShowLaunchNotification {

    override suspend fun invoke(launchId: String) {
        if (!settingsManager.showLaunchNotifications) {
            Logger.d("onStartJob - success - notifications turned off in settings")
            return
        }

        try {
            val currentDateTime: LocalDateTime = getCurrentLocalDateTime()
            val launch = getLaunch(launchId)
            Logger.d("onStartJob - success - notification shown")
            val duration = Duration.ofMinutes(settingsManager.durationBeforeNotificationIsShown.toLong())
            if (launch.launchDateTime.isAfter(currentDateTime) && launch.launchDateTime.isBefore(currentDateTime.plus(duration))) {
                val (rocketName, missionName) = launch.launchNameParts
                val launchNotificationInfo = LaunchNotificationInfo(
                    launchId,
                    launch.rocketId,
                    rocketName,
                    launch.videoUrl,
                    missionName,
                    launch.launchDateTime)
                notificationsHelper.showLaunchNotification(launchNotificationInfo)
            }
        } catch (e: Exception) {
            Logger.e(e)
        }

        return
    }

}