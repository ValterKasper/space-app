package sk.kasper.domain.usecase.impl

import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.threeten.bp.Duration
import org.threeten.bp.LocalDateTime
import org.threeten.bp.Month
import sk.kasper.base.SettingsManager
import sk.kasper.domain.usecase.impl.fakes.FakeNotificationsHelper
import sk.kasper.domain.utils.createLaunch
import sk.kasper.entity.Launch

@RunWith(MockitoJUnitRunner::class)
class ShowLaunchNotificationImplTest {

    companion object {
        private val LOCAL_DATE_TIME_NOW: LocalDateTime = LocalDateTime.of(2000, Month.JANUARY, 1, 12, 0)
        private const val DURATION_BEFORE_NOTIFICATION_IS_SHOWN_MINUTES = 60
        private const val LAUNCH_ID = "10"
    }

    @Mock
    private lateinit var settingsManager: SettingsManager

    private val notificationsHelper = FakeNotificationsHelper()

    private lateinit var showLaunchNotification: ShowLaunchNotificationImpl

    @Before
    fun setUp() {
        prepareUseCase()
        whenever(settingsManager.durationBeforeNotificationIsShown).thenReturn(
            DURATION_BEFORE_NOTIFICATION_IS_SHOWN_MINUTES
        )
        whenever(settingsManager.showLaunchNotifications).thenReturn(true)
    }

    @Test
    fun `when launch has not completed yet then show notification`() = runBlocking {
        val launchDateTime = LOCAL_DATE_TIME_NOW.plusMinutes(30)
        setLaunchResponse(launchDateTime)

        showLaunchNotification(LAUNCH_ID)

        verifyShowNotification(launchDateTime)
    }

    @Test
    fun `when launch notifications are turned off then do nothing`() = runBlocking {
        whenever(settingsManager.showLaunchNotifications).thenReturn(false)
        val launchDateTime = LOCAL_DATE_TIME_NOW.plusMinutes(30)
        setLaunchResponse(launchDateTime)

        showLaunchNotification(LAUNCH_ID)

        verifyNeverShowNotification()
    }

    @Test
    fun `when launch has finished then do nothing`() = runBlocking {
        setLaunchResponse(LOCAL_DATE_TIME_NOW.minusMinutes(30))

        showLaunchNotification(LAUNCH_ID)

        verifyNeverShowNotification()
    }

    @Test
    fun `when launch is far in the future then do nothing`() = runBlocking {
        val longDuration = Duration.ofMinutes(DURATION_BEFORE_NOTIFICATION_IS_SHOWN_MINUTES.toLong()).plusMinutes(20)
        setLaunchResponse(LOCAL_DATE_TIME_NOW.plus(longDuration))

        showLaunchNotification(LAUNCH_ID)

        verifyNeverShowNotification()
    }

    private fun verifyShowNotification(launchDateTime: LocalDateTime) {
        val launchNotificationInfo = notificationsHelper.shownNotifications.first()
        assertThat(launchNotificationInfo.id).isEqualTo(LAUNCH_ID)
        assertThat(launchNotificationInfo.launchDateTime).isEqualTo(launchDateTime)
    }

    private fun verifyNeverShowNotification() {
        assertThat(notificationsHelper.shownNotifications.size).isEqualTo(0)
    }

    private var launchResponse: Launch? = null

    private fun setLaunchResponse(launchDateTime: LocalDateTime) {
        launchResponse = createLaunch(
            id = "id",
            launchName = "Name",
            launchDateTime = launchDateTime
        )
    }

    private fun prepareUseCase() {
        showLaunchNotification = ShowLaunchNotificationImpl(
            { launchResponse!! },
            notificationsHelper,
            settingsManager,
            { LOCAL_DATE_TIME_NOW }
        )
    }

}