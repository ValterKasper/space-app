package sk.kasper.space.notification

import com.nhaarman.mockitokotlin2.*
import kotlinx.coroutines.runBlocking
import org.junit.Ignore
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito.never
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner
import org.threeten.bp.LocalDateTime
import sk.kasper.domain.model.SyncLaunches
import sk.kasper.domain.repository.LaunchRepository
import sk.kasper.domain.utils.createLaunch
import sk.kasper.space.notification.showLaunchNotificationJob.LaunchNotificationChecker
import sk.kasper.space.notification.showLaunchNotificationJob.ShowLaunchNotificationWorkerScheduler

@RunWith(MockitoJUnitRunner::class)
@Ignore("Failing on CI with UnnecessaryStubbingException")
class LaunchNotificationCheckerTest {

    companion object {
        val CURRENT_DATE_TIME: LocalDateTime = LocalDateTime.of(2001, 4, 30, 10, 0)
        const val LAUNCH_ID = "id"
    }

    private lateinit var checkerUnderTest: LaunchNotificationCheckerUnderTest

    @Mock
    private lateinit var workScheduler: ShowLaunchNotificationWorkerScheduler

    @Mock
    private lateinit var repository: LaunchRepository

    @Mock
    private lateinit var syncLaunches: SyncLaunches

    @Test
    @Ignore("Fix me!!!")
    fun launchesChanged_launchInNearFuture_shouldBeScheduled() = runBlocking {
        val launch = createLaunchAtTime(CURRENT_DATE_TIME.plusHours(22))

        whenever(repository.getLaunches()).thenReturn(listOf(launch))
        createChecker()
        callOnSync()

        verify(workScheduler).scheduleLaunchNotification(
                eq(LAUNCH_ID),
                argThat {
                    isAfter(CURRENT_DATE_TIME) && isBefore(launch.launchDateTime)
                })
    }

    @Test
    fun launchesChanged_launchInNearFutureInaccurateDate_shouldNotBeScheduled() = runBlocking {
        val launch = createLaunchAtTime(CURRENT_DATE_TIME.plusHours(22), accurateDate = false)

        whenever(repository.getLaunches()).thenReturn(listOf(launch))
        createChecker()
        callOnSync()

        verify(workScheduler, never()).scheduleLaunchNotification(
            ArgumentMatchers.anyString(),
            any()
        )
    }

    @Test
    fun launchesChanged_launchInNearFutureInaccurateTime_shouldNotBeScheduled() = runBlocking {
        val launch = createLaunchAtTime(CURRENT_DATE_TIME.plusHours(22), accurateTime = false)

        whenever(repository.getLaunches()).thenReturn(listOf(launch))
        createChecker()
        callOnSync()

        verify(workScheduler, never()).scheduleLaunchNotification(
            ArgumentMatchers.anyString(),
            any()
        )
    }

    @Test
    fun launchesChanged_launchInVeryNearFuture_shouldNotBeScheduled() = runBlocking {
        val launch = createLaunchAtTime(CURRENT_DATE_TIME.plusHours(1))

        whenever(repository.getLaunches()).thenReturn(listOf(launch))
        createChecker()
        callOnSync()

        verify(workScheduler, never()).scheduleLaunchNotification(
            ArgumentMatchers.anyString(),
            any()
        )
    }

    @Test
    fun launchesChanged_launchInFarFuture_shouldBeNotScheduled() = runBlocking {
        val launch = createLaunchAtTime(CURRENT_DATE_TIME.plusDays(30))

        whenever(repository.getLaunches()).thenReturn(listOf(launch))
        createChecker()
        callOnSync()

        verify(workScheduler, never()).scheduleLaunchNotification(
            ArgumentMatchers.anyString(),
            any()
        )
    }

    @Test
    fun launchesChanged_launchHasHappened_shouldBeNotScheduled() = runBlocking {
        val launch = createLaunchAtTime(CURRENT_DATE_TIME.minusMinutes(20))

        whenever(repository.getLaunches()).thenReturn(listOf(launch))
        createChecker()
        callOnSync()

        verify(workScheduler, never()).scheduleLaunchNotification(
            ArgumentMatchers.anyString(),
            any()
        )
    }

    private fun createLaunchAtTime(launchDateTime: LocalDateTime, accurateDate: Boolean = true, accurateTime: Boolean = true) = createLaunch(id = LAUNCH_ID, launchDateTime = launchDateTime, accurateDate = accurateDate, accurateTime = accurateTime)

    private fun createChecker() {
        checkerUnderTest = LaunchNotificationCheckerUnderTest()
        checkerUnderTest.onStart(mock())
    }

    private fun callOnSync() {
        val argumentCaptor = argumentCaptor<SyncLaunches.SyncListener> { }
        verify(syncLaunches).addSyncListener(argumentCaptor.capture())
        argumentCaptor.firstValue.onSync()
    }

    inner class LaunchNotificationCheckerUnderTest : LaunchNotificationChecker(repository, syncLaunches, workScheduler) {

        override fun getCurrentDateTime(): LocalDateTime {
            return CURRENT_DATE_TIME
        }

    }

}