package sk.kasper.ui_common.tag

import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import sk.kasper.ui_common.R

interface LaunchFilterItem : FilterItem

enum class FilterTag(@StringRes override val label: Int, @ColorRes override val color: Int) :
    LaunchFilterItem {
    ISS(R.string.tag_iss, R.color.tag_color_8),
    MANNED(R.string.tag_manned, R.color.tag_color_1),
    SATELLITE(R.string.tag_satellite, R.color.tag_color_7),
    TEST_FLIGHT(R.string.tag_test_flight, R.color.tag_color_4),
    SECRET(R.string.tag_secret, R.color.tag_color_5),
    CUBE_SAT(R.string.tag_cube_sat, R.color.tag_color_6),
    ROVER(R.string.tag_rover, R.color.tag_color_9),
    MARS(R.string.tag_mars, R.color.tag_color_2),
    PROBE(R.string.tag_probe, R.color.tag_color_3),
}

enum class FilterRocket(
    @StringRes override val label: Int,
    @ColorRes override val color: Int = R.color.dark_icon,
) : LaunchFilterItem {

    SOYUZ(R.string.rocket_soyuz),
    ARIANE_5(R.string.rocket_ariane_5),
    FALCON_9(R.string.rocket_falcon_9),
    DELTA_4_HEAVY(R.string.rocket_delta_VI_heavy),
    FALCON_HEAVY(R.string.rocket_falcon_heavy),
    ATLAS_5(R.string.rocket_atlas_V),

}