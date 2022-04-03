package sk.kasper.space.notification

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito.never
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner
import org.threeten.bp.LocalDateTime
import sk.kasper.domain.usecase.impl.ScheduleLaunchNotificationsImpl
import sk.kasper.domain.utils.createLaunch
import sk.kasper.repository.SyncLaunchesRepository

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class ScheduleLaunchNotificationsTest {

    companion object {
        val CURRENT_DATE_TIME: LocalDateTime = LocalDateTime.of(2001, 4, 30, 10, 0)
        const val LAUNCH_ID = "id"
    }

    private lateinit var checkerUnderTest: ScheduleLaunchNotificationsUnderTest

    @Mock
    private lateinit var workScheduler: EnqueueLaunchNotificationImpl

    @Mock
    private lateinit var repository: sk.kasper.repository.LaunchRepository

    @Mock
    private lateinit var syncLaunches: SyncLaunchesRepository

    @Test
    fun launchesChanged_launchInNearFutureInaccurateDate_shouldNotBeScheduled() = runTest {
        val launch = createLaunchAtTime(CURRENT_DATE_TIME.plusHours(22), accurateDate = false)

        whenever(repository.getLaunches()).thenReturn(listOf(launch))
        createChecker()
        callOnSync()

        verify(workScheduler, never()).enqueue(
            ArgumentMatchers.anyString(),
            any()
        )
    }

    @Test
    fun launchesChanged_launchInNearFutureInaccurateTime_shouldNotBeScheduled() = runTest {
        val launch = createLaunchAtTime(CURRENT_DATE_TIME.plusHours(22), accurateTime = false)

        whenever(repository.getLaunches()).thenReturn(listOf(launch))
        createChecker()
        callOnSync()

        verify(workScheduler, never()).enqueue(
            ArgumentMatchers.anyString(),
            any()
        )
    }

    @Test
    fun launchesChanged_launchInVeryNearFuture_shouldNotBeScheduled() = runTest {
        val launch = createLaunchAtTime(CURRENT_DATE_TIME.plusHours(1))

        whenever(repository.getLaunches()).thenReturn(listOf(launch))
        createChecker()
        callOnSync()

        verify(workScheduler, never()).enqueue(
            ArgumentMatchers.anyString(),
            any()
        )
    }

    @Test
    fun launchesChanged_launchInFarFuture_shouldBeNotScheduled() = runTest {
        val launch = createLaunchAtTime(CURRENT_DATE_TIME.plusDays(30))

        whenever(repository.getLaunches()).thenReturn(listOf(launch))
        createChecker()
        callOnSync()

        verify(workScheduler, never()).enqueue(
            ArgumentMatchers.anyString(),
            any()
        )
    }

    @Test
    fun launchesChanged_launchHasHappened_shouldBeNotScheduled() = runTest {
        val launch = createLaunchAtTime(CURRENT_DATE_TIME.minusMinutes(20))

        whenever(repository.getLaunches()).thenReturn(listOf(launch))
        createChecker()
        callOnSync()

        verify(workScheduler, never()).enqueue(
            ArgumentMatchers.anyString(),
            any()
        )
    }

    private fun createLaunchAtTime(launchDateTime: LocalDateTime, accurateDate: Boolean = true, accurateTime: Boolean = true) = createLaunch(id = LAUNCH_ID, launchDateTime = launchDateTime, accurateDate = accurateDate, accurateTime = accurateTime)

    private fun createChecker() {
        checkerUnderTest = ScheduleLaunchNotificationsUnderTest()
        checkerUnderTest.onStart(mock())
    }

    private fun callOnSync() {
        val argumentCaptor = argumentCaptor<SyncLaunchesRepository.SyncListener> { }
        verify(syncLaunches).addSyncListener(argumentCaptor.capture())
        argumentCaptor.firstValue.onSync()
    }

    inner class ScheduleLaunchNotificationsUnderTest :
        ScheduleLaunchNotificationsImpl(repository, syncLaunches, workScheduler) {

        override fun getCurrentDateTime(): LocalDateTime {
            return CURRENT_DATE_TIME
        }

    }

}