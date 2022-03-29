package sk.kasper.ui_common.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

open class ReducerViewModel<STATE, SIDE_EFFECT>(
    defaultState: STATE,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Main
) : ViewModel() {

    private val _state: MutableStateFlow<STATE> = MutableStateFlow(defaultState)
    val state: StateFlow<STATE> = _state

    private val _sideEffects: MutableSharedFlow<SIDE_EFFECT> = MutableSharedFlow(extraBufferCapacity = 10)
    val sideEffects: SharedFlow<SIDE_EFFECT> = _sideEffects

    protected fun action(transform: suspend () -> Unit) {
        Timber.d("action - 0, $dispatcher")
        viewModelScope.launch(dispatcher) {
            Timber.d("action - launch - 1")
            transform()
            Timber.d("action - launch - 2")
        }
    }

    protected suspend fun emitSideEffect(sideEffect2: SIDE_EFFECT) {
        _sideEffects.emit(sideEffect2)
    }

    protected suspend fun reduce(reducer: STATE.() -> STATE) {
        withContext(dispatcher) {
            _state.value = _state.value.reducer()
        }
    }

    protected suspend fun snapshot(): STATE {
        return withContext(dispatcher) {
            _state.value
        }
    }
}