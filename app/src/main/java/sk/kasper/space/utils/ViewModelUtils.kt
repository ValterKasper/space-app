package sk.kasper.space.utils

import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders

inline fun <reified A : ViewModel> Fragment.provideViewModel(crossinline block: () -> A): A {
    return ViewModelProviders.of(this, object : ViewModelProvider.Factory {
        override fun <A : ViewModel> create(modelClass: Class<A>): A {
            @Suppress("UNCHECKED_CAST")
            return block() as A
        }
    }).get(A::class.java)
}

inline fun <reified A : ViewModel> Fragment.provideViewModel(@Nullable viewModelFactory: ViewModelProvider.Factory? = null): A {
    return ViewModelProviders.of(this, viewModelFactory).get(A::class.java)
}

inline fun <reified A : ViewModel> Fragment.provideViewModelActivityScoped(@Nullable viewModelFactory: ViewModelProvider.Factory? = null): A {
    return ViewModelProviders.of(requireActivity(), viewModelFactory).get(A::class.java)
}