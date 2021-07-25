package sk.kasper.ui_common.tag

interface TagMapper {
    fun toUiTag(tagId: Long): FilterTag
    fun toDomainTag(tag: FilterTag): Long
}