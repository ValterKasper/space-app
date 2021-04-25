package sk.kasper.ui_timeline.filter

import androidx.annotation.StringRes

sealed class FilterItem {

    companion object {
        const val HEADER_TYPE = 0
        const val TAG_TYPE = 1
        const val ROCKET_TYPE = 2
    }

    abstract fun getType(): Int

    data class TagFilterItem(val tagType: Long, val selected: Boolean): FilterItem() {
        override fun getType() = TAG_TYPE
    }

    data class RocketFilterItem(val rocketId: Long, val selected: Boolean): FilterItem() {
        override fun getType() = ROCKET_TYPE
    }

    class HeaderFilterItem(@StringRes val stringRes: Int): FilterItem() {
        override fun getType() = HEADER_TYPE
    }

}