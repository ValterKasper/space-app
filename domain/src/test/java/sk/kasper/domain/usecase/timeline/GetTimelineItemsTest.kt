package sk.kasper.domain.usecase.timeline

//@RunWith(MockitoJUnitRunner::class)
class GetTimelineItemsTest {

//    private lateinit var useCase: GetTimelineItems
//
//    @Mock
//    private lateinit var repo: LaunchRepository
//
//    @Mock
//    private lateinit var sync: SyncLaunches
//
//    @Before
//    fun setUp() {
//        useCase = GetTimelineItems(repo, sync)
//    }

/*    @Test
    fun getObservable_returnAll() = runBlocking {
        val launchId = 1L
        whenever(repo.getLaunches()).thenReturn(listOf(
                createLaunch(launchId, emptyList())))

        val timelineItems = useCase.getTimelineItems(FilterSpec.EMPTY_FILTER)

        assertTrue(checkSuccessResponseLaunches(timelineItems, setOf(launchId)))
    }

    @Test
    fun getObservable_filterByTag() = runBlocking {
        val launchId1 = 1L
        val launchId2 = 2L
        whenever(repo.getLaunches()).thenReturn(listOf(
                createLaunch(launchId1, listOf(11L)),
                createLaunch(launchId2, listOf(12L))))

        val timelineItems = useCase.getTimelineItems(FilterSpec(setOf(12L)))

        assertTrue(checkSuccessResponseLaunches(timelineItems, setOf(launchId2)))
    }

    @Test
    fun getObservable_filterByRocket() = runBlocking {
        val launchId1 = 1L
        val launchId2 = 2L
        whenever(repo.getLaunches()).thenReturn(listOf(
                createLaunch(launchId1, rocketId = Rocket.FALCON_9),
                createLaunch(launchId2, rocketId = Rocket.ARIANE_5)))

        val timelineItems = useCase.getTimelineItems(FilterSpec(rockets = setOf(Rocket.FALCON_9)))

        assertTrue(checkSuccessResponseLaunches(timelineItems, setOf(launchId1)))
    }

    private fun checkSuccessResponseLaunches(it: List<Launch>, launchIds: Set<Long>): Boolean {
        return it.map { it.id }.toSet() == launchIds
    }

    private fun createLaunch(launchId: Long, tagTypes: List<Long> = emptyList(), rocketId: Long? = null) = createLaunch(
            id = launchId,
            launchName = "Launch $launchId",
            rocketId = rocketId,
            tags = tagTypes.map { Tag(launchId, it) })*/

}