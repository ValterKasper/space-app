package sk.kasper.space.notification.showLaunchNotificationJob

import com.nhaarman.mockitokotlin2.*
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.threeten.bp.Duration
import org.threeten.bp.LocalDateTime
import org.threeten.bp.Month
import sk.kasper.base.SettingsManager
import sk.kasper.domain.usecase.impl.ShowLaunchNotificationImpl
import sk.kasper.domain.utils.createLaunch
import sk.kasper.entity.Launch
import sk.kasper.space.notification.NotificationsHelperImpl

@RunWith(MockitoJUnitRunner::class)
class ShowLaunchNotificationTest {

    companion object {
        val LOCAL_DATE_TIME_NOW: LocalDateTime = LocalDateTime.of(2000, Month.JANUARY, 1, 12, 0)
        const val DURATION_BEFORE_NOTIFICATION_IS_SHOWN_MINUTES = 60
    }

    @Mock
    private lateinit var notificationsHelper: NotificationsHelperImpl

    @Mock
    private lateinit var settingsManager: SettingsManager

    private lateinit var controller: ShowLaunchNotificationImpl

    @Before
    fun setUp() {
        prepareController()
        whenever(settingsManager.durationBeforeNotificationIsShown).thenReturn(DURATION_BEFORE_NOTIFICATION_IS_SHOWN_MINUTES)
        whenever(settingsManager.showLaunchNotifications).thenReturn(true)
    }

    @Test
    fun onStartJob_launchNotCompleted_showNotification() = runBlocking {
        val launchDateTime = LOCAL_DATE_TIME_NOW.plusMinutes(30)
        setLaunchResponse(launchDateTime)

        onStartJob()

        verifyShowNotification(launchDateTime)
    }

    @Test
    fun onStartJob_launchNotificationsAreTurnedOff_doNothing() = runBlocking {
        whenever(settingsManager.showLaunchNotifications).thenReturn(false)
        val launchDateTime = LOCAL_DATE_TIME_NOW.plusMinutes(30)
        setLaunchResponse(launchDateTime)

        onStartJob()

        verifyNeverShowNotification()
    }

    @Test
    fun onStartJob_launchCompleted_doNothing() = runBlocking {
        setLaunchResponse(LOCAL_DATE_TIME_NOW.minusMinutes(30))

        onStartJob()

        verifyNeverShowNotification()
    }

    @Test
    fun onStartJob_launchFarInFuture_doNothing() = runBlocking {
        val longDuration = Duration.ofMinutes(DURATION_BEFORE_NOTIFICATION_IS_SHOWN_MINUTES.toLong()).plusMinutes(20)
        setLaunchResponse(LOCAL_DATE_TIME_NOW.plus(longDuration))

        onStartJob()

        verifyNeverShowNotification()
    }

    private suspend fun onStartJob() {
        controller.doWork("10L")
    }

    private fun verifyShowNotification(launchDateTime: LocalDateTime) {
        verify(notificationsHelper).showLaunchNotification(check {
            assertThat(it.id, `is`("10L"))
            assertThat(it.launchDateTime, `is`(launchDateTime))
        })
    }

    private fun verifyNeverShowNotification() {
        verify(notificationsHelper, never()).showLaunchNotification(any())
    }

    private var launchResponse: Launch? = null

    private fun setLaunchResponse(launchDateTime: LocalDateTime) {
        launchResponse = createLaunch(
            id = "id",
            launchName = "Name",
            launchDateTime = launchDateTime
        )
    }

    private fun prepareController() {
        controller = ShowLaunchNotificationImpl(
            { launchResponse!! },
            notificationsHelper,
            LOCAL_DATE_TIME_NOW,
            settingsManager
        )
    }

}