package sk.kasper.space.notification.showLaunchNotificationJob

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.check
import com.nhaarman.mockitokotlin2.never
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner
import org.threeten.bp.Duration
import org.threeten.bp.LocalDateTime
import org.threeten.bp.Month
import sk.kasper.domain.usecase.launchdetail.GetLaunch
import sk.kasper.domain.utils.createLaunch
import sk.kasper.space.notification.NotificationsHelper
import sk.kasper.space.settings.SettingsManager

@RunWith(MockitoJUnitRunner::class)
class ShowLaunchNotificationWorkControllerTest {

    companion object {
        val LOCAL_DATE_TIME_NOW: LocalDateTime = LocalDateTime.of(2000, Month.JANUARY, 1, 12, 0)
        const val DURATION_BEFORE_NOTIFICATION_IS_SHOWN_MINUTES = 60
    }

    @Mock
    private lateinit var notificationsHelper: NotificationsHelper

    @Mock
    private lateinit var settingsManager: SettingsManager

    @Mock
    private lateinit var getLaunch: GetLaunch

    private lateinit var controller: ShowLaunchNotificationWorkController

    @Before
    fun setUp() {
        prepareController()
        whenever(settingsManager.durationBeforeNotificationIsShown).thenReturn(DURATION_BEFORE_NOTIFICATION_IS_SHOWN_MINUTES)
        whenever(settingsManager.showLaunchNotifications).thenReturn(true)
    }

    @Test
    fun onStartJob_launchNotCompleted_showNotification() = runBlocking {
        val launchDateTime = LOCAL_DATE_TIME_NOW.plusMinutes(30)
        mockGetLaunchResponse(launchDateTime)

        onStartJob()

        verifyShowNotification(launchDateTime)
    }

    @Test
    fun onStartJob_launchNotificationsAreTurnedOff_doNothing() = runBlocking {
        whenever(settingsManager.showLaunchNotifications).thenReturn(false)
        val launchDateTime = LOCAL_DATE_TIME_NOW.plusMinutes(30)
        mockGetLaunchResponse(launchDateTime)

        onStartJob()

        verifyNeverShowNotification()
    }

    @Test
    fun onStartJob_launchCompleted_doNothing() = runBlocking {
        mockGetLaunchResponse(LOCAL_DATE_TIME_NOW.minusMinutes(30))

        onStartJob()

        verifyNeverShowNotification()
    }

    @Test
    fun onStartJob_launchFarInFuture_doNothing() = runBlocking {
        val longDuration = Duration.ofMinutes(DURATION_BEFORE_NOTIFICATION_IS_SHOWN_MINUTES.toLong()).plusMinutes(20)
        mockGetLaunchResponse(LOCAL_DATE_TIME_NOW.plus(longDuration))

        onStartJob()

        verifyNeverShowNotification()
    }

    private suspend fun onStartJob() {
        controller.doWork(10L)
    }

    private fun verifyShowNotification(launchDateTime: LocalDateTime) {
        verify(notificationsHelper).showLaunchNotification(check {
            assertThat(it.id, `is`(10L))
            assertThat(it.launchDateTime, `is`(launchDateTime))
        })
    }

    private fun verifyNeverShowNotification() {
        verify(notificationsHelper, never()).showLaunchNotification(any())
    }

    private fun mockGetLaunchResponse(launchDateTime: LocalDateTime) = runBlocking {
        whenever(getLaunch.getLaunch(any())).thenReturn(createLaunch(
                id = 10L,
                launchName = "Name",
                launchDateTime = launchDateTime
        ))
    }

    private fun prepareController() {
        controller = ShowLaunchNotificationWorkController(getLaunch, notificationsHelper, LOCAL_DATE_TIME_NOW, settingsManager)
    }

}