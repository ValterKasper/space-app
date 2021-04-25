package sk.kasper.ui_timeline

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.GravityCompat
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.google.android.material.snackbar.Snackbar
import sk.kasper.ui_common.utils.provideViewModel
import sk.kasper.ui_timeline.filter.TimelineFilterItemsAdapter
import sk.kasper.ui_timeline.filter.TimelineFilterSpecModel
import sk.kasper.ui_timeline.filter.TimelineFilterViewModel
import sk.kasper.ui_common.BaseFragment
import sk.kasper.ui_common.utils.createSlideAnimNavOptions
import sk.kasper.ui_timeline.databinding.FragmentTimelineBinding
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
            findNavController().navigate(Uri.parse("spaceapp://settings"), createSlideAnimNavOptions())
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
        timelineViewModel.timelineItems.observe(viewLifecycleOwner, {
            timelineItemsAdapter.setTimelineItems(it)
        })

        timelineViewModel.connectionErrorEvent.observe(viewLifecycleOwner, {
            Snackbar.make(binding.root, "Connection error occurred", Snackbar.LENGTH_SHORT).show()
        })

        timelineViewModel.showFilterEvent.observe(viewLifecycleOwner, {
            binding.drawerLayout.openDrawer(GravityCompat.END)
        })

        timelineViewModel.showLaunchDetailEvent.observe(viewLifecycleOwner, {
            findNavController().navigate(
                Uri.parse("spaceapp://launch/$it"),
                createSlideAnimNavOptions()
            )
        })

        timelineViewModel.progressVisible.observe(viewLifecycleOwner, {
            binding.swipeRefresh.isRefreshing = it

            // todo do something
            // (activity as MainActivity).setIdle(!it)
        })

        filterViewModel.filterItems.observe(viewLifecycleOwner, {
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
