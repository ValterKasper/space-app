package sk.kasper.space.notification.showLaunchNotificationJob

import android.content.Context
import androidx.work.ExistingWorkPolicy
import androidx.work.WorkManager
import dagger.hilt.android.qualifiers.ApplicationContext
import org.threeten.bp.LocalDateTime
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Naplanuje zobrazenie notifikacie na konkretny cas
 */
@Singleton
open class ShowLaunchNotificationWorkerScheduler @Inject constructor(@ApplicationContext private val context: Context) {

    companion object {
        private const val UNIQUE_WORK_NAME = "Show notification work"
    }

    open fun scheduleLaunchNotification(launchId: String, dateTimeNotification: LocalDateTime) {
        WorkManager
            .getInstance(context)
            .enqueueUniqueWork(
                "$UNIQUE_WORK_NAME $launchId",
                        ExistingWorkPolicy.REPLACE,
                        ShowLaunchNotificationWorker.createWorkRequest(launchId, dateTimeNotification))
    }

}