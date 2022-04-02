package sk.kasper.repository

import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner
import org.threeten.bp.LocalDateTime
import org.threeten.bp.Month
import sk.kasper.database.dao.LaunchDao
import sk.kasper.database.entity.LaunchDetailEntity
import sk.kasper.entity.utils.toTimeStamp
import sk.kasper.repository.impl.LaunchRepositoryImpl

private val LOCAL_DATE_TIME_NOW: LocalDateTime = LocalDateTime.of(2000, Month.JANUARY, 1, 12, 0)

@RunWith(MockitoJUnitRunner::class)
class LaunchRepositoryTest {

    private lateinit var repo: LaunchRepositoryImpl

    @Mock
    private lateinit var launchDao: LaunchDao

    @Before
    fun setUp() {
        repo = LaunchRepositoryUnderTest()
    }

    @Test
    fun getLaunches_returnLaunchesFromLaunchDao() = runBlocking {
        val launches = listOf(
            LaunchDetailEntity(
                "id",
                LOCAL_DATE_TIME_NOW.plusDays(7).toTimeStamp(),
                "R1",
                mainPhotoUrl = null
            )
        )
        returnLaunchesFromDao(launches)

        val repoLaunches = repo.getLaunches()
        assertEquals(1, repoLaunches.size)
        assertEquals(launches[0].id, repoLaunches[0].id)
    }

    private suspend fun returnLaunchesFromDao(list: List<LaunchDetailEntity>) {
        `when`(launchDao.getLaunches()).thenReturn(list)
    }

    internal inner class LaunchRepositoryUnderTest
        : LaunchRepositoryImpl(launchDao) {

        override fun getCurrentDateTime(): LocalDateTime {
            return LOCAL_DATE_TIME_NOW
        }
    }

}
