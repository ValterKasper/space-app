package sk.kasper.work.notification

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.*
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import org.threeten.bp.LocalDateTime
import sk.kasper.base.logger.Logger
import sk.kasper.base.utils.toTimeStamp
import sk.kasper.domain.usecase.ShowLaunchNotification
import java.util.concurrent.TimeUnit

@HiltWorker
class ShowLaunchNotificationWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val showLaunchNotification: ShowLaunchNotification,
) : CoroutineWorker(appContext, workerParams) {

    companion object {
        private const val LAUNCH_ID_KEY = "extra-launch-key"

        fun createWorkRequest(launchId: String, dateTimeNotification: LocalDateTime): OneTimeWorkRequest {
            Logger.d("createWorkRequest $launchId at $dateTimeNotification")

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
        val launchId = inputData.getString(LAUNCH_ID_KEY) ?: ""
        showLaunchNotification(launchId)
        return Result.success()
    }

}
