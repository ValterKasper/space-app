package sk.kasper.space.launchdetail.section

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert.*
import org.junit.Before
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

    @Mock
    private lateinit var getLaunchSite: GetLaunchSite

    private lateinit var viewModel: LaunchSiteViewModel

    @get:Rule
    @ExperimentalCoroutinesApi
    var coroutinesMainDispatcherRule = CoroutinesMainDispatcherRule()

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        viewModel = LaunchSiteViewModel(getLaunchSite)
    }

    @Test
    fun setLaunchId_loadLaunchSite_success() = runBlocking {
        val launchSite = LaunchSite(Position(10.0, 20.0), "name")
        val successResponse = SuccessResponse(launchSite)
        whenever(getLaunchSite.getLaunchSite(100)).thenReturn(successResponse)

        viewModel.launchId = 100

        assertIsVisible(true)

        assertEquals(launchSite, viewModel.launchSite.value)
    }

    @Test
    fun setLaunchId_loadLaunchSite_error() = runBlocking {
        whenever(getLaunchSite.getLaunchSite(100)).thenReturn(ErrorResponse("Error"))

        viewModel.launchId = 100

        assertIsVisible(false)
        assertNull(viewModel.launchSite.value)
    }

    @Test
    fun getGoogleApiAvailable() {
        viewModel.googleApiAvailable = false

        assertIsVisible(false)
    }

    private fun assertIsVisible(visible: Boolean) {
        assertThat(viewModel.visible, `is`(visible))
    }

}