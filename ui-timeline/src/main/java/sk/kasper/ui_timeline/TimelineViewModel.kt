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

    init {
        viewModelScope.launch {
            val initial =
                Transform(state = TimelineState(filterItems = createUnselectedFilterItems()))
            pendingActions
                .onEach { Timber.d("$it") }
                .flatMapMerge { action ->
                    if (action is TimelineAction.LoadLaunches) {
                        flow {
                            when (refreshTimelineItems.refresh()) {
                                is SuccessResponse -> emit(
                                    TimelineAction.LaunchesLoaded(
                                        loadTimeline(action.filterSpec)
                                    )
                                )
                                is ErrorResponse -> TimelineAction.ErrorAction
                                else -> TimelineAction.ErrorAction
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
                        is TimelineAction.TagFilterItemChangedAction -> {
                            val newState = handleTagFilterItemChanged(action, transform.state)
                            timelineAction =
                                TimelineAction.LoadLaunches(newState.filterSpec, force = false)
                            newState
                        }
                        is TimelineAction.RocketFilterItemChangedAction -> {
                            val newState = handleRocketFilterItemChanged(action, transform.state)
                            timelineAction =
                                TimelineAction.LoadLaunches(newState.filterSpec, force = false)
                            newState
                        }
                        is TimelineAction.ClearAllClickAction -> {
                            timelineAction =
                                TimelineAction.LoadLaunches(FilterSpec.EMPTY_FILTER, force = false)
                            transform.state.copy(
                                filterSpec = FilterSpec.EMPTY_FILTER,
                                clearButtonVisibility = View.GONE,
                                filterItems = createFilterItemsFromFilterSpec(FilterSpec.EMPTY_FILTER)
                            )
                        }
                        is TimelineAction.RefreshAction -> {
                            timelineAction = TimelineAction.LoadLaunches(
                                transform.state.filterSpec,
                                force = action.force
                            )
                            transform.state.copy(progressVisible = action.force)
                        }
                        is TimelineAction.LaunchesLoaded -> {
                            val list = action.launches
                            val filterSpec = transform.state.filterSpec
                            transform.state.copy(
                                showNoMatchingLaunches = list.isEmpty() && filterSpec.filterNotEmpty(),
                                showRetryToLoadLaunches = list.isEmpty() && !filterSpec.filterNotEmpty(),
                                timelineItems = mapToTimeListItem(list),
                                progressVisible = false
                            )
                        }
                        is TimelineAction.FilterBarClickAction -> {
                            sideEffect = SideEffect.ShowFilter
                            transform.state
                        }
                        is TimelineAction.ItemClickedAction -> {
                            sideEffect = SideEffect.ShowLaunchDetail(action.item.id)
                            transform.state
                        }
                        is TimelineAction.ErrorAction -> {
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
            submitAction(TimelineAction.RefreshAction(force = false))
        }
    }

    private fun handleTagFilterItemChanged(
        action: TimelineAction.TagFilterItemChangedAction,
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
        action: TimelineAction.RocketFilterItemChangedAction,
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
        submitAction(TimelineAction.FilterBarClickAction)
    }

    fun onRefresh() {
        submitAction(TimelineAction.RefreshAction(force = true))
    }

    override fun onItemClick(item: LaunchListItem) {
        submitAction(TimelineAction.ItemClickedAction(item))
    }

    fun onClearAllClick() {
        submitAction(TimelineAction.ClearAllClickAction)
    }

    override fun onTagFilterItemChanged(tagFilterItem: FilterItem.TagFilterItem) {
        submitAction(TimelineAction.TagFilterItemChangedAction(tagFilterItem))
    }

    override fun onRocketFilterItemChanged(rocketFilterItem: FilterItem.RocketFilterItem) {
        submitAction(TimelineAction.RocketFilterItemChangedAction(rocketFilterItem))
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
