package sk.kasper.space.timeline.filter

import android.view.View
import androidx.databinding.ObservableInt
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import sk.kasper.domain.model.FilterSpec
import sk.kasper.space.R

class TimelineFilterViewModel(private val timelineFilterSpecModel: TimelineFilterSpecModel)
    : ViewModel(), FilterSelectionListener {

    val filterItems: MutableLiveData<List<FilterItem>> = MutableLiveData()

    val clearButtonVisibility: ObservableInt = ObservableInt(View.GONE)

    init {
        val actualFilterSpec = timelineFilterSpecModel.value
        filterItems.value = if (actualFilterSpec.filterNotEmpty()) {
            clearButtonVisibility.set(View.VISIBLE)
            emptyList<FilterItem>()
                    .plus(FilterItem.HeaderFilterItem(R.string.title_tags))
                    .plus(FilterSpec.ALL_TAGS.map { FilterItem.TagFilterItem(it, actualFilterSpec.tagTypes.contains(it)) })
                    .plus(FilterItem.HeaderFilterItem(R.string.title_rockets))
                    .plus(FilterSpec.ALL_ROCKETS.map { FilterItem.RocketFilterItem(it, actualFilterSpec.rockets.contains(it)) })
        } else {
            clearButtonVisibility.set(View.GONE)
            createUnselectedFilterItems()
        }
    }

    fun onClearAllClick() {
        timelineFilterSpecModel.value = FilterSpec.EMPTY_FILTER
        filterItems.value = createUnselectedFilterItems()
        clearButtonVisibility.set(View.GONE)
    }

    override fun onTagFilterItemChanged(tagFilterItem: FilterItem.TagFilterItem) {
        val tagTypes: Set<Long> = createNewTagTypes(tagFilterItem)
        timelineFilterSpecModel.value = timelineFilterSpecModel.value.copy(tagTypes = tagTypes)
        updateClearButtonVisibility()
    }

    override fun onRocketFilterItemChanged(rocketFilterItem: FilterItem.RocketFilterItem) {
        val rockets: Set<Long> = createNewRockets(rocketFilterItem)
        timelineFilterSpecModel.value = timelineFilterSpecModel.value.copy(rockets = rockets)
        updateClearButtonVisibility()
    }

    private fun updateClearButtonVisibility() {
        clearButtonVisibility.set(if (timelineFilterSpecModel.value.filterNotEmpty()) {
            View.VISIBLE
        } else {
            View.GONE
        })
    }

    private fun createNewTagTypes(tagFilterItem: FilterItem.TagFilterItem): Set<Long> =
            if (tagFilterItem.selected) {
                timelineFilterSpecModel.value.tagTypes.plus(tagFilterItem.tagType)
            } else {
                timelineFilterSpecModel.value.tagTypes.minus(tagFilterItem.tagType)
            }

    private fun createNewRockets(rocketFilterItem: FilterItem.RocketFilterItem): Set<Long> =
            if (rocketFilterItem.selected) {
                timelineFilterSpecModel.value.rockets.plus(rocketFilterItem.rocketId)
            } else {
                timelineFilterSpecModel.value.rockets.minus(rocketFilterItem.rocketId)
            }

    private fun createUnselectedFilterTagItems() =
            FilterSpec.ALL_TAGS.map { FilterItem.TagFilterItem(it, false) }

    private fun createUnselectedFilterRocketItems() =
            FilterSpec.ALL_ROCKETS.map { FilterItem.RocketFilterItem(it, false) }

    private fun createUnselectedFilterItems(): List<FilterItem> {
        return emptyList<FilterItem>()
                .plus(FilterItem.HeaderFilterItem(R.string.title_tags))
                .plus(createUnselectedFilterTagItems())
                .plus(FilterItem.HeaderFilterItem(R.string.title_rockets))
                .plus(createUnselectedFilterRocketItems())
    }

}