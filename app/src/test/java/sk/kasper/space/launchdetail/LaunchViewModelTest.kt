package sk.kasper.space.launchdetail

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import sk.kasper.domain.usecase.launchdetail.GetLaunch
import sk.kasper.space.utils.CoroutinesMainDispatcherRule

@RunWith(MockitoJUnitRunner::class)
class LaunchViewModelTest {

    @Mock
    private lateinit var getLaunch: GetLaunch

    @get:Rule
    @ExperimentalCoroutinesApi
    var coroutinesMainDispatcherRule = CoroutinesMainDispatcherRule()

    @Test
    fun loadLaunchDetail() = runBlocking {
/*        whenever(getLaunch.getLaunch(0)).thenReturn(createLaunch(0, rocketName = "Falcon"))
        val viewModel = LaunchViewModel(getLaunch, 0)
        assertEquals("Falcon", viewModel.rocketName)*/
    }
}