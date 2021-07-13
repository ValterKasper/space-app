package sk.kasper.ui_common.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject
import javax.inject.Provider

@ActivityScoped
class ViewModelFactory @Inject constructor(
        private val viewModelProviders: Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>)
    : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(viewModelClass: Class<T>): T {
        val foundEntry = viewModelProviders.entries.find { viewModelClass.isAssignableFrom(it.key) }
        val provider: Provider<ViewModel> = foundEntry?.value ?: throw IllegalArgumentException("unknown view model class name: ${viewModelClass.simpleName}")

        @Suppress("UNCHECKED_CAST")
        return provider.get() as T
    }

}