package sk.kasper.space.notification

import android.content.Context
import androidx.work.ExistingWorkPolicy
import androidx.work.WorkManager
import dagger.hilt.android.qualifiers.ApplicationContext
import org.threeten.bp.LocalDateTime
import sk.kasper.base.notification.EnqueueLaunchNotification
import javax.inject.Inject

class EnqueueLaunchNotificationImpl @Inject constructor(@ApplicationContext private val context: Context) :
    EnqueueLaunchNotification {

    companion object {
        private const val UNIQUE_WORK_NAME = "Show notification work"
    }

    override operator fun invoke(launchId: String, dateTimeNotification: LocalDateTime) {
        WorkManager
            .getInstance(context)
            .enqueueUniqueWork(
                "$UNIQUE_WORK_NAME $launchId",
                ExistingWorkPolicy.REPLACE,
                ShowLaunchNotificationWorker.createWorkRequest(launchId, dateTimeNotification)
            )
    }

}