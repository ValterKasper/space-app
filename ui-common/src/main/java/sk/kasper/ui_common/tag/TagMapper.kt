package sk.kasper.ui_common.tag

interface TagMapper {
    fun toUiTag(tagId: Long): UiTag
    fun toDomainTag(tag: UiTag): Long
}