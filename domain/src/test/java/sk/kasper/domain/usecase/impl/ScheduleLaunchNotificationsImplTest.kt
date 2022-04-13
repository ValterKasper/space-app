package sk.kasper.domain.usecase.impl

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.eq
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.Mockito.never
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner
import org.threeten.bp.LocalDateTime
import sk.kasper.base.notification.EnqueueLaunchNotification
import sk.kasper.domain.utils.createLaunch

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class ScheduleLaunchNotificationsImplTest {

    companion object {
        private val CURRENT_DATE_TIME: LocalDateTime = LocalDateTime.of(2001, 4, 30, 10, 0)
        private const val LAUNCH_ID = "id"
    }

    private lateinit var scheduleLaunchNotifications: ScheduleLaunchNotificationsImpl

    @Mock
    private lateinit var enqueueLaunchNotification: EnqueueLaunchNotification

    @Before
    fun setUp() {
        scheduleLaunchNotifications = ScheduleLaunchNotificationsImpl(
            enqueueLaunchNotification
        ) { CURRENT_DATE_TIME }
    }

    @Test
    fun `when launch is in near future then should not be enqueued`() = runTest {
        val launch = createLaunchAtTime(CURRENT_DATE_TIME.plusHours(22))

        scheduleLaunchNotifications(listOf(launch))

        verify(enqueueLaunchNotification).invoke(eq(LAUNCH_ID), any())
    }

    @Test
    fun `when launch is in near future and has inaccurate date then should not be enqueued`() = runTest {
        val launch = createLaunchAtTime(CURRENT_DATE_TIME.plusHours(22), accurateDate = false)

        scheduleLaunchNotifications(listOf(launch))

        verify(enqueueLaunchNotification, never()).invoke(anyString(), any())
    }

    @Test
    fun `when launch is in very near the future then should not be enqueued`() = runTest {
        val launch = createLaunchAtTime(CURRENT_DATE_TIME.plusHours(1))

        scheduleLaunchNotifications(listOf(launch))

        verify(enqueueLaunchNotification, never()).invoke(anyString(), any())
    }

    @Test
    fun `when launch is in very far the future then should not be enqueued`() = runTest {
        val launch = createLaunchAtTime(CURRENT_DATE_TIME.plusDays(30))

        scheduleLaunchNotifications(listOf(launch))

        verify(enqueueLaunchNotification, never()).invoke(anyString(), any())
    }

    @Test
    fun `when launch already happened then should not be enqueued`() = runTest {
        val launch = createLaunchAtTime(CURRENT_DATE_TIME.minusMinutes(20))

        scheduleLaunchNotifications(listOf(launch))

        verify(enqueueLaunchNotification, never()).invoke(anyString(), any())
    }

    private fun createLaunchAtTime(
        launchDateTime: LocalDateTime,
        accurateDate: Boolean = true,
        accurateTime: Boolean = true
    ) = createLaunch(
        id = LAUNCH_ID,
        launchDateTime = launchDateTime,
        accurateDate = accurateDate,
        accurateTime = accurateTime
    )

}