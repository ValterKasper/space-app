package sk.kasper.ui_launch.section

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import sk.kasper.ui_common.viewmodel.ReducerViewModel
import sk.kasper.ui_launch.section.LoaderViewModel.Action.Show

abstract class LoaderViewModel<STATE, LOAD>(defaultState: STATE) :
    ReducerViewModel<STATE, LoaderViewModel.Action, LoaderViewModel.SideEffect>(defaultState) {

    sealed class Action {
        object Init : Action()
        data class Show<LOAD>(val load: LOAD) : Action()
        data class OnError(val e: Exception) : Action()
    }

    enum class SideEffect {
    }

    // todo how to handle Init from init

    final override fun mapActionToActionFlow(action: Action): Flow<Action> {
        return if (action == Action.Init) {
            flow {
                try {
                    emit(Show(load()))
                } catch (e: Exception) {
                    // todo handle errors
                    emit(Action.OnError(e))
                }
            }
        } else {
            super.mapActionToActionFlow(action)
        }
    }

    final override fun ScanScope.scan(action: Action, oldState: STATE): STATE {
        return when (action) {
            is Show<*> -> {
                @Suppress("UNCHECKED_CAST")
                mapLoadToState(action.load as LOAD)
            }
            else -> oldState
        }
    }

    abstract fun mapLoadToState(load: LOAD): STATE

    abstract suspend fun load(): LOAD
}