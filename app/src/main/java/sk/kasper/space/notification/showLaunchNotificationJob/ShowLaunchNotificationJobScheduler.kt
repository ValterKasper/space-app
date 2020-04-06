package sk.kasper.space.notification.showLaunchNotificationJob

import android.app.job.JobScheduler
import android.content.Context
import org.threeten.bp.LocalDateTime
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Naplanuje zobrazenie notifikacie na konkretny cas
 */
@Singleton
open class ShowLaunchNotificationJobScheduler @Inject constructor(private val context: Context) {

    open fun scheduleLaunchNotification(launchId: Long, dateTimeNotification: LocalDateTime, dateTimeNotificationDeadline: LocalDateTime) {
        val jobScheduler: JobScheduler? = context.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
        jobScheduler?.let {
            val jobInfo = ShowLaunchNotificationJob.createJobInfo(context, launchId, dateTimeNotification, dateTimeNotificationDeadline)
            it.schedule(jobInfo)
        }
    }

}