package sk.kasper.ui_launch.section

import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import sk.kasper.domain.model.Response
import sk.kasper.domain.usecase.GetRocketForLaunch
import sk.kasper.entity.Rocket
import sk.kasper.ui_launch.R
import javax.inject.Inject

data class RocketSectionState(
    val rocketName: String = "",
    val height: String = "",
    val diameter: String = "",
    val mass: String = "",
    val payloadLeo: String = "",
    val payloadGto: String = "",
    val thrust: String = "",
    val stages: String = "",
    val title: Int = R.string.section_rocket,
    val visible: Boolean = false
)

@HiltViewModel
class RocketSectionViewModel @Inject constructor(
    private val handle: SavedStateHandle,
    private val getRocketForLaunch: GetRocketForLaunch
) : LoaderViewModel<RocketSectionState, Rocket>(RocketSectionState()) {

    init {
        loadAction()
    }

    override suspend fun load(): Response<Rocket> {
        return getRocketForLaunch(handle.get("launchId")!!)
    }

    override fun mapLoadToState(load: Rocket, oldState: RocketSectionState): RocketSectionState {
        return RocketSectionState(
            rocketName = load.rocketName,
            height = "${load.height} m",
            diameter = "${load.diameter} m",
            mass = "${load.mass} kg",
            payloadLeo = "${load.payloadLeo} kg",
            payloadGto = "${load.payloadGto} kg",
            thrust = "${load.thrust} kN",
            stages = "${load.stages}",
            visible = true
        )
    }

    override fun mapErrorToState(
        message: String?,
        oldState: RocketSectionState
    ): RocketSectionState {
        return oldState.copy(visible = false)
    }

}