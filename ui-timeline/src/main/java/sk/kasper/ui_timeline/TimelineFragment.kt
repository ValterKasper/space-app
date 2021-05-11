package sk.kasper.ui_timeline

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.GravityCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collect
import sk.kasper.ui_common.BaseFragment
import sk.kasper.ui_common.utils.createSlideAnimNavOptions
import sk.kasper.ui_timeline.databinding.FragmentTimelineBinding
import sk.kasper.ui_timeline.ui.FilterDrawer
import sk.kasper.ui_timeline.ui.Timeline
import timber.log.Timber


class TimelineFragment : BaseFragment() {

    private val timelineViewModel: TimelineViewModel by viewModels()

    private lateinit var binding: FragmentTimelineBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        observeViewModels()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTimelineBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.filterComposeView.setContent {
            FilterDrawer(timelineViewModel)
        }

        binding.timelineComposeView.setContent {
            Timeline(timelineViewModel)
        }

        // todo do something
        // (activity as MainActivity).setIdle(!it)
    }

    private fun observeViewModels() {
        lifecycleScope.launchWhenStarted {
            timelineViewModel.sideEffects.collect {
                Timber.d("$it")
                when (it) {
                    SideEffect.ConnectionError -> {
                        Snackbar.make(
                            binding.root,
                            "Connection error occurred",
                            Snackbar.LENGTH_SHORT
                        )
                            .show()
                    }
                    SideEffect.ShowFilter -> {
                        binding.drawerLayout.openDrawer(GravityCompat.END)
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

    // todo move to state, when is used compose
    override fun onBackPress(): Boolean =
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.END)) {
            binding.drawerLayout.closeDrawer(GravityCompat.END)
            true
        } else {
            false
        }
}
