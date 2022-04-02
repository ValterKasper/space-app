package sk.kasper.space.notification.showLaunchNotificationJob

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.threeten.bp.Duration
import org.threeten.bp.LocalDateTime
import sk.kasper.domain.model.SyncLaunches
import sk.kasper.domain.repository.LaunchRepository
import sk.kasper.entity.Launch
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Rozhodne, pre ktore starty sa naplanuju notifikacie a na aky cas
 */
// TODO I: overenginered
// TODO I: ako su pomenovane dlhozijuce komponent v inyvh android aplikaciach
@Singleton
open class LaunchNotificationChecker @Inject constructor(
    private val repository: LaunchRepository,
    private val syncLaunches: SyncLaunches,
    private val showLaunchNotificationWorkerScheduler: ShowLaunchNotificationWorkerScheduler
) : DefaultLifecycleObserver, SyncLaunches.SyncListener {

    companion object {
        private val MIN_DURATION_AFTER_NOW_FOR_SCHEDULING: Duration = Duration.ofHours(2)
        private val MAX_DURATION_AFTER_NOW_FOR_SCHEDULING: Duration = Duration.ofDays(7)

        private val MIN_DURATION_BEFORE_LAUNCH_TO_SHOW_NOTIFICATION: Duration = Duration.ofMinutes(60)
    }

    override fun onSync() {
        // TODO D: inject scope
        GlobalScope.launch {
            checkLaunches(repository.getLaunches())
        }
    }

    override fun onStart(owner: LifecycleOwner) {
        syncLaunches.addSyncListener(this)
    }

    override fun onStop(owner: LifecycleOwner) {
        syncLaunches.removeSyncListener(this)
    }

    private fun checkLaunches(launches: List<Launch>) {
        launches
                .filter { it.accurateTime && it.accurateDate }
                .filter { getCurrentDateTime().plus(MIN_DURATION_AFTER_NOW_FOR_SCHEDULING).isBefore(it.launchDateTime) }
                .filter { getCurrentDateTime().plus(MAX_DURATION_AFTER_NOW_FOR_SCHEDULING).isAfter(it.launchDateTime) }
                .forEach {
                    showLaunchNotificationWorkerScheduler.scheduleLaunchNotification(
                            it.id,
                            it.launchDateTime.minus(MIN_DURATION_BEFORE_LAUNCH_TO_SHOW_NOTIFICATION))
                }
    }

    open fun getCurrentDateTime(): LocalDateTime {
        return LocalDateTime.now()
    }

}