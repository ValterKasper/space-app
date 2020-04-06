package sk.kasper.space.timeline.filter

interface FilterSelectionListener {
    fun onTagFilterItemChanged(tagFilterItem: FilterItem.TagFilterItem)
    fun onRocketFilterItemChanged(rocketFilterItem: FilterItem.RocketFilterItem)
}