package sk.kasper.ui_common.utils

import androidx.annotation.MainThread
import androidx.fragment.app.createViewModelLazy
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import sk.kasper.ui_common.BaseFragment

@MainThread
inline fun <reified VM : ViewModel> BaseFragment.viewModels(
    noinline ownerProducer: () -> ViewModelStoreOwner = { this },
    noinline block: (() -> VM)? = null
) = createViewModelLazy(
    VM::class,
    { ownerProducer().viewModelStore },
    { block?.let { provideViewModelFactory(block) } ?: viewModelFactory })

inline fun <reified A : ViewModel> provideViewModelFactory(crossinline block: () -> A): ViewModelProvider.Factory {
    return object : ViewModelProvider.Factory {
        override fun <A : ViewModel> create(modelClass: Class<A>): A {
            @Suppress("UNCHECKED_CAST")
            return block() as A
        }
    }
}
