package sk.kasper.ui_timeline

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDateTime
import org.threeten.bp.LocalTime
import sk.kasper.domain.model.ErrorResponse
import sk.kasper.domain.model.FilterSpec
import sk.kasper.domain.model.Launch
import sk.kasper.domain.model.SuccessResponse
import sk.kasper.domain.usecase.timeline.GetTimelineItems
import sk.kasper.domain.usecase.timeline.RefreshTimelineItems
import sk.kasper.ui_common.settings.SettingsManager
import sk.kasper.ui_common.tag.TagMapper
import sk.kasper.ui_common.viewmodel.ReducerViewModel
import sk.kasper.ui_timeline.TimelineAction.*
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
    UI_TOOLKIT_PLAYGROUND,
    COMPOSE_PLAYGROUND,
    SETTINGS
}

sealed class TimelineAction {
    object Init : TimelineAction()
    object FilterBarClickAction : TimelineAction()
    object ErrorAction : TimelineAction()
    object ClearAllClickAction : TimelineAction()
    data class NavigateClickAction(val destination: Destination) : TimelineAction()
    data class RefreshAction(val force: Boolean) : TimelineAction()
    data class LaunchesLoaded(val launches: List<Launch>) : TimelineAction()
    data class LoadLaunches(val filterSpec: FilterSpec, val force: Boolean) : TimelineAction()
    class TagFilterItemChangedAction(val tagFilterItem: FilterItem.TagFilterItem) : TimelineAction()
    class RocketFilterItemChangedAction(val rocketFilterItem: FilterItem.RocketFilterItem) :
        TimelineAction()

    class ItemClickedAction(val item: LaunchListItem) : TimelineAction()
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
) : ReducerViewModel<TimelineState, TimelineAction, SideEffect>(TimelineState()),
    LaunchListItemViewModel.OnListInteractionListener,
    FilterSelectionListener {

    init {
        viewModelScope.launch {
            submitAction(Init)
        }
    }

    override fun mapActionToActionFlow(action: TimelineAction) =
        if (action is LoadLaunches) {
            flow {
                when (refreshTimelineItems.refresh()) {
                    is SuccessResponse -> emit(
                        LaunchesLoaded(
                            loadTimeline(action.filterSpec)
                        )
                    )
                    is ErrorResponse -> ErrorAction
                    else -> ErrorAction
                }
            }
        } else {
            super.mapActionToActionFlow(action)
        }

    override fun ScanScope.scan(
        action: TimelineAction,
        oldState: TimelineState
    ): TimelineState {
        return when (action) {
            is Init -> {
                oldState.copy(filterItems = createUnselectedFilterItems())
            }
            is TagFilterItemChangedAction -> {
                val newState = handleTagFilterItemChanged(action, oldState)
                emitAction(LoadLaunches(newState.filterSpec, force = false))
                newState
            }
            is RocketFilterItemChangedAction -> {
                val newState = handleRocketFilterItemChanged(action, oldState)
                emitAction(LoadLaunches(newState.filterSpec, force = false))
                newState
            }
            is ClearAllClickAction -> {
                emitAction(LoadLaunches(FilterSpec.EMPTY_FILTER, force = false))
                oldState.copy(
                    filterSpec = FilterSpec.EMPTY_FILTER,
                    clearButtonVisible = false,
                    filterItems = createFilterItemsFromFilterSpec(FilterSpec.EMPTY_FILTER)
                )
            }
            is RefreshAction -> {
                emitAction(
                    LoadLaunches(
                        oldState.filterSpec,
                        force = action.force
                    )
                )
                oldState.copy(progressVisible = action.force)
            }
            is LaunchesLoaded -> {
                val list = action.launches
                val filterSpec = oldState.filterSpec
                oldState.copy(
                    showNoMatchingLaunches = list.isEmpty() && filterSpec.filterNotEmpty(),
                    showRetryToLoadLaunches = list.isEmpty() && !filterSpec.filterNotEmpty(),
                    timelineItems = mapToTimeListItem(list),
                    progressVisible = false
                )
            }
            is FilterBarClickAction -> {
                emitSideEffect(SideEffect.ShowFilter)
                oldState
            }
            is ItemClickedAction -> {
                emitSideEffect(SideEffect.NavigateTo("spaceapp://launch/${action.item.id}"))
                oldState
            }
            is ErrorAction -> {
                emitSideEffect(SideEffect.ConnectionError)
                oldState
            }
            is NavigateClickAction -> {
                val path = when (action.destination) {
                    Destination.UI_TOOLKIT_PLAYGROUND -> "ui_toolkit_playground"
                    Destination.COMPOSE_PLAYGROUND -> "compose_playground"
                    Destination.SETTINGS -> "settings"
                }
                emitSideEffect(SideEffect.NavigateTo("spaceapp://$path"))
                oldState
            }
            else -> {
                throw IllegalStateException("Unknown action: $action")
            }
        }
    }

    private fun handleTagFilterItemChanged(
        action: TagFilterItemChangedAction,
        oldState: TimelineState
    ): TimelineState {
        val tagTypes = if (action.tagFilterItem.selected) {
            oldState.filterSpec.tagTypes.plus(tagMapper.toDomainTag(action.tagFilterItem.tag))
        } else {
            oldState.filterSpec.tagTypes.minus(tagMapper.toDomainTag(action.tagFilterItem.tag))
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
        action: RocketFilterItemChangedAction,
        oldState: TimelineState
    ): TimelineState {
        val rocketTypes = if (action.rocketFilterItem.selected) {
            oldState.filterSpec.rockets.plus(action.rocketFilterItem.rocketId)
        } else {
            oldState.filterSpec.rockets.minus(action.rocketFilterItem.rocketId)
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
        return getTimelineItems.getTimelineItems(filterSpec)
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
            .map { LaunchListItem.fromLaunch(it, tagMapper) }
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

    fun onFilterBarClick() {
        submitAction(FilterBarClickAction)
    }

    fun onRefresh() {
        submitAction(RefreshAction(force = true))
    }

    override fun onItemClick(item: LaunchListItem) {
        submitAction(ItemClickedAction(item))
    }

    fun onClearAllClick() {
        submitAction(ClearAllClickAction)
    }

    fun navigateClick(destination: Destination) {
        submitAction(NavigateClickAction(destination))
    }

    override fun onTagFilterItemChanged(tagFilterItem: FilterItem.TagFilterItem) {
        submitAction(TagFilterItemChangedAction(tagFilterItem))
    }

    override fun onRocketFilterItemChanged(rocketFilterItem: FilterItem.RocketFilterItem) {
        submitAction(RocketFilterItemChangedAction(rocketFilterItem))
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
