package sk.kasper.ui_launch.section

import androidx.annotation.StringRes
import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import sk.kasper.domain.model.Response
import sk.kasper.domain.usecase.GetOrbit
import sk.kasper.entity.Orbit
import sk.kasper.ui_common.view.OrbitToDraw
import sk.kasper.ui_launch.R
import javax.inject.Inject

data class OrbitState(
    val orbit: OrbitToDraw = OrbitToDraw.LEO,
    @StringRes
    val nameId: Int = R.string.geostationary_orbit,
    val visible: Boolean = false
)

@HiltViewModel
class OrbitViewModel @Inject constructor(
    private val getOrbit: GetOrbit,
    private val handle: SavedStateHandle,
) : LoaderViewModel<OrbitState, Orbit>(OrbitState()) {

    init {
        loadAction()
    }

    override suspend fun load(): Response<Orbit> {
        return getOrbit(handle.get("launchId")!!)
    }

    override fun mapLoadToState(load: Orbit, oldState: OrbitState): OrbitState {
        return when (load) {
            Orbit.LEO -> OrbitState(
                orbit = OrbitToDraw.LEO,
                nameId = R.string.low_earth_orbit,
                visible = true
            )
            Orbit.GEO -> OrbitState(
                orbit = OrbitToDraw.GEO,
                nameId = R.string.geostationary_orbit,
                visible = true
            )
            else -> OrbitState(visible = false)
        }
    }

    override fun mapErrorToState(message: String?, oldState: OrbitState): OrbitState {
        return oldState.copy(visible = false)
    }

}