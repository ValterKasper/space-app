package sk.kasper.space.launchdetail.section

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import sk.kasper.domain.model.ErrorResponse
import sk.kasper.domain.model.FalconCore
import sk.kasper.domain.model.SuccessResponse
import sk.kasper.domain.usecase.launchdetail.GetFalconCore
import sk.kasper.space.R
import sk.kasper.space.utils.CoroutinesMainDispatcherRule
import sk.kasper.space.utils.FormattedString

@RunWith(MockitoJUnitRunner::class)
class FalconInfoViewModelTest {

    private lateinit var viewModel: FalconInfoViewModel

    @Mock
    private lateinit var getFalconCore: GetFalconCore

    @get:Rule
    @ExperimentalCoroutinesApi
    var coroutinesMainDispatcherRule = CoroutinesMainDispatcherRule()

    private fun createViewModel() {
        viewModel = FalconInfoViewModel(0, getFalconCore)
    }

    @Test
    fun noFalconCoreInfo_falconCoreInfoSectionInvisible() = runBlocking {
        whenever(getFalconCore.getFalconCore(any())).thenReturn(ErrorResponse(null))
        createViewModel()

        MatcherAssert.assertThat(viewModel.visible, CoreMatchers.`is`(false))
        MatcherAssert.assertThat(viewModel.blockVersionVisible, CoreMatchers.`is`(false))
        MatcherAssert.assertThat(viewModel.blockStatusVisible, CoreMatchers.`is`(false))
        MatcherAssert.assertThat(viewModel.landingTypeVisible, CoreMatchers.`is`(false))
        MatcherAssert.assertThat(viewModel.landingShipVisible, CoreMatchers.`is`(false))
        MatcherAssert.assertThat(viewModel.landingZoneVisible, CoreMatchers.`is`(false))
    }

    @Test
    fun falconCoreInfo_showVersionAndStatus() = runBlocking {
        whenever(getFalconCore.getFalconCore(any())).thenReturn(SuccessResponse(FalconCore(
                false, 4, 1, FalconCore.LandingType.UNKNOWN, FalconCore.LandingVehicle.UNKNOWN
        )))
        createViewModel()

        MatcherAssert.assertThat(viewModel.visible, CoreMatchers.`is`(true))
        MatcherAssert.assertThat(viewModel.blockVersionVisible, CoreMatchers.`is`(true))
        MatcherAssert.assertThat(viewModel.blockStatusVisible, CoreMatchers.`is`(true))
        MatcherAssert.assertThat(viewModel.blockVersion, CoreMatchers.`is`(FormattedString(R.string.block_x, 4)))
        MatcherAssert.assertThat(viewModel.blockStatus, CoreMatchers.`is`(FormattedString.from(R.string.new_block)))
    }

    @Test
    fun falconCoreInfo_reused_showCountOfFlights() = runBlocking {
        whenever(getFalconCore.getFalconCore(any())).thenReturn(SuccessResponse(FalconCore(
                true, 4, 2, FalconCore.LandingType.UNKNOWN, FalconCore.LandingVehicle.UNKNOWN
        )))
        createViewModel()

        MatcherAssert.assertThat(viewModel.blockStatusVisible, CoreMatchers.`is`(true))
        MatcherAssert.assertThat(viewModel.blockStatus, CoreMatchers.`is`(FormattedString(R.string.x_flight, 2)))
    }

    @Test
    fun falconCoreInfo_landingTypeOcean_showJustLandingType() = runBlocking {
        whenever(getFalconCore.getFalconCore(any())).thenReturn(SuccessResponse(FalconCore(
                false, 4, 1, FalconCore.LandingType.OCEAN, FalconCore.LandingVehicle.UNKNOWN
        )))
        createViewModel()

        MatcherAssert.assertThat(viewModel.landingTypeVisible, CoreMatchers.`is`(true))
        MatcherAssert.assertThat(viewModel.landingType, CoreMatchers.`is`(R.string.landing_type_ocean))
        MatcherAssert.assertThat(viewModel.landingShipVisible, CoreMatchers.`is`(false))
        MatcherAssert.assertThat(viewModel.landingZoneVisible, CoreMatchers.`is`(false))
    }

    @Test
    fun falconCoreInfo_landingTypeUnknown_donNotShowLanding() = runBlocking {
        whenever(getFalconCore.getFalconCore(any())).thenReturn(SuccessResponse(FalconCore(
                false, 4, 1, FalconCore.LandingType.UNKNOWN, FalconCore.LandingVehicle.UNKNOWN
        )))
        createViewModel()

        MatcherAssert.assertThat(viewModel.landingTypeVisible, CoreMatchers.`is`(false))
        MatcherAssert.assertThat(viewModel.landingShipVisible, CoreMatchers.`is`(false))
        MatcherAssert.assertThat(viewModel.landingZoneVisible, CoreMatchers.`is`(false))
    }

    @Test
    fun falconCoreInfo_landingTypeASDS_showShip() = runBlocking {
        whenever(getFalconCore.getFalconCore(any())).thenReturn(SuccessResponse(FalconCore(
                false, 4, 1, FalconCore.LandingType.ASDS, FalconCore.LandingVehicle.OCISLY
        )))
        createViewModel()

        MatcherAssert.assertThat(viewModel.landingTypeVisible, CoreMatchers.`is`(true))
        MatcherAssert.assertThat(viewModel.landingType, CoreMatchers.`is`(R.string.landing_type_autonomous_spaceport_drone_ship))
        MatcherAssert.assertThat(viewModel.landingShipVisible, CoreMatchers.`is`(true))
        MatcherAssert.assertThat(viewModel.landingShip, CoreMatchers.`is`(R.string.ASDS_of_course_i_still_love_you))
        MatcherAssert.assertThat(viewModel.landingZoneVisible, CoreMatchers.`is`(false))
    }

    @Test
    fun falconCoreInfo_landingTypeRTLS_showLandingZone() = runBlocking {
        whenever(getFalconCore.getFalconCore(any())).thenReturn(SuccessResponse(FalconCore(
                false, 4, 1, FalconCore.LandingType.RTLS, FalconCore.LandingVehicle.LZ_1
        )))
        createViewModel()

        MatcherAssert.assertThat(viewModel.landingTypeVisible, CoreMatchers.`is`(true))
        MatcherAssert.assertThat(viewModel.landingType, CoreMatchers.`is`(R.string.landing_type_return_to_launch_site))
        MatcherAssert.assertThat(viewModel.landingShipVisible, CoreMatchers.`is`(false))
        MatcherAssert.assertThat(viewModel.landingZoneVisible, CoreMatchers.`is`(true))
        MatcherAssert.assertThat(viewModel.landingZone, CoreMatchers.`is`(R.string.landing_zone_LZ1))
    }
}