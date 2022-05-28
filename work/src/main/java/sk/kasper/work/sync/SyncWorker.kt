package sk.kasper.work.sync

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.*
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import sk.kasper.base.Flags
import sk.kasper.base.logger.Logger
import sk.kasper.domain.model.ErrorResponse
import sk.kasper.domain.model.SuccessResponse
import sk.kasper.domain.usecase.RefreshTimelineItems
import java.util.concurrent.TimeUnit

@HiltWorker
class SyncWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val refreshTimelineItems: RefreshTimelineItems
) : CoroutineWorker(appContext, workerParams) {

    companion object {
        private const val UNIQUE_PERIODIC_WORK_NAME = "Sync launches work"

        fun startPeriodicWork(context: Context, flags: Flags) {
            val constrains = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .setRequiresCharging(false)
                .build()

            val syncRequest = PeriodicWorkRequestBuilder<SyncWorker>(flags.syncIntervalHours, TimeUnit.HOURS)
                .setConstraints(constrains)
                .build()

            WorkManager
                .getInstance(context)
                .enqueueUniquePeriodicWork(UNIQUE_PERIODIC_WORK_NAME, ExistingPeriodicWorkPolicy.KEEP, syncRequest)

            Logger.d("periodic launches sync work request started")
        }

    }

    override suspend fun doWork(): Result {
        Logger.d("doWork")
        return when (refreshTimelineItems()) {
            is SuccessResponse -> {
                Logger.d("doWork - success")
                Result.success()
            }
            is ErrorResponse -> {
                Logger.d("doWork - failure")
                Result.failure()
            }
        }
    }

}