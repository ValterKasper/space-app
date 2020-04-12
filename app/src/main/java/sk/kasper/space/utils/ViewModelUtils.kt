package sk.kasper.space.utils

import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

inline fun <reified A : ViewModel> Fragment.provideViewModel(crossinline block: () -> A): A {
    return ViewModelProvider(this, object : ViewModelProvider.Factory {
        override fun <A : ViewModel> create(modelClass: Class<A>): A {
            @Suppress("UNCHECKED_CAST")
            return block() as A
        }
    }).get(A::class.java)
}

inline fun <reified A : ViewModel> Fragment.provideViewModel(@Nullable viewModelFactory: ViewModelProvider.Factory): A {
    return ViewModelProvider(this, viewModelFactory).get(A::class.java)
}

inline fun <reified A : ViewModel> Fragment.provideViewModel(): A {
    return ViewModelProvider(this).get(A::class.java)
}

inline fun <reified A : ViewModel> Fragment.provideViewModelActivityScoped(): A {
    return ViewModelProvider(requireActivity()).get(A::class.java)
}