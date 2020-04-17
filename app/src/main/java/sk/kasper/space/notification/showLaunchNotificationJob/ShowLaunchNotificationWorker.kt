package sk.kasper.space.notification.showLaunchNotificationJob

import android.content.Context
import androidx.work.*
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import org.threeten.bp.LocalDateTime
import sk.kasper.domain.usecase.launchdetail.GetLaunch
import sk.kasper.space.notification.NotificationsHelper
import sk.kasper.space.settings.SettingsManager
import sk.kasper.space.utils.toTimeStamp
import sk.kasper.space.work.ChildWorkerFactory
import timber.log.Timber
import java.util.concurrent.TimeUnit

class ShowLaunchNotificationWorker @AssistedInject constructor(
        @Assisted appContext: Context,
        @Assisted workerParams: WorkerParameters,
        private val notificationsHelper: NotificationsHelper,
        private val settingsManager: SettingsManager,
        private val getLaunch: GetLaunch

) : CoroutineWorker(appContext, workerParams) {

    companion object {
        const val LAUNCH_ID_KEY = "extra-launch-key"

        fun createWorkRequest(launchId: Long, dateTimeNotification: LocalDateTime): OneTimeWorkRequest {
            Timber.d("createWorkRequest $launchId at $dateTimeNotification")

            val constrains = Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .setRequiresCharging(false)
                    .build()

            return OneTimeWorkRequestBuilder<ShowLaunchNotificationWorker>()
                    .setConstraints(constrains)
                    .setInputData(workDataOf(LAUNCH_ID_KEY to launchId))
                    .setInitialDelay(dateTimeNotification.toTimeStamp() - System.currentTimeMillis(), TimeUnit.MILLISECONDS)
                    .build()
        }
    }

    override suspend fun doWork(): Result {
        val launchId = inputData.getLong(LAUNCH_ID_KEY, 0)
        ShowLaunchNotificationWorkController(getLaunch, notificationsHelper, LocalDateTime.now(), settingsManager).doWork(launchId)
        return Result.success()
    }

    @AssistedInject.Factory
    interface Factory : ChildWorkerFactory

}
