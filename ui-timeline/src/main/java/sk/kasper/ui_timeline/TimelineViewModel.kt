package sk.kasper.ui_timeline

import android.view.View
import androidx.databinding.Bindable
import androidx.databinding.ObservableInt
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
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
import sk.kasper.ui_timeline.filter.TimelineFilterSpecModel
import timber.log.Timber
import javax.inject.Inject

open class TimelineViewModel @Inject constructor(
    private val getTimelineItems: GetTimelineItems,
    private val refreshTimelineItems: RefreshTimelineItems,
    private val settingsManager: SettingsManager
) : ObservableViewModel(),
    LaunchListItemViewModel.OnListInteractionListener,
    FilterSelectionListener {

    val timelineItems: MutableStateFlow<List<TimelineListItem>> = MutableStateFlow(emptyList())
    val progressVisible: MutableStateFlow<Boolean> = MutableStateFlow(false)

    val connectionErrorEvent: MutableSharedFlow<Unit> = MutableSharedFlow(extraBufferCapacity = 100)
    val showLaunchDetailEvent: MutableSharedFlow<String> =
        MutableSharedFlow(extraBufferCapacity = 100)
    val showFilterEvent: MutableSharedFlow<Unit> = MutableSharedFlow(extraBufferCapacity = 100)

    @get:Bindable
    var showNoMatchingLaunches: Boolean = false

    @get:Bindable
    var showRetryToLoadLaunches: Boolean = false

    val filterItems: MutableStateFlow<List<FilterItem>> = MutableStateFlow(emptyList())

    val clearButtonVisibility: ObservableInt = ObservableInt(View.GONE)

    private val timelineFilterSpecModel: TimelineFilterSpecModel = TimelineFilterSpecModel()

    init {
        viewModelScope.launch {
            timelineFilterSpecModel.flow.collect {
                runLongOp {
                    loadTimeline(it)
                }
            }
        }

        val actualFilterSpec = timelineFilterSpecModel.value
        filterItems.value = if (actualFilterSpec.filterNotEmpty()) {
            clearButtonVisibility.set(View.VISIBLE)
            emptyList<FilterItem>()
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
        } else {
            clearButtonVisibility.set(View.GONE)
            createUnselectedFilterItems()
        }
    }

    private suspend fun loadTimeline(filterSpec: FilterSpec) {
        Timber.d("LoadingResponse")
        val timelineItemsRaw = getTimelineItems.getTimelineItems(filterSpec)
        val listItems = mapToTimeListItem(timelineItemsRaw)
        Timber.d("SuccessResponse size: ${listItems.size}")
        timelineItems.value = listItems
        showNoMatchingLaunches = listItems.isEmpty() && filterSpec.filterNotEmpty()
        showRetryToLoadLaunches = listItems.isEmpty() && !filterSpec.filterNotEmpty()
        notifyPropertyChanged(BR.showNoMatchingLaunches)
        notifyPropertyChanged(BR.showRetryToLoadLaunches)
    }

    private fun mapToTimeListItem(list: List<Launch>): List<TimelineListItem> {
        val items: MutableList<TimelineListItem> = mutableListOf()
        val currentDateTime = getCurrentDateTime()
        val todayStartDateTime = LocalDateTime.of(currentDateTime.toLocalDate(), LocalTime.MIDNIGHT) // start of this day
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

    fun onFilterBarClick() {
        viewModelScope.launch {
            showFilterEvent.emit(Unit)
        }
    }

    fun onRefresh() {
        viewModelScope.launch {
            runLongOp {
                when (refreshTimelineItems.refresh()) {
                    is SuccessResponse -> loadTimeline(timelineFilterSpecModel.value)
                    is ErrorResponse -> connectionErrorEvent.emit(Unit)
                }
            }
        }
    }

    private suspend fun runLongOp(block: suspend () -> Unit) {
        progressVisible.value = true
        block()
        progressVisible.value = false
    }

    override fun onItemClick(item: LaunchListItem) {
        viewModelScope.launch {
            showLaunchDetailEvent.emit(item.id)
        }
    }

    open fun getCurrentDateTime(): LocalDateTime = LocalDateTime.now()

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
        clearButtonVisibility.set(
            if (timelineFilterSpecModel.value.filterNotEmpty()) {
                View.VISIBLE
            } else {
                View.GONE
            }
        )
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
