package sk.kasper.work.notification

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import sk.kasper.base.init.AppInitializer
import sk.kasper.domain.usecase.ObserveLaunches
import sk.kasper.domain.usecase.ScheduleLaunchNotifications
import javax.inject.Inject

internal class LaunchNotificationsInitializer @Inject constructor(
    private val scheduleLaunchNotifications: ScheduleLaunchNotifications,
    private val observeLaunches: ObserveLaunches,
    private val appCoroutineScope: CoroutineScope
) : AppInitializer {

    override fun init() {
        appCoroutineScope.launch {
            observeLaunches().collect {
                scheduleLaunchNotifications(it)
            }
        }
    }

}