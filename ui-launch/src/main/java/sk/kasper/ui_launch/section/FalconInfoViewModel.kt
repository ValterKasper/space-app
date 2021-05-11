package sk.kasper.space.launchdetail.section

import androidx.annotation.StringRes
import androidx.databinding.Bindable
import androidx.lifecycle.viewModelScope
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import kotlinx.coroutines.launch
import sk.kasper.domain.model.ErrorResponse
import sk.kasper.domain.model.FalconCore
import sk.kasper.domain.model.SuccessResponse
import sk.kasper.domain.usecase.launchdetail.GetFalconCore
import sk.kasper.ui_common.utils.FormattedString
import sk.kasper.ui_common.utils.ObservableViewModel
import sk.kasper.ui_launch.R

class FalconInfoViewModel @AssistedInject constructor(
        @Assisted launchId: String,
        private val getFalconCore: GetFalconCore): ObservableViewModel() {

    @Bindable
    var visible = false

    @Bindable
    var blockVersionVisible = false

    @Bindable
    var blockStatusVisible = false

    @Bindable
    var landingTypeVisible = false

    @Bindable
    @StringRes
    var landingType = 0

    @Bindable
    var landingShipVisible = false

    @Bindable
    @StringRes
    var landingShip = 0

    @Bindable
    var landingZoneVisible = false

    @Bindable
    @StringRes
    var landingZone = 0

    @Bindable
    var blockVersion: FormattedString = FormattedString.empty()

    @Bindable
    var blockStatus: FormattedString = FormattedString.empty()

    init {
        viewModelScope.launch {
            getFalconCore.getFalconCore(launchId).also { response ->
                when (response) {
                    is SuccessResponse -> {
                        response.data.let {
                            visible = true
                            blockVersionVisible = true
                            blockVersion = FormattedString(R.string.block_x, it.block)
                            blockStatusVisible = true
                            blockStatus = if (it.flights > 1) {
                                FormattedString(R.string.x_flight, it.flights)
                            } else {
                                FormattedString.from(R.string.new_block)
                            }

                            when (it.landingType) {
                                FalconCore.LandingType.OCEAN -> {
                                    landingType = R.string.landing_type_ocean
                                    landingTypeVisible = true
                                }
                                FalconCore.LandingType.ASDS -> {
                                    landingType = R.string.landing_type_autonomous_spaceport_drone_ship
                                    landingTypeVisible = true
                                    when (it.landingVehicle) {
                                        FalconCore.LandingVehicle.OCISLY -> {
                                            landingShip = R.string.ASDS_of_course_i_still_love_you
                                            landingShipVisible = true
                                        }
                                        FalconCore.LandingVehicle.JRTI -> {
                                            landingShip = R.string.ASDS_just_read_the_instructions
                                            landingShipVisible = true
                                        }
                                        else -> {
                                        }
                                    }
                                }
                                FalconCore.LandingType.RTLS -> {
                                    landingType = R.string.landing_type_return_to_launch_site
                                    landingTypeVisible = true
                                    when (it.landingVehicle) {
                                        FalconCore.LandingVehicle.LZ_1 -> {
                                            landingZone = R.string.landing_zone_LZ1
                                            landingZoneVisible = true
                                        }
                                        FalconCore.LandingVehicle.LZ_2 -> {
                                            landingZone = R.string.landing_zone_LZ2
                                            landingZoneVisible = true
                                        }
                                        else -> {
                                        }
                                    }
                                }
                                else -> {
                                }
                            }
                        }
                    }
                    is ErrorResponse -> {
                        // does not have core
                    }
                }

            }
        }
    }

    @AssistedInject.Factory
    interface Factory {
        fun create(launchId: String): FalconInfoViewModel
    }

}