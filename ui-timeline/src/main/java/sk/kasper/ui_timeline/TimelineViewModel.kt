package sk.kasper.ui_timeline

import org.threeten.bp.LocalDateTime
import org.threeten.bp.LocalTime
import sk.kasper.domain.model.ErrorResponse
import sk.kasper.domain.model.FilterSpec
import sk.kasper.domain.model.Launch
import sk.kasper.domain.model.SuccessResponse
import sk.kasper.domain.usecase.timeline.GetTimelineItems
import sk.kasper.domain.usecase.timeline.RefreshTimelineItems
import sk.kasper.ui_common.rocket.RocketMapper
import sk.kasper.ui_common.settings.SettingsManager
import sk.kasper.ui_common.tag.TagMapper
import sk.kasper.ui_common.viewmodel.ReducerViewModel2
import sk.kasper.ui_timeline.filter.FilterItem
import sk.kasper.ui_timeline.filter.FilterSelectionListener
import javax.inject.Inject


data class TimelineState(
    val showNoMatchingLaunches: Boolean = false,
    val showRetryToLoadLaunches: Boolean = false,
    val clearButtonVisible: Boolean = false,
    val filterSpec: FilterSpec = FilterSpec.EMPTY_FILTER,
    val filterItems: List<FilterItem> = emptyList(),
    val timelineItems: List<TimelineListItem> = emptyList(),
    val progressVisible: Boolean = false
) {
    override fun toString(): String {
        val count = filterItems
            .filterIsInstance<FilterItem.TagFilterItem>()
            .count(FilterItem.TagFilterItem::selected)
        return "TIs = ${timelineItems.size}, filterSpec = ${filterSpec}, clearButtonVisibility = ${clearButtonVisible}, progress = ${progressVisible}, selected = $count"
    }
}

enum class Destination {
    COMPOSE_PLAYGROUND,
    SETTINGS
}

sealed class SideEffect {
    object ConnectionError : SideEffect()
    data class NavigateTo(val uriString: String) : SideEffect()
    object ShowFilter : SideEffect()
}

