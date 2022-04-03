package sk.kasper.space.sync

import android.content.Context
import androidx.work.*
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import sk.kasper.base.Flags
import sk.kasper.repository.SyncLaunchesRepository
import sk.kasper.space.work.ChildWorkerFactory
import timber.log.Timber
import java.util.concurrent.TimeUnit

class SyncWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val syncLaunchesRepository: SyncLaunchesRepository
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
        return if (syncLaunchesRepository.doSync(force = true)) {
            Timber.d("doWork - success")
            Result.success()
        } else {
            Timber.d("doWork - failure")
            Result.failure()
        }
    }

    @AssistedFactory
    interface Factory : ChildWorkerFactory {
        override fun create(appContext: Context, workerParams: WorkerParameters): SyncWorker
    }

}