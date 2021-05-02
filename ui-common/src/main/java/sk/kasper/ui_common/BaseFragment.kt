package sk.kasper.ui_common

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.annotation.MainThread
import androidx.fragment.app.Fragment
import androidx.fragment.app.createViewModelLazy
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelStoreOwner
import dagger.android.support.AndroidSupportInjection
import sk.kasper.ui_common.utils.backpress.BackPressManager
import sk.kasper.ui_common.utils.backpress.OnBackPressListener
import sk.kasper.ui_common.viewmodel.ViewModelFactory
import javax.inject.Inject

open class BaseFragment : Fragment(), OnBackPressListener {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var backPressManager: BackPressManager

    @MainThread
    inline fun <reified VM : ViewModel> BaseFragment.viewModels(
        noinline ownerProducer: () -> ViewModelStoreOwner = { this }
    ) = createViewModelLazy(VM::class, { ownerProducer().viewModelStore }, { viewModelFactory })

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        backPressManager.addListener(this)
    }

    override fun onResume() {
        super.onResume()
        // track current fragment as shown screen. It works until there is at most one visible fragment a time.
        // otherwise implementation should be changed

        // todo do without referencing firebase in ui-common
        //FirebaseAnalytics.getInstance(requireContext()).setCurrentScreen(requireActivity(), javaClass.simpleName, javaClass.simpleName)

        view?.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
    }

    override fun onDestroyView() {
        super.onDestroyView()
        backPressManager.removeListener(this)
    }

    override fun onBackPress(): Boolean = false

}