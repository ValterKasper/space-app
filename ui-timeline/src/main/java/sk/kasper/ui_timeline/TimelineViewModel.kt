package sk.kasper.ui_timeline

import android.view.View
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
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
import sk.kasper.ui_common.utils.ObservableViewModel
import sk.kasper.ui_timeline.TimelineAction.*
import sk.kasper.ui_timeline.filter.FilterItem
import sk.kasper.ui_timeline.filter.FilterSelectionListener
import timber.log.Timber
import javax.inject.Inject


data class TimelineState(
    val showNoMatchingLaunches: Boolean = false,
    val showRetryToLoadLaunches: Boolean = false,
    val clearButtonVisibility: Int = View.GONE,
    val filterSpec: FilterSpec = FilterSpec.EMPTY_FILTER,
    val filterItems: List<FilterItem> = emptyList(),
    val timelineItems: List<TimelineListItem> = emptyList(),
    val progressVisible: Boolean = false
) {
    override fun toString(): String {
        val count = filterItems
            .filterIsInstance<FilterItem.TagFilterItem>()
            .count(FilterItem.TagFilterItem::selected)
        return "TIs = ${timelineItems.size}, filterSpec = ${filterSpec}, clearButtonVisibility = ${clearButtonVisibility}, progress = ${progressVisible}, selected = $count"
    }
}

sealed class TimelineAction {
    object FilterBarClickAction : TimelineAction()
    object ErrorAction : TimelineAction()
    object ClearAllClickAction : TimelineAction()
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
    data class ShowLaunchDetail(val id: String) : SideEffect()
    object ShowFilter : SideEffect()
}

class Transform(
    val state: TimelineState,
    val action: TimelineAction? = null,
    val sideEffect: SideEffect? = null
)

class StateChanger(val change: suspend TimelineState.() -> TimelineState)

private suspend fun FlowCollector<StateChanger>.emitState(change: suspend TimelineState.() -> TimelineState) {
    emit(StateChanger(change))
}

