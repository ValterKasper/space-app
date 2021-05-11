package sk.kasper.space.tag

import sk.kasper.domain.model.Tag
import sk.kasper.ui_common.tag.TagMapper
import sk.kasper.ui_common.tag.UiTag
import javax.inject.Inject


class TagMapperImpl @Inject constructor() : TagMapper {

    override fun toUiTag(tagId: Long): UiTag {
        return when (tagId) {
            Tag.ISS -> UiTag.ISS
            Tag.MANNED -> UiTag.MANNED
            Tag.SATELLITE -> UiTag.SATELLITE
            Tag.TEST_FLIGHT -> UiTag.TEST_FLIGHT
            Tag.SECRET -> UiTag.SECRET
            Tag.CUBE_SAT -> UiTag.CUBE_SAT
            Tag.ROVER -> UiTag.ROVER
            Tag.MARS -> UiTag.MARS
            Tag.PROBE -> UiTag.PROBE
            else -> throw IllegalStateException("Unknown tag id $tagId")
        }
    }

    override fun toDomainTag(tag: UiTag): Long {
        return when (tag) {
            UiTag.ISS -> Tag.ISS
            UiTag.MANNED -> Tag.MANNED
            UiTag.SATELLITE -> Tag.SATELLITE
            UiTag.TEST_FLIGHT -> Tag.TEST_FLIGHT
            UiTag.SECRET -> Tag.SECRET
            UiTag.CUBE_SAT -> Tag.CUBE_SAT
            UiTag.ROVER -> Tag.ROVER
            UiTag.MARS -> Tag.MARS
            UiTag.PROBE -> Tag.PROBE
            else -> throw IllegalStateException("Unknown tag $tag")
        }
    }

}