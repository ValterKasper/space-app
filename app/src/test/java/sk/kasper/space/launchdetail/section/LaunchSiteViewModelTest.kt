package sk.kasper.space.launchdetail.section

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import sk.kasper.domain.model.ErrorResponse
import sk.kasper.domain.model.LaunchSite
import sk.kasper.domain.model.Position
import sk.kasper.domain.model.SuccessResponse
import sk.kasper.domain.usecase.launchdetail.GetLaunchSite
import sk.kasper.space.utils.CoroutinesMainDispatcherRule

@RunWith(MockitoJUnitRunner::class)
open class LaunchSiteViewModelTest {

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

}