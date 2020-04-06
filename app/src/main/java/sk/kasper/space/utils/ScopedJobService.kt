package sk.kasper.space.utils

import android.app.job.JobParameters
import android.app.job.JobService
import androidx.annotation.CallSuper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

abstract class ScopedJobService: JobService(), CoroutineScope {

    private val job = Job()
    override val coroutineContext = Dispatchers.Main + job

    @CallSuper
    override fun onStopJob(jobParameters: JobParameters): Boolean {
        job.cancel()
        return false
    }

}