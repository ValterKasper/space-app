package sk.kasper.ui_common.mapper

import sk.kasper.entity.Tag
import sk.kasper.ui_common.tag.FilterTag
import javax.inject.Inject

class MapToDomainTag @Inject constructor() {

    operator fun invoke(tag: FilterTag): Long {
        return when (tag) {
            FilterTag.ISS -> Tag.ISS
            FilterTag.MANNED -> Tag.MANNED
            FilterTag.SATELLITE -> Tag.SATELLITE
            FilterTag.TEST_FLIGHT -> Tag.TEST_FLIGHT
            FilterTag.SECRET -> Tag.SECRET
            FilterTag.CUBE_SAT -> Tag.CUBE_SAT
            FilterTag.ROVER -> Tag.ROVER
            FilterTag.MARS -> Tag.MARS
            FilterTag.PROBE -> Tag.PROBE
        }
    }

}