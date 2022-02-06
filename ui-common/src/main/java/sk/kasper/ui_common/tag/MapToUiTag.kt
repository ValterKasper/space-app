package sk.kasper.ui_common.tag

fun interface MapToUiTag {
    operator fun invoke(tagId: Long): FilterTag
}