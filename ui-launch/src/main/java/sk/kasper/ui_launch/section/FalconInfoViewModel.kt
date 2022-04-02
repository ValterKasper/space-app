package sk.kasper.ui_launch.section

import androidx.annotation.StringRes
import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import sk.kasper.domain.model.Response
import sk.kasper.domain.usecase.GetFalconCore
import sk.kasper.entity.FalconCore
import sk.kasper.ui_common.utils.FormattedString
import sk.kasper.ui_launch.R
import javax.inject.Inject

data class FalconInfoState(
    val visible: Boolean = false,
    val blockVersionVisible: Boolean = false,
    val blockStatusVisible: Boolean = false,
    val landingTypeVisible: Boolean = false,
    @StringRes
    val landingType: Int = 0,
    val landingShipVisible: Boolean = false,
    @StringRes
    val landingShip: Int = 0,
    val landingZoneVisible: Boolean = false,
    @StringRes
    val landingZone: Int = 0,
    val blockVersion: FormattedString = FormattedString.empty(),
    val blockStatus: FormattedString = FormattedString.empty(),
)

@HiltViewModel
class FalconInfoViewModel @Inject constructor(
    private val handle: SavedStateHandle,
    private val getFalconCore: GetFalconCore
) : LoaderViewModel<FalconInfoState, FalconCore>(
    FalconInfoState()
) {

    init {
        loadAction()
    }

    override suspend fun load(): Response<FalconCore> = getFalconCore(handle.get("launchId")!!)

    override fun mapLoadToState(load: FalconCore, oldState: FalconInfoState): FalconInfoState {
        var landingTypeVisible = false

        @StringRes
        var landingType = 0
        var landingShipVisible = false

        @StringRes
        var landingShip = 0
        var landingZoneVisible = false

        @StringRes
        var landingZone = 0

        when (load.landingType) {
            FalconCore.LandingType.OCEAN -> {
                landingType = R.string.landing_type_ocean
                landingTypeVisible = true
            }
            FalconCore.LandingType.ASDS -> {
                landingType = R.string.landing_type_autonomous_spaceport_drone_ship
                landingTypeVisible = true
                when (load.landingVehicle) {
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
                when (load.landingVehicle) {
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

        val blockStatus = if (load.flights > 1) {
            FormattedString(R.string.x_flight, load.flights)
        } else {
            FormattedString.from(R.string.new_block)
        }
        return FalconInfoState(
            visible = true,
            blockVersionVisible = true,
            blockVersion = FormattedString(R.string.block_x, load.block),
            blockStatusVisible = true,
            blockStatus = blockStatus,
            landingTypeVisible = landingTypeVisible,
            landingType = landingType,
            landingShipVisible = landingShipVisible,
            landingShip = landingShip,
            landingZoneVisible = landingZoneVisible,
            landingZone = landingZone
        )
    }

    override fun mapErrorToState(message: String?, oldState: FalconInfoState): FalconInfoState {
        return oldState.copy(visible = false)
    }

}