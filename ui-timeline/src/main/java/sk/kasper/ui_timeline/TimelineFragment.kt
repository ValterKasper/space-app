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
import sk.kasper.ui_timeline.filter.TimelineFilterItemsAdapter
import timber.log.Timber


class TimelineFragment : BaseFragment() {

    private val timelineViewModel: TimelineViewModel by viewModels()

    private lateinit var binding: FragmentTimelineBinding

    private lateinit var timelineFilterItemsAdapter: TimelineFilterItemsAdapter
    private lateinit var timelineItemsAdapter: TimelineItemsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTimelineBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.timelineViewModel = timelineViewModel

        with(binding.toolbar) {
            inflateMenu(R.menu.menu_timeline)
            menu.findItem(R.id.menu_playground).isVisible = BuildConfig.DEBUG
            setOnMenuItemClickListener(::onMenuItemClicked)
            NavigationUI.setupWithNavController(this, findNavController())
        }

        binding.swipeRefresh.setOnRefreshListener {
            timelineViewModel.onRefresh()
        }

        timelineItemsAdapter = TimelineItemsAdapter(requireContext(), timelineViewModel)
        binding.launchesRecyclerView.adapter = timelineItemsAdapter

        timelineFilterItemsAdapter = TimelineFilterItemsAdapter(requireContext(), timelineViewModel)
        binding.filterRecycleView.adapter = timelineFilterItemsAdapter

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
        R.id.menu_playground -> {
            findNavController().navigate(
                Uri.parse("spaceapp://playground"),
                createSlideAnimNavOptions()
            )
            true
        }
        else -> false
    }

    private fun observeViewModels() {
        lifecycleScope.launchWhenStarted {
            timelineViewModel.state.collect {
                Timber.d(it.toString())
                binding.clearFilters.visibility = it.clearButtonVisibility
                binding.filtersBar.visibility = it.clearButtonVisibility
                binding.swipeRefresh.isRefreshing = it.progressVisible
                binding.emptyStateLinearLayout.visibility =
                    if (it.showNoMatchingLaunches) View.VISIBLE else View.GONE
                binding.showRetryToLoadLaunches.visibility =
                    if (it.showRetryToLoadLaunches) View.VISIBLE else View.GONE
                timelineItemsAdapter.setTimelineItems(it.timelineItems)
                timelineFilterItemsAdapter.setFilterItems(it.filterItems)
            }
        }

        lifecycleScope.launchWhenStarted {
            timelineViewModel.sideEffects.collect {
                when (it) {
                    TimelineViewModel.SideEffect.ConnectionError -> {
                        Snackbar.make(
                            binding.root,
                            "Connection error occurred",
                            Snackbar.LENGTH_SHORT
                        )
                            .show()
                    }
                    TimelineViewModel.SideEffect.ShowFilter -> {
                        binding.drawerLayout.openDrawer(GravityCompat.END)
                    }
                    is TimelineViewModel.SideEffect.ShowLaunchDetail -> {
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
