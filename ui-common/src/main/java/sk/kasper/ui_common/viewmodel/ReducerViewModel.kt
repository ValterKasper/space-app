package sk.kasper.ui_common.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber

abstract class ReducerViewModel<STATE, ACTION, SIDE_EFFECT>(defaultState: STATE) : ViewModel() {

    private val mutableState: MutableStateFlow<STATE> = MutableStateFlow(defaultState)
    val state: StateFlow<STATE> = mutableState

    private val pendingActions = MutableSharedFlow<ACTION>(extraBufferCapacity = 10)

    private val mutableSideEffects: MutableSharedFlow<SIDE_EFFECT> =
        MutableSharedFlow(extraBufferCapacity = 10)
    val sideEffects: SharedFlow<SIDE_EFFECT> = mutableSideEffects

    init {
        viewModelScope.launch {
            pendingActions
                .onEach { Timber.tag("ACTION").d("$it") }
                .flatMapMerge { action -> mapActionToActionFlow(action) }
                .scan(ScanResult(state = defaultState)) { oldScanResult: ScanResult, action: ACTION ->
                    val scanScope = ScanScope()
                    val newState = scanScope.scan(action, oldScanResult.state)
                    ScanResult(newState, scanScope.action, scanScope.sideEffect)
                }.collect { scanResult ->
                    Timber.tag("STATE").d("${scanResult.state}")
                    mutableState.value = scanResult.state
                    scanResult.action?.let { action ->
                        submitAction(action)
                    }
                    scanResult.sideEffect?.let {
                        mutableSideEffects.emit(it)
                    }
                }
        }
    }

    fun submitAction(action: ACTION) {
        viewModelScope.launch {
            pendingActions.emit(action)
        }
    }

    inner class ScanScope {
        internal var sideEffect: SIDE_EFFECT? = null
        internal var action: ACTION? = null

        fun emitAction(action: ACTION) {
            this.action = action
        }

        fun emitSideEffect(sideEffect: SIDE_EFFECT) {
            this.sideEffect = sideEffect
        }
    }

    protected open fun ScanScope.scan(action: ACTION, oldState: STATE): STATE = oldState

    protected open fun mapActionToActionFlow(action: ACTION): Flow<ACTION> {
        return flow { emit(action) }
    }

    private inner class ScanResult(
        val state: STATE,
        val action: ACTION? = null,
        val sideEffect: SIDE_EFFECT? = null
    )
}