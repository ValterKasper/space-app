package sk.kasper.space.launchdetail.section

//@RunWith(MockitoJUnitRunner::class)
open class LaunchSiteViewModelTest {
/*

    companion object {
        private const val LAUNCH_ID = 100L
    }

    @Mock
    private lateinit var getLaunchSite: GetLaunchSite

    @get:Rule
    @ExperimentalCoroutinesApi
    var coroutinesMainDispatcherRule = CoroutinesMainDispatcherRule()

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Test
    fun setLaunchId_loadLaunchSite_success() = runBlocking {
        val launchSite = LaunchSite(Position(10.0, 20.0), "name")
        val successResponse = SuccessResponse(launchSite)
        whenever(getLaunchSite.getLaunchSite(LAUNCH_ID)).thenReturn(successResponse)
        val viewModel = LaunchSiteViewModel(LAUNCH_ID, true, getLaunchSite)

        viewModel.assertIsVisible(true)

        assertEquals(launchSite, viewModel.launchSite.value)
    }

    @Test
    fun setLaunchId_loadLaunchSite_error() = runBlocking {
        whenever(getLaunchSite.getLaunchSite(LAUNCH_ID)).thenReturn(ErrorResponse("Error"))
        val viewModel = LaunchSiteViewModel(LAUNCH_ID, false, getLaunchSite)

        viewModel.assertIsVisible(false)
        assertNull(viewModel.launchSite.value)
    }

    @Test
    fun getGoogleApiAvailable() {
        val viewModel = LaunchSiteViewModel(LAUNCH_ID, false, getLaunchSite)
        viewModel.assertIsVisible(false)
    }

    private fun SectionViewModel.assertIsVisible(visible: Boolean) {
        assertThat(this.visible, `is`(visible))
    }
*/

}