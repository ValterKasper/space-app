package sk.kasper.space.notification.showLaunchNotificationJob

import android.app.job.JobInfo
import android.app.job.JobParameters
import android.content.ComponentName
import android.content.Context
import android.os.PersistableBundle
import dagger.android.AndroidInjection
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDateTime
import sk.kasper.domain.usecase.launchdetail.GetLaunch
import sk.kasper.space.notification.NotificationsHelper
import sk.kasper.space.settings.SettingsManager
import sk.kasper.space.utils.ScopedJobService
import sk.kasper.space.utils.toTimeStamp
import timber.log.Timber
import javax.inject.Inject

class ShowLaunchNotificationJob : ScopedJobService() {

    @Inject
    lateinit var notificationsHelper: NotificationsHelper

    @Inject
    lateinit var settingsManager: SettingsManager

    @Inject
    lateinit var getLaunch: GetLaunch

    companion object {
        const val EXTRA_LAUNCH_ID = "extra-launch-id"

        fun createJobInfo(context: Context, launchId: Long, dateTimeNotification: LocalDateTime, dateTimeNotificationDeadline: LocalDateTime): JobInfo? {
            Timber.d("scheduleLaunchNotification $launchId at $dateTimeNotification")

            val bundle = PersistableBundle()
            bundle.putLong(EXTRA_LAUNCH_ID, launchId)

            return JobInfo.Builder(launchId.toInt(), ComponentName(context, ShowLaunchNotificationJob::class.java))
                    .setRequiresDeviceIdle(false)
                    .setRequiresCharging(false)
                    .setPersisted(true)
                    .setExtras(bundle)
                    .setMinimumLatency(dateTimeNotification.toTimeStamp() - System.currentTimeMillis())
                    .setOverrideDeadline(dateTimeNotificationDeadline.toTimeStamp() - System.currentTimeMillis())
                    .build()
        }
    }

    override fun onCreate() {
        AndroidInjection.inject(this)
        super.onCreate()
    }

    override fun onStartJob(jobParameters: JobParameters): Boolean {
        launch(Dispatchers.IO) {
            val launchId = jobParameters.extras.getLong(EXTRA_LAUNCH_ID)
            ShowLaunchNotificationJobController(getLaunch, notificationsHelper, LocalDateTime.now(), settingsManager).onStartJob(launchId)
            jobFinished(jobParameters, false)
        }

        return false
    }

}
