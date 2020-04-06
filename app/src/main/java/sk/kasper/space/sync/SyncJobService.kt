package sk.kasper.space.sync

import android.app.job.JobInfo
import android.app.job.JobParameters
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import dagger.android.AndroidInjection
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import sk.kasper.domain.model.SyncLaunches
import sk.kasper.space.BuildConfig
import sk.kasper.space.utils.ScopedJobService
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject


class SyncJobService : ScopedJobService()  {

    companion object {
        private const val LAUNCHES_SYNC_JOB_ID = 100

        fun startPeriodicLaunchesSyncJob(context: Context) {
            val jobScheduler: JobScheduler? = context.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler

            if (jobScheduler == null) {
                Timber.e("startPeriodicLaunchesSyncJob: jobScheduler == null")
                return
            }

            if (isJobPending(jobScheduler, LAUNCHES_SYNC_JOB_ID)) {
                Timber.d("startPeriodicLaunchesSyncJob: job is already pending")
                return
            }

            val builder = JobInfo.Builder(LAUNCHES_SYNC_JOB_ID, ComponentName(context, SyncJobService::class.java))
                    .setRequiresDeviceIdle(false)
                    .setRequiresCharging(false)
                    .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)

            // todo odstran
            val periodicMode = true
            if (periodicMode) {
                builder.setPeriodic(TimeUnit.HOURS.toMillis(BuildConfig.SYNC_INTERVAL_HOURS))
            } else {
                /**
                 * Just for testing.
                 * Periodic jobs have minimal interval 15min
                 */
                Timber.w("Periodic mode is turned off")
                builder.setMinimumLatency(5000)
                builder.setOverrideDeadline(10000)
            }

            val jobInfo = builder.build()

            jobScheduler.schedule(jobInfo)
            Timber.d("periodic launches sync job started")
        }

        private fun isJobPending(jobScheduler: JobScheduler, jobId: Int): Boolean {
            jobScheduler.allPendingJobs.forEach {
                if (it.id == jobId) {
                    return true
                }
            }

            return false
        }
    }

    @Inject
    lateinit var syncLaunches: SyncLaunches

    override fun onCreate() {
        AndroidInjection.inject(this)
        super.onCreate()
    }

    override fun onStartJob(jobParameters: JobParameters): Boolean {
        Timber.d("onStartJob")
        launch(Dispatchers.IO) {
            if (syncLaunches.doSync(force = true)) {
                Timber.d("onStartJob - success")
                jobFinished(jobParameters, false)
            } else {
                Timber.d("onStartJob - failure")
                jobFinished(jobParameters, true)
            }
        }

        return true
    }

    override fun onStopJob(jobParameters: JobParameters): Boolean {
        super.onStopJob(jobParameters)
        Timber.d("onStopJob")

        return false
    }

}