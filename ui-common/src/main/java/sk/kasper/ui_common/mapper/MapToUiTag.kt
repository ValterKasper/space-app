package sk.kasper.ui_common.mapper

import sk.kasper.entity.Tag
import sk.kasper.ui_common.tag.FilterTag
import javax.inject.Inject

class MapToUiTag @Inject constructor() {

    operator fun invoke(tagId: Long): FilterTag {
        return when (tagId) {
            Tag.ISS -> FilterTag.ISS
            Tag.MANNED -> FilterTag.MANNED
            Tag.SATELLITE -> FilterTag.SATELLITE
            Tag.TEST_FLIGHT -> FilterTag.TEST_FLIGHT
            Tag.SECRET -> FilterTag.SECRET
            Tag.CUBE_SAT -> FilterTag.CUBE_SAT
            Tag.ROVER -> FilterTag.ROVER
            Tag.MARS -> FilterTag.MARS
            Tag.PROBE -> FilterTag.PROBE
            else -> throw IllegalStateException("Unknown tag id $tagId")
        }
    }

}