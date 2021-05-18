package sk.kasper.ui_common.utils

import androidx.annotation.MainThread
import androidx.fragment.app.Fragment
import androidx.fragment.app.createViewModelLazy
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner

@MainThread
inline fun <reified VM : ViewModel> Fragment.viewModels(
    noinline ownerProducer: () -> ViewModelStoreOwner = { this },
    noinline block: () -> VM
) = createViewModelLazy(
    VM::class,
    { ownerProducer().viewModelStore },
    { provideViewModelFactory(block) })

inline fun <reified A : ViewModel> provideViewModelFactory(crossinline block: () -> A): ViewModelProvider.Factory {
    return object : ViewModelProvider.Factory {
        override fun <A : ViewModel> create(modelClass: Class<A>): A {
            @Suppress("UNCHECKED_CAST")
            return block() as A
        }
    }
}
