package sk.kasper.ui_launch.section

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import sk.kasper.domain.model.ErrorResponse
import sk.kasper.domain.model.Response
import sk.kasper.domain.model.SuccessResponse
import sk.kasper.ui_common.viewmodel.ReducerViewModel
import sk.kasper.ui_launch.section.LoaderViewModel.Action.Show

abstract class LoaderViewModel<STATE, LOAD>(defaultState: STATE) :
    ReducerViewModel<STATE, LoaderViewModel.Action, LoaderViewModel.SideEffect>(defaultState) {

    sealed class Action {
        object Init : Action()
        data class Show<LOAD>(val load: LOAD) : Action()
        data class OnError(val message: String?) : Action()
    }

    enum class SideEffect

    // todo how to handle Init from init

    final override fun mapActionToActionFlow(action: Action): Flow<Action> {
        return if (action == Action.Init) {
            flow {
                when (val load = load()) {
                    is SuccessResponse -> emit(Show(load.data))
                    is ErrorResponse -> emit(Action.OnError(load.message))
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
                mapLoadToState(action.load as LOAD, oldState)
            }
            is Action.OnError -> {
                mapErrorToState(action.message, oldState)
            }
            else -> oldState
        }
    }

    open fun mapLoadToState(load: LOAD, oldState: STATE): STATE {
        return oldState
    }

    open fun mapErrorToState(message: String?, oldState: STATE): STATE {
        return oldState
    }

    abstract suspend fun load(): Response<LOAD>
}