open class TimelineViewModel @Inject constructor(
    private val getTimelineItems: GetTimelineItems,
    private val refreshTimelineItems: RefreshTimelineItems,
    private val settingsManager: SettingsManager,
    private val tagMapper: TagMapper,
    private val rocketMapper: RocketMapper,
) : ReducerViewModel2<TimelineState, SideEffect>(TimelineState()),
    FilterSelectionListener {

    init {
        init()
    }

    private fun init() = action {
        reduce {
            copy(filterItems = createUnselectedFilterItems())
        }
        reloadTimelineItems(filterSpec = FilterSpec.EMPTY_FILTER)
    }

    fun onFilterBarClick() = action {
        emitSideEffect(SideEffect.ShowFilter)
    }

    fun onRefresh() = action {
        reduce {
            copy(progressVisible = true)
        }

        doSync()
        reloadTimelineItems(snapshot().filterSpec)

        reduce {
            copy(progressVisible = false)
        }
    }

    fun onItemClick(item: LaunchListItem) = action {
        emitSideEffect(SideEffect.NavigateTo("spaceapp://launch/${item.id}"))
    }

    fun onClearAllClick() = action {
        reloadTimelineItems(FilterSpec.EMPTY_FILTER)
        reduce {
            copy(
                filterSpec = FilterSpec.EMPTY_FILTER,
                clearButtonVisible = false,
                filterItems = createFilterItemsFromFilterSpec(FilterSpec.EMPTY_FILTER)
            )
        }
    }

    fun navigateClick(destination: Destination) = action {
        val path = when (destination) {
            Destination.COMPOSE_PLAYGROUND -> "compose_playground"
            Destination.SETTINGS -> "settings"
        }
        emitSideEffect(SideEffect.NavigateTo("spaceapp://$path"))
    }

    override fun onTagFilterItemChanged(tagFilterItem: FilterItem.TagFilterItem) = action {
        val oldState = snapshot()
        val newState = handleTagFilterItemChanged(oldState, tagFilterItem)
        reloadTimelineItems(newState.filterSpec)
    }

    override fun onRocketFilterItemChanged(rocketFilterItem: FilterItem.RocketFilterItem) = action {
        val oldState = snapshot()
        val newState = handleRocketFilterItemChanged(oldState, rocketFilterItem)
        reloadTimelineItems(newState.filterSpec)
    }

    private fun handleTagFilterItemChanged(
        oldState: TimelineState,
        tagFilterItem: FilterItem.TagFilterItem
    ): TimelineState {
        val tagTypes = if (tagFilterItem.selected) {
            oldState.filterSpec.tagTypes.plus(tagMapper.toDomainTag(tagFilterItem.tag))
        } else {
            oldState.filterSpec.tagTypes.minus(tagMapper.toDomainTag(tagFilterItem.tag))
        }

        val filterSpec = oldState.filterSpec.copy(tagTypes = tagTypes)

        return oldState.copy(
            filterSpec = filterSpec,
            progressVisible = true,
            clearButtonVisible = filterSpec.filterNotEmpty(),
            filterItems = createFilterItemsFromFilterSpec(filterSpec)
        )
    }

    private fun handleRocketFilterItemChanged(
        oldState: TimelineState,
        rocketFilterItem: FilterItem.RocketFilterItem
    ): TimelineState {
        val rocketTypes = if (rocketFilterItem.selected) {
            oldState.filterSpec.rockets.plus(rocketFilterItem.rocketId)
        } else {
            oldState.filterSpec.rockets.minus(rocketFilterItem.rocketId)
        }

        val filterSpec = oldState.filterSpec.copy(rockets = rocketTypes)

        return oldState.copy(
            filterSpec = filterSpec,
            progressVisible = true,
            clearButtonVisible = filterSpec.filterNotEmpty(),
            filterItems = createFilterItemsFromFilterSpec(filterSpec)
        )
    }

    private fun createFilterItemsFromFilterSpec(actualFilterSpec: FilterSpec): List<FilterItem> {
        return emptyList<FilterItem>()
            .plus(FilterItem.HeaderFilterItem(R.string.title_tags))
            .plus(FilterSpec.ALL_TAGS.map {
                FilterItem.TagFilterItem(
                    tagMapper.toUiTag(it),
                    actualFilterSpec.tagTypes.contains(it)
                )
            })
            .plus(FilterItem.HeaderFilterItem(R.string.title_rockets))
            .plus(FilterSpec.ALL_ROCKETS.map {
                FilterItem.RocketFilterItem(
                    it,
                    actualFilterSpec.rockets.contains(it)
                )
            })
    }

    private suspend fun loadTimeline(filterSpec: FilterSpec): List<Launch> {
        return when (val response = getTimelineItems.getTimelineItems(filterSpec)) {
            is SuccessResponse -> {
                return response.data
            }
            is ErrorResponse -> {
                emitSideEffect(SideEffect.ConnectionError)
                emptyList()
            }
        }
    }

    private fun mapToTimeListItem(list: List<Launch>): List<TimelineListItem> {
        val items: MutableList<TimelineListItem> = mutableListOf()
        val currentDateTime = getCurrentDateTime()
        val todayStartDateTime =
            LocalDateTime.of(currentDateTime.toLocalDate(), LocalTime.MIDNIGHT) // start of this day
        val todayEndDateTime = todayStartDateTime.plusDays(1) // end of this day
        val tomorrowEndDateTime = todayEndDateTime.plusDays(1) // end of next day
        val weekLaterDateTime = todayEndDateTime.plusDays(6)
        val showUnconfirmedLaunches = settingsManager.showUnconfirmedLaunches

        list
            .filter { showUnconfirmedLaunches || it.accurateDate }
            .filter { it.launchDateTime.isAfter(todayStartDateTime) }
            .map { LaunchListItem.fromLaunch(it, tagMapper, rocketMapper) }
            .groupBy {
                when {
                    !it.accurateDate -> LabelListItem.Month(it.launchDateTime.monthValue)
                    it.launchDateTime.isBefore(todayEndDateTime) -> LabelListItem.Today
                    it.launchDateTime.isBefore(tomorrowEndDateTime) -> LabelListItem.Tomorrow
                    it.launchDateTime.isBefore(weekLaterDateTime) -> LabelListItem.ThisWeek
                    else -> LabelListItem.Month(it.launchDateTime.monthValue)
                }
            }
            .toSortedMap(labelListItemsComparator())
            .forEach {
                items.add(it.key)
                items.addAll(it.value.sortedWith(launchListItemsComparator()))
            }

        return items
    }

    private fun labelListItemsComparator() = Comparator { o1: LabelListItem, o2: LabelListItem ->
        o1.compareTo(o2)
    }

    // accurate items go first
    private fun launchListItemsComparator(): java.util.Comparator<LaunchListItem> {
        return Comparator { o1: LaunchListItem, o2: LaunchListItem ->
            if (o1.accurateDate xor o2.accurateDate) { // accuracy values are different
                if (o1.accurateDate) -1 else 1
            } else {
                o1.launchDateTime.compareTo(o2.launchDateTime)
            }
        }
    }

    open fun getCurrentDateTime(): LocalDateTime = LocalDateTime.now()

    private suspend fun doSync() {
        when (refreshTimelineItems.refresh()) {
            is SuccessResponse -> {
            }
            is ErrorResponse -> emitSideEffect(SideEffect.ConnectionError)
        }
    }

    private suspend fun reloadTimelineItems(filterSpec: FilterSpec) {
        val list = loadTimeline(filterSpec)
        reduce {
            copy(
                showNoMatchingLaunches = list.isEmpty() && filterSpec.filterNotEmpty(),
                showRetryToLoadLaunches = list.isEmpty() && !filterSpec.filterNotEmpty(),
                timelineItems = mapToTimeListItem(list),
                progressVisible = false
            )
        }
    }

    private fun createUnselectedFilterTagItems() =
        FilterSpec.ALL_TAGS.map { FilterItem.TagFilterItem(tagMapper.toUiTag(it), false) }

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
