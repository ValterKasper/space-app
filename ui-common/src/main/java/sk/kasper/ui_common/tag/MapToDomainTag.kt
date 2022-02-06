package sk.kasper.ui_common.tag

fun interface MapToDomainTag {
    operator fun invoke(tag: FilterTag): Long
}