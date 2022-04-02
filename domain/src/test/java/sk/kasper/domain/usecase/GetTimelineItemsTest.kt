package sk.kasper.domain.usecase

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import sk.kasper.domain.model.FilterSpec
import sk.kasper.domain.model.Response
import sk.kasper.domain.model.SuccessResponse
import sk.kasper.domain.usecase.impl.GetTimelineItemsImpl
import sk.kasper.domain.utils.createLaunch
import sk.kasper.entity.Launch
import sk.kasper.entity.Rocket
import sk.kasper.entity.Tag
import sk.kasper.repository.LaunchRepository
import sk.kasper.repository.SyncLaunchesRepository

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class GetTimelineItemsTest {

    private lateinit var getTimelineItems: GetTimelineItemsImpl

    @Mock
    private lateinit var repo: LaunchRepository

    @Mock
    private lateinit var sync: SyncLaunchesRepository

    private companion object {
        private const val LAUNCH_ID_1 = "ID 1"
        private const val LAUNCH_ID_2 = "ID 2"
    }

    @Before
    fun setUp() {
        getTimelineItems = GetTimelineItemsImpl(repo, sync)
    }

    @Test
    fun getObservable_returnAll() = runTest {
        whenever(sync.doSync(any())).thenReturn(true)
        whenever(repo.getLaunches()).thenReturn(listOf(createLaunch(id = LAUNCH_ID_1, tags = emptyList())))

        val timelineItems = getTimelineItems(FilterSpec.EMPTY_FILTER)

        assertTrue(checkSuccessResponseLaunches(timelineItems, setOf(LAUNCH_ID_1)))
    }

    @Test
    fun getObservable_filterByTag() = runTest {
        whenever(sync.doSync(any())).thenReturn(true)
        whenever(repo.getLaunches()).thenReturn(
            listOf(
                createLaunch(LAUNCH_ID_1, tags = listOf(Tag(LAUNCH_ID_1, Tag.ISS))),
                createLaunch(LAUNCH_ID_2, tags = listOf(Tag(LAUNCH_ID_2, Tag.CUBE_SAT)))
            )
        )

        val timelineItems = getTimelineItems(FilterSpec(setOf(Tag.CUBE_SAT)))

        assertTrue(checkSuccessResponseLaunches(timelineItems, setOf(LAUNCH_ID_2)))
    }

    @Test
    fun getObservable_filterByRocket() = runTest {
        whenever(sync.doSync(any())).thenReturn(true)
        whenever(repo.getLaunches()).thenReturn(
            listOf(
                createLaunch(id = LAUNCH_ID_1, rocketId = Rocket.FALCON_9),
                createLaunch(id = LAUNCH_ID_2, rocketId = Rocket.ARIANE_5)
            )
        )

        val timelineItems = getTimelineItems(FilterSpec(rockets = setOf(Rocket.FALCON_9)))

        assertTrue(checkSuccessResponseLaunches(timelineItems, setOf(LAUNCH_ID_1)))
    }

    private fun checkSuccessResponseLaunches(it: Response<List<Launch>>, launchIds: Set<String>): Boolean {
        it as SuccessResponse
        return it.data.map { it.id }.toSet() == launchIds
    }

}