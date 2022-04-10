package sk.kasper.domain.usecase.impl.fakes

import sk.kasper.base.notification.LaunchNotificationInfo
import sk.kasper.base.notification.NotificationsHelper

class FakeNotificationsHelper : NotificationsHelper {

    private val _shownNotifications: MutableList<LaunchNotificationInfo> = mutableListOf()
    val shownNotifications: List<LaunchNotificationInfo> = _shownNotifications

    override fun showLaunchNotification(launchNotificationInfo: LaunchNotificationInfo) {
        _shownNotifications.add(launchNotificationInfo)
    }

}