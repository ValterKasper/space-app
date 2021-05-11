package sk.kasper.space.launchdetail.section

import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import sk.kasper.domain.model.Orbit
import sk.kasper.domain.usecase.launchdetail.GetOrbit
import sk.kasper.ui_launch.section.OrbitViewModel

@RunWith(MockitoJUnitRunner::class)
class OrbitViewModelTest {

    private lateinit var viewModel: OrbitViewModel

    @Mock
    private lateinit var getOrbit: GetOrbit

    @get:Rule
    @ExperimentalCoroutinesApi
    var coroutinesMainDispatcherRule = CoroutinesMainDispatcherRule()

    @Test
    fun leo_visible() = runBlocking {
        returnOrbit(Orbit.LEO)

        createViewModel()

        assertThat(viewModel.visible, `is`(true))
    }

    @Test
    fun gto_visible() = runBlocking {
        returnOrbit(Orbit.GTO)

        createViewModel()

        assertThat(viewModel.visible, `is`(true))
    }

    @Test
    fun geo_visible() = runBlocking {
        returnOrbit(Orbit.GEO)

        createViewModel()

        assertThat(viewModel.visible, `is`(true))
    }

    @Test
    fun heo_notVisible() = runBlocking {
        returnOrbit(Orbit.HEO)

        createViewModel()

        assertThat(viewModel.visible, `is`(false))
    }

    private suspend fun returnOrbit(orbit: Orbit) {
        whenever(getOrbit.getOrbit(any())).thenReturn(orbit)
    }

    private fun createViewModel() {
        viewModel = OrbitViewModel(getOrbit, 0)
    }

}