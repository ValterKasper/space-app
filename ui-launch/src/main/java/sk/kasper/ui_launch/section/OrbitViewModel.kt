package sk.kasper.ui_launch.section

import androidx.annotation.StringRes
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import sk.kasper.domain.model.Orbit
import sk.kasper.domain.usecase.launchdetail.GetOrbit
import sk.kasper.ui_common.view.OrbitToDraw
import sk.kasper.ui_common.viewmodel.ReducerViewModel
import sk.kasper.ui_launch.R
import timber.log.Timber

data class OrbitState(
    val orbit: OrbitToDraw = OrbitToDraw.LEO,
    @StringRes
    val nameId: Int = R.string.geostationary_orbit,
    val visible: Boolean = false
)

sealed class OrbitAction {
    object Init : OrbitAction()
    data class ShowOrbit(val orbit: Orbit) : OrbitAction()
}

class OrbitViewModel @AssistedInject constructor(
    private val getOrbit: GetOrbit,
    @Assisted private val launchId: String
) : ReducerViewModel<OrbitState, OrbitAction, Unit>(OrbitState()) {

    init {
        submitAction(OrbitAction.Init)
    }

    override fun mapActionToActionFlow(action: OrbitAction): Flow<OrbitAction> {
        return if (action == OrbitAction.Init) {
            flow {
                try {
                    emit(OrbitAction.ShowOrbit(getOrbit.getOrbit(launchId = launchId)))
                } catch (e: Exception) {
                    Timber.e(e)
                }
            }
        } else {
            super.mapActionToActionFlow(action)
        }
    }

    override fun ScanScope.scan(action: OrbitAction, oldState: OrbitState): OrbitState {
        return when (action) {
            is OrbitAction.ShowOrbit -> {
                when (action.orbit) {
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
            else -> oldState
        }
    }

    @AssistedInject.Factory
    interface Factory {
        fun create(launchId: String): OrbitViewModel
    }

}