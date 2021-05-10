package sk.kasper.ui_timeline

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.GravityCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collect
import sk.kasper.ui_common.BaseFragment
import sk.kasper.ui_common.utils.createSlideAnimNavOptions
import sk.kasper.ui_timeline.databinding.FragmentTimelineBinding
import sk.kasper.ui_timeline.ui.FilterDrawer
import sk.kasper.ui_timeline.ui.Timeline


class TimelineFragment : BaseFragment() {

    private val timelineViewModel: TimelineViewModel by viewModels()

    private lateinit var binding: FragmentTimelineBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTimelineBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding.toolbar) {
            inflateMenu(R.menu.menu_timeline)
            menu.findItem(R.id.menu_compose_playground).isVisible = BuildConfig.DEBUG
            menu.findItem(R.id.menu_ui_toolkit_playground).isVisible = BuildConfig.DEBUG
            setOnMenuItemClickListener(::onMenuItemClicked)
            NavigationUI.setupWithNavController(this, findNavController())
        }

        observeViewModels()
    }

    private fun onMenuItemClicked(item: MenuItem): Boolean = when (item.itemId) {
        R.id.menu_filter -> {
            binding.drawerLayout.openDrawer(GravityCompat.END)
            true
        }
        R.id.menu_settings -> {
            findNavController().navigate(
                Uri.parse("spaceapp://settings"),
                createSlideAnimNavOptions()
            )
            true
        }
        R.id.menu_ui_toolkit_playground -> {
            findNavController().navigate(
                Uri.parse("spaceapp://ui_toolkit_playground"),
                createSlideAnimNavOptions()
            )
            true
        }
        R.id.menu_compose_playground -> {
            findNavController().navigate(
                Uri.parse("spaceapp://compose_playground"),
                createSlideAnimNavOptions()
            )
            true
        }
        else -> false
    }

    private fun observeViewModels() {
        val viewModel: TimelineViewModel by viewModels()
        binding.filterComposeView.setContent {
            FilterDrawer(viewModel)
        }

        binding.timelineComposeView.setContent {
            Timeline(viewModel)
        }

        lifecycleScope.launchWhenStarted {
            timelineViewModel.sideEffects.collect {
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
                    is SideEffect.ShowLaunchDetail -> {
                        findNavController().navigate(
                            Uri.parse("spaceapp://launch/$it"),
                            createSlideAnimNavOptions()
                        )
                    }
                }
            }
        }

        // todo do something
        // (activity as MainActivity).setIdle(!it)
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
