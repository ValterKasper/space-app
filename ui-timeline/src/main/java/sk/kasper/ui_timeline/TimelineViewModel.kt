package sk.kasper.ui_timeline

import dagger.hilt.android.lifecycle.HiltViewModel
import org.threeten.bp.LocalDateTime
import org.threeten.bp.LocalTime
import sk.kasper.base.SettingsManager
import sk.kasper.domain.model.ErrorResponse
import sk.kasper.domain.model.FilterSpec
import sk.kasper.domain.model.SuccessResponse
import sk.kasper.domain.usecase.GetTimelineItems
import sk.kasper.domain.usecase.RefreshTimelineItems
import sk.kasper.entity.Launch
import sk.kasper.ui_common.mapper.MapToDomainTag
import sk.kasper.ui_common.mapper.MapToUiTag
import sk.kasper.ui_common.mapper.RocketMapper
import sk.kasper.ui_common.tag.FilterRocket
import sk.kasper.ui_common.tag.FilterTag
import sk.kasper.ui_common.tag.LaunchFilterItem
import sk.kasper.ui_common.viewmodel.ReducerViewModel
import javax.inject.Inject


data class TimelineState(
    val showNoMatchingLaunches: Boolean = false,
    val showRetryToLoadLaunches: Boolean = false,
    val clearButtonVisible: Boolean = false,
    val filterSpec: FilterSpec = FilterSpec.EMPTY_FILTER,
    val timelineItems: List<TimelineListItem> = emptyList(),
    val progressVisible: Boolean = false
) {
    override fun toString(): String {
        return "TIs = ${timelineItems.size}, filterSpec = ${filterSpec}, clearButtonVisibility = ${clearButtonVisible}, progress = ${progressVisible}"
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

@HiltViewModel
open class TimelineViewModel @Inject constructor(
    private val getTimelineItems: GetTimelineItems,
    private val refreshTimelineItems: RefreshTimelineItems,
    private val settingsManager: SettingsManager,
    private val mapToDomainTag: MapToDomainTag,
    private val mapToUiTag: MapToUiTag,
    private val rocketMapper: RocketMapper
) : ReducerViewModel<TimelineState, SideEffect>(TimelineState()) {

    init {
        init()
    }

    private fun init() = action {
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
                clearButtonVisible = false
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

    fun onFilterItemChanged(filterItem: LaunchFilterItem, selected: Boolean) = action {
        val oldState = snapshot()
        val filterSpec = when (filterItem) {
            is FilterTag -> {
                val tagTypes = if (selected) {
                    oldState.filterSpec.tagTypes.plus(mapToDomainTag(filterItem))
                } else {
                    oldState.filterSpec.tagTypes.minus(mapToDomainTag(filterItem))
                }
                oldState.filterSpec.copy(tagTypes = tagTypes)
            }
            is FilterRocket -> {
                val rocketTypes = if (selected) {
                    oldState.filterSpec.rockets.plus(rocketMapper.toDomainRocket(filterItem))
                } else {
                    oldState.filterSpec.rockets.minus(rocketMapper.toDomainRocket(filterItem))
                }
                oldState.filterSpec.copy(rockets = rocketTypes)
            }
            else -> throw IllegalStateException()
        }
        val newState = oldState.copy(
            filterSpec = filterSpec,
            progressVisible = true,
            clearButtonVisible = filterSpec.filterNotEmpty()
        )

        reloadTimelineItems(newState.filterSpec)
    }

    private suspend fun loadTimeline(filterSpec: FilterSpec): List<Launch> {
        return when (val response = getTimelineItems(filterSpec)) {
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
            .map { LaunchListItem.fromLaunch(it, mapToUiTag, rocketMapper) }
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
        when (refreshTimelineItems()) {
            is SuccessResponse -> {
            }
            is ErrorResponse -> emitSideEffect(SideEffect.ConnectionError)
        }
    }

    private suspend fun reloadTimelineItems(filterSpec: FilterSpec) {
        val list = mapToTimeListItem(loadTimeline(filterSpec))
        reduce {
            copy(
                showNoMatchingLaunches = list.isEmpty() && filterSpec.filterNotEmpty(),
                showRetryToLoadLaunches = list.isEmpty() && !filterSpec.filterNotEmpty(),
                timelineItems = list,
                progressVisible = false
            )
        }
    }

}
