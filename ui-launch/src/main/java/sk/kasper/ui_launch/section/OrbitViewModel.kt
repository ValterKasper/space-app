package sk.kasper.ui_launch.section

import androidx.annotation.StringRes
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import sk.kasper.domain.model.Orbit
import sk.kasper.domain.model.Response
import sk.kasper.domain.usecase.launchdetail.GetOrbit
import sk.kasper.ui_common.view.OrbitToDraw
import sk.kasper.ui_launch.R

data class OrbitState(
    val orbit: OrbitToDraw = OrbitToDraw.LEO,
    @StringRes
    val nameId: Int = R.string.geostationary_orbit,
    val visible: Boolean = false
)

class OrbitViewModel @AssistedInject constructor(
    private val getOrbit: GetOrbit,
    @Assisted private val launchId: String
) : LoaderViewModel<OrbitState, Orbit>(OrbitState()) {

    init {
        loadAction()
    }

    override suspend fun load(): Response<Orbit> {
        return getOrbit.getOrbit(launchId = launchId)
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

    @AssistedInject.Factory
    interface Factory {
        fun create(launchId: String): OrbitViewModel
    }

}