open class TimelineViewModel @Inject constructor(
    private val getTimelineItems: GetTimelineItems,
    private val refreshTimelineItems: RefreshTimelineItems,
    private val settingsManager: SettingsManager
) : ObservableViewModel(),
    LaunchListItemViewModel.OnListInteractionListener,
    FilterSelectionListener {

    val state = MutableStateFlow(TimelineState())

    val sideEffects: MutableSharedFlow<SideEffect> = MutableSharedFlow(extraBufferCapacity = 100)

    private val pendingActions = MutableSharedFlow<TimelineAction>()

    fun submitAction(action: TimelineAction) {
        viewModelScope.launch {
            pendingActions.emit(action)
        }
    }

    /** API design testing zone */
    fun updateState(block: TimelineState.() -> TimelineState) {}

    fun emitSideEffect(sideEffect: SideEffect) {}

    class onAction<A>(val processAction: suspend A.() -> Unit) {}

    class onActionWithState<A>(val processAction: suspend A.(TimelineState) -> Unit) {}

    // takto by mohol vyzerat cely viewModel
    init {
        onAction<LoadLaunches> {
            val listItems = mapToTimeListItem(loadTimeline(filterSpec))

            updateState {
                copy(timelineItems = listItems)
            }
        }

        onActionWithState<RefreshAction> { state ->
            val listItems = mapToTimeListItem(loadTimeline(state.filterSpec))

            updateState {
                copy(timelineItems = listItems)
            }
        }

        onAction<FilterBarClickAction> {
            emitSideEffect(SideEffect.ShowFilter)
        }
    }

    /** END of API design testing zone */


    init {
        viewModelScope.launch {
            val initial =
                Transform(state = TimelineState(filterItems = createUnselectedFilterItems()))
            pendingActions
                .onEach { Timber.d("$it") }
                .flatMapMerge { action ->
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
                        flow { emit(action) }
                    }
                }
                .scan(initial) { transform: Transform, action: TimelineAction ->
                    var timelineAction: TimelineAction? = null
                    var sideEffect: SideEffect? = null
                    val state = when (action) {
                        is TagFilterItemChangedAction -> {
                            val newState = handleTagFilterItemChanged(action, transform.state)
                            timelineAction =
                                LoadLaunches(newState.filterSpec, force = false)
                            newState
                        }
                        is RocketFilterItemChangedAction -> {
                            val newState = handleRocketFilterItemChanged(action, transform.state)
                            timelineAction =
                                LoadLaunches(newState.filterSpec, force = false)
                            newState
                        }
                        is ClearAllClickAction -> {
                            timelineAction =
                                LoadLaunches(FilterSpec.EMPTY_FILTER, force = false)
                            transform.state.copy(
                                filterSpec = FilterSpec.EMPTY_FILTER,
                                clearButtonVisibility = View.GONE,
                                filterItems = createFilterItemsFromFilterSpec(FilterSpec.EMPTY_FILTER)
                            )
                        }
                        is RefreshAction -> {
                            timelineAction = LoadLaunches(
                                transform.state.filterSpec,
                                force = action.force
                            )
                            transform.state.copy(progressVisible = action.force)
                        }
                        is LaunchesLoaded -> {
                            val list = action.launches
                            val filterSpec = transform.state.filterSpec
                            transform.state.copy(
                                showNoMatchingLaunches = list.isEmpty() && filterSpec.filterNotEmpty(),
                                showRetryToLoadLaunches = list.isEmpty() && !filterSpec.filterNotEmpty(),
                                timelineItems = mapToTimeListItem(list),
                                progressVisible = false
                            )
                        }
                        is FilterBarClickAction -> {
                            sideEffect = SideEffect.ShowFilter
                            transform.state
                        }
                        is ItemClickedAction -> {
                            sideEffect = SideEffect.ShowLaunchDetail(action.item.id)
                            transform.state
                        }
                        is ErrorAction -> {
                            sideEffect = SideEffect.ConnectionError
                            transform.state
                        }
                        else -> {
                            throw IllegalStateException("Unknown action: $action")
                        }
                    }

                    Transform(state = state, action = timelineAction, sideEffect = sideEffect)
                }.collect { transform ->
                    state.value = transform.state
                    transform.action?.let { action ->
                        submitAction(action)
                    }
                    transform.sideEffect?.let {
                        sideEffects.emit(it)
                    }
                }
        }

        viewModelScope.launch {
            // initial action
            submitAction(RefreshAction(force = false))
        }
    }

    private fun handleTagFilterItemChanged(
        action: TagFilterItemChangedAction,
        oldState: TimelineState
    ): TimelineState {
        val tagTypes = if (action.tagFilterItem.selected) {
            oldState.filterSpec.tagTypes.plus(action.tagFilterItem.tagType)
        } else {
            oldState.filterSpec.tagTypes.minus(action.tagFilterItem.tagType)
        }

        val filterSpec = oldState.filterSpec.copy(tagTypes = tagTypes)
        val clearButtonVisibility = if (filterSpec.filterNotEmpty()) View.VISIBLE else View.GONE

        return oldState.copy(
            filterSpec = filterSpec,
            progressVisible = true,
            clearButtonVisibility = clearButtonVisibility,
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
        val clearButtonVisibility = if (filterSpec.filterNotEmpty()) View.VISIBLE else View.GONE

        return oldState.copy(
            filterSpec = filterSpec,
            progressVisible = true,
            clearButtonVisibility = clearButtonVisibility,
            filterItems = createFilterItemsFromFilterSpec(filterSpec)
        )
    }

    private fun createFilterItemsFromFilterSpec(actualFilterSpec: FilterSpec): List<FilterItem> {
        return emptyList<FilterItem>()
            .plus(FilterItem.HeaderFilterItem(R.string.title_tags))
            .plus(FilterSpec.ALL_TAGS.map {
                FilterItem.TagFilterItem(
                    it,
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
            .map { LaunchListItem.fromLaunch(it) }
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

    override fun onTagFilterItemChanged(tagFilterItem: FilterItem.TagFilterItem) {
        submitAction(TagFilterItemChangedAction(tagFilterItem))
    }

    override fun onRocketFilterItemChanged(rocketFilterItem: FilterItem.RocketFilterItem) {
        submitAction(RocketFilterItemChangedAction(rocketFilterItem))
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
