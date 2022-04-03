package sk.kasper.space.notification

import android.content.Context
import androidx.work.*
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import org.threeten.bp.LocalDateTime
import sk.kasper.base.logger.Logger
import sk.kasper.domain.usecase.ShowLaunchNotification
import sk.kasper.entity.utils.toTimeStamp
import sk.kasper.space.work.ChildWorkerFactory
import java.util.concurrent.TimeUnit

class ShowLaunchNotificationWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val showLaunchNotification: ShowLaunchNotification,
) : CoroutineWorker(appContext, workerParams) {

    companion object {
        const val LAUNCH_ID_KEY = "extra-launch-key"

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

    @AssistedFactory
    interface Factory : ChildWorkerFactory {
        override fun create(
            appContext: Context,
            workerParams: WorkerParameters
        ): ShowLaunchNotificationWorker
    }

}
