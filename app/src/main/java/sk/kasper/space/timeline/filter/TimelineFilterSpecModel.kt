package sk.kasper.space.timeline.filter

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.Flow
import sk.kasper.domain.model.FilterSpec
import sk.kasper.space.utils.StateFlow

class TimelineFilterSpecModel: ViewModel() {

    private val stateFlow = StateFlow(FilterSpec.EMPTY_FILTER)

    var value: FilterSpec
        get() = stateFlow.value
        set(value) {
            stateFlow.value = value
        }

    val flow: Flow<FilterSpec> = stateFlow.flow

}