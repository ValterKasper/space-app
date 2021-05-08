package sk.kasper.ui_timeline

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Checkbox
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.view.GravityCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collect
import sk.kasper.domain.model.Rocket
import sk.kasper.domain.model.Tag
import sk.kasper.ui_common.BaseFragment
import sk.kasper.ui_common.theme.SpaceTheme
import sk.kasper.ui_common.utils.createSlideAnimNavOptions
import sk.kasper.ui_timeline.databinding.FragmentTimelineBinding
import sk.kasper.ui_timeline.filter.FilterItem
import sk.kasper.ui_timeline.filter.RocketViewModel
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
            menu.findItem(R.id.menu_compose_playground).isVisible = BuildConfig.DEBUG
            menu.findItem(R.id.menu_ui_toolkit_playground).isVisible = BuildConfig.DEBUG
            setOnMenuItemClickListener(::onMenuItemClicked)
            NavigationUI.setupWithNavController(this, findNavController())
        }

        binding.swipeRefresh.setOnRefreshListener {
            timelineViewModel.onRefresh()
        }

        timelineItemsAdapter = TimelineItemsAdapter(requireContext(), timelineViewModel)
        binding.launchesRecyclerView.adapter = timelineItemsAdapter

        timelineFilterItemsAdapter = TimelineFilterItemsAdapter(requireContext(), timelineViewModel)
//        binding.filterRecycleView.adapter = timelineFilterItemsAdapter

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
        binding.composeView.setContent {
            val viewModel: TimelineViewModel by viewModels()
            val state by viewModel.state.collectAsState()
            FilterDrawer(
                state.filterItems,
                viewModel::onTagFilterItemChanged,
                viewModel::onRocketFilterItemChanged,
                viewModel::onClearAllClick
            )
        }

        lifecycleScope.launchWhenStarted {
            timelineViewModel.state.collect {
                Timber.d(it.toString())
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

    @Composable
    fun AppContainer(body: @Composable () -> Unit) {
        SpaceTheme {
            body()
        }
    }

    @Composable
    fun FilterDrawer(
        filterItems: List<FilterItem>,
        onTagCheckChange: (FilterItem.TagFilterItem) -> Unit = {},
        onRocketCheckChange: (FilterItem.RocketFilterItem) -> Unit = {},
        onClearAllClick: () -> Unit = {}
    ) {
        AppContainer {
            Column {
                Row {
                    Text(stringResource(R.string.filter), style = MaterialTheme.typography.h3)
                    TextButton(onClick = onClearAllClick) {
                        Text(text = stringResource(R.string.clear_all))
                    }
                }
                LazyColumn {
                    items(filterItems) { filterItem ->
                        when (filterItem) {
                            is FilterItem.HeaderFilterItem -> {
                                Text(
                                    modifier = Modifier.defaultMinSize(minHeight = 48.dp),
                                    text = stringResource(filterItem.stringRes),
                                    style = MaterialTheme.typography.h6
                                )
                            }
                            is FilterItem.TagFilterItem -> {
                                TagFilterItemComposable(filterItem, onTagCheckChange)
                            }
                            is FilterItem.RocketFilterItem -> {
                                Row {
                                    val rocketViewModel = RocketViewModel(filterItem.rocketId)
                                    Text(text = stringResource(rocketViewModel.label))
                                    Checkbox(
                                        checked = filterItem.selected,
                                        onCheckedChange = { checked ->
                                            onRocketCheckChange(
                                                filterItem.copy(selected = checked)
                                            )
                                        })
                                }
                            }
                        }

                    }
                }
            }
        }
    }

    @Preview(
        widthDp = 256
    )
    @Composable
    fun FilterDrawerPreview() {
        FilterDrawer(
            filterItems = listOf(
                FilterItem.HeaderFilterItem(R.string.tags),
                FilterItem.TagFilterItem(Tag.ISS, true),
                FilterItem.TagFilterItem(Tag.MARS, false),
                FilterItem.HeaderFilterItem(R.string.title_rockets),
                FilterItem.RocketFilterItem(Rocket.ARIANE_5, true),
                FilterItem.RocketFilterItem(Rocket.FALCON_9, false),
            )
        )
    }

    @Composable
    fun TagFilterItemComposable(
        filterItem: FilterItem.TagFilterItem,
        onTagCheckChange: (FilterItem.TagFilterItem) -> Unit
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            val tagViewModel = TagViewModel(filterItem.tagType)
            Text(text = stringResource(tagViewModel.label), modifier = Modifier.weight(1f))
            Checkbox(
                checked = filterItem.selected,
                onCheckedChange = { checked ->
                    onTagCheckChange(
                        filterItem.copy(selected = checked)
                    )
                })
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
