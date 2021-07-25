package sk.kasper.ui_timeline.filter

import sk.kasper.ui_common.tag.FilterTag

sealed class FilterItem {

    companion object {
        const val HEADER_TYPE = 0
        const val TAG_TYPE = 1
        const val ROCKET_TYPE = 2
    }

    abstract fun getType(): Int

    data class TagFilterItem(val tag: FilterTag, val selected: Boolean) : FilterItem() {
        override fun getType() = TAG_TYPE
    }

    data class RocketFilterItem(val rocketId: Long, val selected: Boolean): FilterItem() {
        override fun getType() = ROCKET_TYPE
    }

}