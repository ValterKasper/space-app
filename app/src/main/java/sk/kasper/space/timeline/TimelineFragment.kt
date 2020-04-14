package sk.kasper.space.timeline

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.GravityCompat
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.google.android.material.snackbar.Snackbar
import sk.kasper.space.BaseFragment
import sk.kasper.space.BuildConfig
import sk.kasper.space.R
import sk.kasper.space.databinding.FragmentTimelineBinding
import sk.kasper.space.mainactivity.MainActivity
import sk.kasper.space.timeline.filter.TimelineFilterItemsAdapter
import sk.kasper.space.timeline.filter.TimelineFilterSpecModel
import sk.kasper.space.timeline.filter.TimelineFilterViewModel
import sk.kasper.space.utils.provideViewModel
import javax.inject.Inject


class TimelineFragment : BaseFragment() {

    private lateinit var timelineViewModel: TimelineViewModel
    private lateinit var filterViewModel: TimelineFilterViewModel

    private lateinit var binding: FragmentTimelineBinding

    private lateinit var timelineFilterItemsAdapter: TimelineFilterItemsAdapter
    private lateinit var timelineItemsAdapter: TimelineItemsAdapter

    @Inject
    lateinit var timelineViewModelFactory: TimelineViewModel.Factory

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val timelineFilterSpecModel: TimelineFilterSpecModel = provideViewModel()

        timelineViewModel = provideViewModel {
            timelineViewModelFactory.create(timelineFilterSpecModel)
        }

        filterViewModel = provideViewModel {
            TimelineFilterViewModel(timelineFilterSpecModel)
        }

        binding = FragmentTimelineBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.timelineViewModel = timelineViewModel
        binding.filterViewModel = filterViewModel

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

        timelineFilterItemsAdapter = TimelineFilterItemsAdapter(requireContext(), filterViewModel)
        binding.filterRecycleView.adapter = timelineFilterItemsAdapter

        observeViewModels()
    }

    private fun onMenuItemClicked(item: MenuItem): Boolean = when (item.itemId) {
        R.id.menu_filter -> {
            binding.drawerLayout.openDrawer(GravityCompat.END)
            true
        }
        R.id.menu_settings -> {
            findNavController().navigate(R.id.action_timelineFragment_to_settingsFragment)
            true
        }
        R.id.menu_playground -> {
            findNavController().navigate(R.id.action_timelineFragment_to_playgroundFragment)
            true
        }
        else -> false
    }

    private fun observeViewModels() {
        timelineViewModel.timelineItems.observe(viewLifecycleOwner, Observer {
            timelineItemsAdapter.setTimelineItems(it)
        })

        timelineViewModel.connectionErrorEvent.observe(viewLifecycleOwner, Observer {
            Snackbar.make(binding.root, "Connection error occurred", Snackbar.LENGTH_SHORT).show()
        })

        timelineViewModel.showFilterEvent.observe(viewLifecycleOwner, Observer {
            binding.drawerLayout.openDrawer(GravityCompat.END)
        })

        timelineViewModel.showLaunchDetailEvent.observe(viewLifecycleOwner, Observer {
            findNavController().navigate(TimelineFragmentDirections.actionTimelineFragmentToLaunchFragment(it))
        })

        timelineViewModel.progressVisible.observe(viewLifecycleOwner, Observer {
            binding.swipeRefresh.isRefreshing = it
            (activity as MainActivity).setIdle(!it)
        })

        filterViewModel.filterItems.observe(viewLifecycleOwner, Observer {
            timelineFilterItemsAdapter.setFilterItems(it)
        })
    }

    override fun onBackPress(): Boolean =
            if (binding.drawerLayout.isDrawerOpen(GravityCompat.END)) {
                binding.drawerLayout.closeDrawer(GravityCompat.END)
                true
            } else {
                false
            }
}
