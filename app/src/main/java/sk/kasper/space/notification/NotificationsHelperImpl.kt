package sk.kasper.space.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.text.format.DateUtils
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.navigation.NavDeepLinkBuilder
import dagger.hilt.android.qualifiers.ApplicationContext
import org.threeten.bp.Duration
import sk.kasper.base.notification.LaunchNotificationInfo
import sk.kasper.base.notification.NotificationsHelper
import sk.kasper.entity.utils.toTimeStamp
import sk.kasper.space.R
import sk.kasper.ui_common.mapper.RocketMapper
import sk.kasper.ui_common.utils.RoundedSquareLetterProvider
import sk.kasper.ui_common.utils.RoundedSquareTransformation
import sk.kasper.ui_common.utils.toPixels
import sk.kasper.ui_launch.LaunchFragmentArgs
import javax.inject.Inject

class NotificationsHelperImpl @Inject constructor(
    private val rocketMapper: RocketMapper,
    @ApplicationContext context: Context
) : ContextWrapper(context), NotificationsHelper {

    companion object {
        private const val LAUNCHES_CHANNEL = "launches"
    }

    private val notificationManager: NotificationManager by lazy {
        getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    /**
     * Registers notification channels, which can be used later by individual notifications.
     */
    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            initChannels()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initChannels() {
        val launchesChannel = NotificationChannel(
                LAUNCHES_CHANNEL,
                getString(R.string.notification_channel_launches),
                NotificationManager.IMPORTANCE_DEFAULT)

        // Configure the channel's initial settings
        launchesChannel.lightColor = Color.GREEN

        notificationManager.createNotificationChannel(launchesChannel)
    }

    override fun showLaunchNotification(launchNotificationInfo: LaunchNotificationInfo) {
        if (launchNotificationInfo.missionName == null || launchNotificationInfo.rocketName == null) {
            return
        }

        val formattedTime = DateUtils.formatDateTime(
            this,
            launchNotificationInfo.launchDateTime.toTimeStamp(),
            DateUtils.FORMAT_SHOW_TIME
        )
        val title = resources.getString(R.string.launch_x, launchNotificationInfo.rocketName)
        val text = resources.getString(R.string.mission_x_at_x, launchNotificationInfo.missionName, formattedTime)
        val notificationDuration = Duration.ofHours(2).toMillis()
        val timeoutAfter = launchNotificationInfo.timeoutAfter.toMillis()
        val largeIconBitmap = getRocketLargeIcon(launchNotificationInfo.rocketId, launchNotificationInfo.rocketName!!)

        val whenMs = System.currentTimeMillis() + notificationDuration

        val builder = NotificationCompat.Builder(this, LAUNCHES_CHANNEL)
                .setContentTitle(title)
                .setContentText(text)
                .setLargeIcon(largeIconBitmap)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setCategory(NotificationCompat.CATEGORY_RECOMMENDATION)
                .setWhen(whenMs)
                .setAutoCancel(true)
                .setTimeoutAfter(whenMs + timeoutAfter)
                .setSmallIcon(smallIcon)
                .setContentIntent(createPendingIntentForMainScreen(launchNotificationInfo.id))

        if (launchNotificationInfo.videoUrl != null) {
            builder.addAction(
                R.drawable.ic_live_tv,
                getString(R.string.video_stream),
                createPendingIntentForWebcast(launchNotificationInfo.videoUrl!!)
            )
        }

        notify(launchNotificationInfo.id.toInt(), builder)
    }

    private fun notify(id: Int, notification: NotificationCompat.Builder) {
        notificationManager.notify(id, notification.build())
    }

    private fun getRocketLargeIcon(rocketId: Long?, rocketName: String): Bitmap {
        val rocketIdToDrawableRes = rocketMapper.toDrawableRes(rocketId)

        return if (rocketIdToDrawableRes == 0) {
            RoundedSquareLetterProvider(this).createLetter(rocketName.first())
        } else {
            val squareBitmap = BitmapFactory.decodeResource(resources, rocketIdToDrawableRes)
            RoundedSquareTransformation(R.dimen.rounded_corners_radius.toPixels(this).toFloat()).transform(squareBitmap)
        }
    }

    private fun createPendingIntentForMainScreen(launchId: String): PendingIntent {
        return NavDeepLinkBuilder(this)
                .setArguments(LaunchFragmentArgs(launchId).toBundle())
                .setDestination(R.id.launchFragment)
                .setGraph(R.navigation.nav_graph)
                .createPendingIntent()
    }

    private fun createPendingIntentForWebcast(url: String): PendingIntent {
        val notificationIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        return PendingIntent.getActivity(this, 0, notificationIntent, 0)
    }

    private val smallIcon: Int
        get() = R.drawable.ic_notification

}