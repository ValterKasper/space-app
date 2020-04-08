package sk.kasper.space.launchdetail.section

import androidx.databinding.Bindable
import androidx.lifecycle.viewModelScope
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import kotlinx.coroutines.launch
import sk.kasper.domain.model.ErrorResponse
import sk.kasper.domain.model.SuccessResponse
import sk.kasper.domain.usecase.launchdetail.GetRocketForLaunch
import sk.kasper.space.BR
import sk.kasper.space.R

class RocketSectionViewModel @AssistedInject constructor(
        @Assisted launchId: Long,
        private val getRocketForLaunch: GetRocketForLaunch): SectionViewModel() {

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

    init {
        title = R.string.section_rocket

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

    @AssistedInject.Factory
    interface Factory {
        fun create(launchId: Long): RocketSectionViewModel
    }

}