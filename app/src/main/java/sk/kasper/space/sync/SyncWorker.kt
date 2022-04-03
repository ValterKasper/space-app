package sk.kasper.space.sync

import android.content.Context
import androidx.work.*
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import sk.kasper.base.Flags
import sk.kasper.domain.model.ErrorResponse
import sk.kasper.domain.model.SuccessResponse
import sk.kasper.domain.usecase.RefreshTimelineItems
import sk.kasper.space.work.ChildWorkerFactory
import timber.log.Timber
import java.util.concurrent.TimeUnit

class SyncWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val refreshTimelineItems: RefreshTimelineItems
)
    : CoroutineWorker(appContext, workerParams) {

    companion object {
        private const val UNIQUE_PERIODIC_WORK_NAME = "Sync launches work"

        fun startPeriodicWork(context: Context, flags: Flags) {
            val constrains = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .setRequiresCharging(false)
                .build()

            val syncRequest = PeriodicWorkRequestBuilder<SyncWorker>(flags.synIntervalHours, TimeUnit.HOURS)
                .setConstraints(constrains)
                .build()

            WorkManager
                    .getInstance(context)
                    .enqueueUniquePeriodicWork(UNIQUE_PERIODIC_WORK_NAME, ExistingPeriodicWorkPolicy.KEEP, syncRequest)

            Timber.d("periodic launches sync work request started")
        }

    }

    override suspend fun doWork(): Result {
        Timber.d("doWork")
        return when (refreshTimelineItems()) {
            is SuccessResponse -> {
                Timber.d("doWork - success")
                Result.success()
            }
            is ErrorResponse -> {
                Timber.d("doWork - failure")
                Result.failure()
            }
        }
    }

    @AssistedFactory
    interface Factory : ChildWorkerFactory {
        override fun create(appContext: Context, workerParams: WorkerParameters): SyncWorker
    }

}