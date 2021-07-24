package sk.kasper.ui_timeline

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import sk.kasper.ui_common.BaseFragment
import sk.kasper.ui_common.utils.createSlideAnimNavOptions
import sk.kasper.ui_timeline.ui.Timeline
import timber.log.Timber

@AndroidEntryPoint
class TimelineFragment : BaseFragment() {

    private val timelineViewModel: TimelineViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        observeViewModels()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                Timeline(timelineViewModel)
            }
        }
    }

    private fun observeViewModels() {
        lifecycleScope.launchWhenStarted {
            timelineViewModel.sideEffects.collect {
                Timber.d("$it")
                when (it) {
                    SideEffect.ConnectionError -> {
                        // todo show snack bar
//                        Snackbar.make(
//                            drawerLayout,
//                            "Connection error occurred",
//                            Snackbar.LENGTH_SHORT
//                        )
//                            .show()
                    }
                    is SideEffect.NavigateTo -> {
                        findNavController().navigate(
                            Uri.parse(it.uriString),
                            createSlideAnimNavOptions()
                        )
                    }
                }
            }
        }
    }

}
