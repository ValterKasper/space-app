package sk.kasper.ui_launch.section

import sk.kasper.domain.model.ErrorResponse
import sk.kasper.domain.model.Response
import sk.kasper.domain.model.SuccessResponse
import sk.kasper.ui_common.viewmodel.ReducerViewModel

abstract class LoaderViewModel<STATE, LOAD>(defaultState: STATE) :
    ReducerViewModel<STATE, LoaderViewModel.SideEffect>(defaultState) {

    enum class SideEffect

    fun loadAction() = action {
        when (val load = load()) {
            is SuccessResponse -> reduce {
                mapLoadToState(load.data, this)
            }
            is ErrorResponse -> reduce {
                mapErrorToState(load.message, this)
            }
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