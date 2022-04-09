package sk.kasper.ui_common

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import sk.kasper.ui_common.utils.backpress.BackPressManager
import sk.kasper.ui_common.utils.backpress.OnBackPressListener
import javax.inject.Inject

open class BaseFragment : Fragment(), OnBackPressListener {

    @Inject
    lateinit var backPressManager: BackPressManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        backPressManager.addListener(this)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        backPressManager.removeListener(this)
    }

    override fun onBackPress(): Boolean = false

}