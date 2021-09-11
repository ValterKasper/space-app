package sk.kasper.domain.usecase.launchdetail

//@RunWith(MockitoJUnitRunner::class)
class GetLaunchSiteTest {

//    @Mock
//    private lateinit var launchSiteRepository : LaunchSiteRepository
//
//    private lateinit var getLaunchSite : GetLaunchSite
//
//    @Before
//    fun setUp() {
//        getLaunchSite = GetLaunchSite(launchSiteRepository)
//    }

/*    @Test
    fun getLaunchSite_error_errorResponse() = runBlocking {
        doAnswer { throw Exception("hej") }.`when`(launchSiteRepository).getLaunchSite(42)

        assertEquals(ErrorResponse("hej"), getLaunchSite.getLaunchSite(42))
    }

    @Test
    fun getLaunchSite_result_successResponse() = runBlocking {
        val launchSite = LaunchSite(Position(10.0, 20.0), "Name")
        whenever(launchSiteRepository.getLaunchSite(anyLong())).thenReturn(launchSite)

        assertEquals(SuccessResponse(launchSite), getLaunchSite.getLaunchSite(42))
    }*/

}