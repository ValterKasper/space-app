package sk.kasper.ui_timeline.filter

interface FilterSelectionListener {
    fun onTagFilterItemChanged(tagFilterItem: FilterItem.TagFilterItem)
    fun onRocketFilterItemChanged(rocketFilterItem: FilterItem.RocketFilterItem)
}