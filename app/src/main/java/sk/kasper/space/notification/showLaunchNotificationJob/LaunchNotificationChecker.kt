package sk.kasper.space.notification.showLaunchNotificationJob

import android.annotation.SuppressLint
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.threeten.bp.Duration
import org.threeten.bp.LocalDateTime
import sk.kasper.domain.model.Launch
import sk.kasper.domain.model.SyncLaunches
import sk.kasper.domain.repository.LaunchRepository
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Rozhodne, pre ktore starty sa naplanuju notifikacie a na aky cas
 */
@Singleton
open class LaunchNotificationChecker @Inject constructor(
        private val repository: LaunchRepository,
        private val syncLaunches: SyncLaunches,
        private val showLaunchNotificationJobScheduler: ShowLaunchNotificationJobScheduler) {

    companion object {
        private val MIN_DURATION_AFTER_NOW_FOR_SCHEDULING: Duration = Duration.ofHours(4)
        private val MAX_DURATION_AFTER_NOW_FOR_SCHEDULING: Duration = Duration.ofDays(7)

        private val MIN_DURATION_BEFORE_LAUNCH_TO_SHOW_NOTIFICATION: Duration = Duration.ofMinutes(60)
        private val MAX_DURATION_BEFORE_LAUNCH_TO_SHOW_NOTIFICATION: Duration = Duration.ofMinutes(3)
    }

    private fun checkLaunches(launches: List<Launch>) {
        launches
                .filter { it.accurateTime && it.accurateDate }
                .filter { getCurrentDateTime().plus(MIN_DURATION_AFTER_NOW_FOR_SCHEDULING).isBefore(it.launchDateTime) }
                .filter { getCurrentDateTime().plus(MAX_DURATION_AFTER_NOW_FOR_SCHEDULING).isAfter(it.launchDateTime) }
                .forEach {
                    showLaunchNotificationJobScheduler.scheduleLaunchNotification(
                            it.id,
                            it.launchDateTime.minus(MIN_DURATION_BEFORE_LAUNCH_TO_SHOW_NOTIFICATION),
                            it.launchDateTime.minus(MAX_DURATION_BEFORE_LAUNCH_TO_SHOW_NOTIFICATION))
                }
    }

    open fun getCurrentDateTime(): LocalDateTime {
        return LocalDateTime.now()
    }

    @SuppressLint("CheckResult")
    fun registerForLaunchChanges() {
        syncLaunches.addSyncListener(object: SyncLaunches.SyncListener {
            override fun onSync() {
                GlobalScope.launch {
                    checkLaunches(repository.getLaunches())
                }
            }
        })
    }

}