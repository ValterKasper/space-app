package sk.kasper.ui_timeline.utils

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow

/**
 * Temporary solution for holding a value and with ability to be notified about changes with the Flow.
 * Provides similar functionality as the BehaviorSubject from Rx.
 * Should be replaced with the DataFlow https://github.com/Kotlin/kotlinx.coroutines/pull/1354, when is ready.
 */
class StateFlow<T>(defaultValue: T) {

    @ExperimentalCoroutinesApi
    private val conflatedBroadcastChannel: ConflatedBroadcastChannel<T> = ConflatedBroadcastChannel(defaultValue)

    var value = defaultValue
        set(value) {
            field = value
            conflatedBroadcastChannel.offer(value)
        }

    val flow: Flow<T> = conflatedBroadcastChannel.asFlow()
}