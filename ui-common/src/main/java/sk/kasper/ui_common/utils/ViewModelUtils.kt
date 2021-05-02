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