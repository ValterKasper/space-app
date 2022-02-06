package sk.kasper.space.mapper

import sk.kasper.domain.model.Tag
import sk.kasper.ui_common.tag.FilterTag
import sk.kasper.ui_common.tag.MapToDomainTag
import sk.kasper.ui_common.tag.MapToUiTag
import javax.inject.Inject


class TagMapperImpl @Inject constructor() : MapToUiTag, MapToDomainTag {

    override fun invoke(tagId: Long): FilterTag {
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

    override fun invoke(tag: FilterTag): Long {
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
            else -> throw IllegalStateException("Unknown tag $tag")
        }
    }

}