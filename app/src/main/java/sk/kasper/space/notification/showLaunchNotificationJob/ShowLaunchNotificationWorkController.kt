package sk.kasper.space.notification.showLaunchNotificationJob

import org.threeten.bp.Duration
import org.threeten.bp.LocalDateTime
import sk.kasper.domain.usecase.launchdetail.GetLaunch
import sk.kasper.space.notification.LaunchNotificationInfo
import sk.kasper.space.notification.NotificationsHelper
import sk.kasper.ui_common.settings.SettingsManager
import timber.log.Timber

class ShowLaunchNotificationWorkController(
    private val getLaunch: GetLaunch,
    private val notificationsHelper: NotificationsHelper,
    private val currentDateTime: LocalDateTime,
    private val settingsManager: SettingsManager
) {

    suspend fun doWork(launchId: String) {
        if (!settingsManager.showLaunchNotifications) {
            Timber.d("onStartJob - success - notifications turned off in settings")
            return
        }

        try {
            val launch = getLaunch(launchId)
            Timber.d("onStartJob - success - notification shown")
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
            Timber.e(e, "onStartJob - failure")
        }

        return
    }

}