package sk.kasper.space.timeline.filter

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.channels.ReceiveChannel
import sk.kasper.domain.model.FilterSpec

open class TimelineFilterSpecModel: ViewModel() {

    // todo sprav cez flow
    @ExperimentalCoroutinesApi
    private val conflatedBroadcastChannel: ConflatedBroadcastChannel<FilterSpec> = ConflatedBroadcastChannel(FilterSpec.EMPTY_FILTER)

    @ExperimentalCoroutinesApi
    open var value = FilterSpec.EMPTY_FILTER
        set(value) {
            field = value
            conflatedBroadcastChannel.offer(value)
        }

    @ExperimentalCoroutinesApi
    val channel: ReceiveChannel<FilterSpec> = conflatedBroadcastChannel.openSubscription()

}