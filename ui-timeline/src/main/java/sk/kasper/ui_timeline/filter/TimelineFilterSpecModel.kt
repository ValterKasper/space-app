package sk.kasper.ui_timeline.filter

import kotlinx.coroutines.flow.Flow
import sk.kasper.domain.model.FilterSpec
import sk.kasper.ui_timeline.utils.StateFlow

class TimelineFilterSpecModel {

    private val stateFlow = StateFlow(FilterSpec.EMPTY_FILTER)

    var value: FilterSpec
        get() = stateFlow.value
        set(value) {
            stateFlow.value = value
        }

    val flow: Flow<FilterSpec> = stateFlow.flow

}