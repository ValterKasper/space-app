package sk.kasper.ui_timeline

import sk.kasper.domain.model.Tag
import sk.kasper.ui_common.R

open class TagViewModel(tagType: Long) {

    val label = when (tagType) {
        Tag.MANNED -> R.string.tag_manned
        Tag.SATELLITE -> R.string.tag_satellite
        Tag.ISS -> R.string.tag_iss
        Tag.TEST_FLIGHT -> R.string.tag_test_flight
        Tag.CUBE_SAT -> R.string.tag_cube_sat
        Tag.SECRET -> R.string.tag_secret
        Tag.PROBE -> R.string.tag_probe
        Tag.MARS -> R.string.tag_mars
        Tag.ROVER -> R.string.tag_rover
        else -> R.string.tag_foo
    }

    val color = when (tagType) {
        Tag.MANNED -> R.color.tag_color_1
        Tag.SATELLITE -> R.color.tag_color_7
        Tag.ISS -> R.color.tag_color_8
        Tag.TEST_FLIGHT -> R.color.tag_color_4
        Tag.CUBE_SAT -> R.color.tag_color_5
        Tag.SECRET -> R.color.tag_color_6
        Tag.MARS -> R.color.tag_color_2
        Tag.PROBE -> R.color.tag_color_3
        Tag.ROVER -> R.color.tag_color_9
        else -> R.color.tag_color_1
    }

}