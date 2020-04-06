package sk.kasper.space.launchdetail.section

import androidx.databinding.Bindable
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import sk.kasper.domain.model.ErrorResponse
import sk.kasper.domain.model.SuccessResponse
import sk.kasper.domain.usecase.launchdetail.GetRocketForLaunch
import sk.kasper.space.BR
import sk.kasper.space.R
import javax.inject.Inject

class RocketSectionViewModel @Inject constructor(
        private val getRocketForLaunch: GetRocketForLaunch): SectionViewModel() {

    init {
        title = R.string.section_rocket
    }

    @get:Bindable
    var rocketName = ""

    @get:Bindable
    var height = ""

    @get:Bindable
    var diameter = ""

    @get:Bindable
    var mass = ""

    @get:Bindable
    var payloadLeo = ""

    @get:Bindable
    var payloadGto = ""

    @get:Bindable
    var thrust = ""

    @get:Bindable
    var stages = ""

    var launchId: Long = 0L
        set(value) {
            if (field == 0L) {
                field = value

                viewModelScope.launch {
                    getRocketForLaunch.getRocket(launchId).also {
                        when (it) {
                            is SuccessResponse -> {
                                rocketName = it.data.rocketName
                                height = "${it.data.height} m"
                                diameter = "${it.data.diameter} m"
                                mass = "${it.data.mass} kg"
                                payloadLeo = "${it.data.payloadLeo} kg"
                                payloadGto = "${it.data.payloadGto} kg"
                                thrust = "${it.data.thrust} kN"
                                stages = "${it.data.stages}"
                                notifyChange()
                            }
                            is ErrorResponse -> {
                                visible = false
                                notifyPropertyChanged(BR.visible)
                            }
                        }

                    }
                }
            }
        }

}