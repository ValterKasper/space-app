package sk.kasper.ui_timeline.ui

import android.content.res.Configuration
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.insets.navigationBarsPadding
import com.google.accompanist.insets.rememberInsetsPaddingValues
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import org.threeten.bp.LocalDateTime
import sk.kasper.ui_common.tag.Filter
import sk.kasper.ui_common.tag.FilterTag
import sk.kasper.ui_common.theme.SpaceTheme
import sk.kasper.ui_common.ui.InsetAwareTopAppBar
import sk.kasper.ui_common.ui.LaunchDateTime
import sk.kasper.ui_common.ui.TagsRow
import sk.kasper.ui_common.utils.RoundedSquareLetterProvider
import sk.kasper.ui_timeline.*
import sk.kasper.ui_timeline.R
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun Timeline(viewModel: TimelineViewModel) {
    val state by viewModel.state.collectAsState()

    SpaceTheme {
        ProvideWindowInsets {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                TimelineAppBar(viewModel)

                if (state.clearButtonVisible) {
                    FilterBar(viewModel::onFilterBarClick, viewModel::onClearAllClick)
                }

                Box {
                    SwipeRefresh(
                        state = rememberSwipeRefreshState(state.progressVisible),
                        onRefresh = viewModel::onRefresh
                    ) {
                        if (state.showNoMatchingLaunches) {
                            Box(Modifier.fillMaxSize()) {
                                Text(
                                    text = stringResource(id = R.string.no_matching_launches),
                                    style = MaterialTheme.typography.h6,
                                    modifier = Modifier
                                        .alpha(0.62f)
                                        .align(Alignment.Center)
                                )
                            }
                        }

                        if (state.showRetryToLoadLaunches) {
                            Box(Modifier.fillMaxSize()) {
                                Surface(elevation = 8.dp) {
                                    Column(modifier = Modifier.align(Alignment.Center)) {
                                        Text(
                                            text = stringResource(id = R.string.your_connections_is_off),
                                            style = MaterialTheme.typography.h6,
                                            modifier = Modifier
                                                .alpha(0.62f)
                                        )
                                        Text(
                                            text = stringResource(id = R.string.pull_to_refresh_to_try_again),
                                            style = MaterialTheme.typography.body1,
                                            modifier = Modifier
                                                .alpha(0.62f)
                                        )
                                    }
                                }
                            }
                        }

                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize(),
                            contentPadding = rememberInsetsPaddingValues(
                                insets = LocalWindowInsets.current.systemBars,
                                applyTop = false,
                                additionalTop = 56.dp
                            )
                        ) {
                            items(state.timelineItems) { item ->
                                when (item) {
                                    is LaunchListItem -> LaunchListItemLayout(
                                        LaunchListItemViewModel(
                                            item
                                        ), viewModel::onItemClick
                                    )
                                    is LabelListItem -> LabelListItem(item)
                                }
                            }
                        }
                    }

                    Filter(
                        filterDefinition = filterDefinition,
                        onItemSelected = { filterItem, selected ->
                            viewModel.onFilterItemChanged(filterItem, selected)
                        },
                        onClearAll = {
                            viewModel.onClearAllClick()
                        })
                }
            }
        }
    }
}

@Composable
private fun TimelineAppBar(viewModel: TimelineViewModel) {
    InsetAwareTopAppBar(
        title = {
            Text(
                text = stringResource(id = R.string.app_name),
                style = MaterialTheme.typography.h6,
            )
        },
        actions = {
            var expanded by remember { mutableStateOf(false) }
            IconButton(onClick = { expanded = true }) {
                Icon(
                    painterResource(id = R.drawable.ic_baseline_more_vert_24),
                    contentDescription = null
                )
            }

            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                if (BuildConfig.DEBUG) {
                    DropdownMenuItem(onClick = { viewModel.navigateClick(Destination.COMPOSE_PLAYGROUND) }) {
                        Text(stringResource(id = R.string.compose_playground))
                    }
                }
                DropdownMenuItem(onClick = { viewModel.navigateClick(Destination.SETTINGS) }) {
                    Text(stringResource(id = R.string.settings))
                }
            }
        })
}

@Composable
private fun FilterBar(onFilterBarClick: () -> Unit = {}, onClearAllClick: () -> Unit = {}) {
    Surface(
        elevation = 4.dp,
        modifier = Modifier.clickable { onFilterBarClick() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .requiredHeight(56.dp)
                .navigationBarsPadding(bottom = false),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(id = R.string.filtering_is_on),
                style = MaterialTheme.typography.body1,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = dimensionResource(id = R.dimen.padding_normal))
            )
            Icon(
                modifier = Modifier
                    .clickable { onClearAllClick() }
                    .padding(dimensionResource(id = R.dimen.padding_normal))
                    .requiredSize(24.dp),
                painter = painterResource(id = R.drawable.ic_clear_all),
                contentDescription = stringResource(id = R.string.filter),
                tint = MaterialTheme.colors.secondary,
            )
        }
    }
}

@Composable
fun LaunchListItemLayout(
    viewModel: LaunchListItemViewModel,
    onItemClick: (LaunchListItem) -> Unit = {}
) {
    Surface {
        Row(Modifier
            .fillMaxWidth()
            .clickable { onItemClick(viewModel.item) }
            .padding(vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            RocketIcon(
                modifier = Modifier.padding(
                    start = dimensionResource(id = R.dimen.padding_normal)
                ),
                rocketName = viewModel.title,
                rocketIconId = viewModel.rocketIcon
            )
            Column(modifier = Modifier.padding(horizontal = dimensionResource(id = R.dimen.padding_normal))) {
                Text(
                    text = viewModel.title,
                    style = MaterialTheme.typography.body1,
                    modifier = Modifier.padding(bottom = 2.dp)
                )
                CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                    LaunchDateTime(
                        Modifier.padding(bottom = 4.dp),
                        viewModel.launchDateTime,
                        viewModel.formattedTimeType,
                        viewModel.dateConfirmed,
                        viewModel.prettyTimeVisible,
                        viewModel.formattedTimeVisible
                    )
                }
                if (viewModel.tagsVisible) {
                    TagsRow(Modifier.padding(top = 4.dp), viewModel.tags)
                }
            }
        }

    }
}

@Composable
private fun LabelListItem(item: LabelListItem) {
    val simpleDateFormat = SimpleDateFormat("LLLL", Locale.getDefault())
    val calendar = GregorianCalendar()
    val text = when (item) {
        is LabelListItem.Today -> stringResource(R.string.today)
        is LabelListItem.Tomorrow -> stringResource(R.string.tomorrow)
        is LabelListItem.ThisWeek -> stringResource(R.string.this_week)
        is LabelListItem.Month -> {
            calendar.set(Calendar.DAY_OF_MONTH, 1)
            calendar.set(Calendar.MONTH, item.month - 1)
            simpleDateFormat.format(calendar.time)
        }
    }
    Surface {
        Section(text = text)
    }
}

@Composable
private fun RocketIcon(
    modifier: Modifier = Modifier, rocketName: String, rocketIconId: Int
) {
    if (rocketIconId == 0) {
        // todo temp solution: compose canvas can't draw text
        val roundedSquareLetterProvider = RoundedSquareLetterProvider(LocalContext.current)
        Surface(
            modifier = modifier.requiredSize(dimensionResource(id = R.dimen.rocket_icon_size))
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                drawIntoCanvas {
                    roundedSquareLetterProvider.drawLetterOnCanvas(
                        it.nativeCanvas,
                        rocketName.first()
                    )
                }
            }
        }
    } else {
        Image(
            painter = painterResource(id = rocketIconId),
            modifier = modifier
                .clip(RoundedCornerShape(dimensionResource(id = R.dimen.rounded_corners_radius)))
                .requiredSize(dimensionResource(id = R.dimen.rocket_icon_size)),
            contentDescription = "null"
        )
    }
}


@Composable
fun LaunchListItemForPreview(showTags: Boolean = true) {
    SpaceTheme {
        Surface {
            LaunchListItemLayout(
                LaunchListItemViewModel(
                    LaunchListItem(
                        "id",
                        "Atlas V 421 | SBIRS GEO-5",
                        LocalDateTime.of(2021, 7, 25, 10, 59),
                        rocketResId = R.drawable.ariane_5,
                        rocketName = "Atlas V",
                        accurateDate = true,
                        accurateTime = true,
                        tags = if (showTags) listOf(
                            FilterTag.CUBE_SAT,
                            FilterTag.ISS,
                            FilterTag.MARS
                        ) else emptyList()
                    ),
                    LocalDateTime.of(2020, 2, 28, 12, 30)
                )
            )
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
fun LaunchListItemForPreviewDay() {
    LaunchListItemForPreview()
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
fun LaunchListItemForPreviewDayNoTags() {
    LaunchListItemForPreview(showTags = false)
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun LaunchListItemForPreviewNight() {
    LaunchListItemForPreview()
}

@Preview(
    showBackground = true,
    backgroundColor = android.graphics.Color.WHITE.toLong()
)
@Composable
fun RocketIconPreview() {
    SpaceTheme {
        Row(Modifier.padding(16.dp)) {
            val modifier = Modifier.size(16.dp)
            RocketIcon(rocketName = "F", rocketIconId = R.drawable.falcon_9)
            Spacer(modifier = modifier)
            RocketIcon(rocketName = "L", rocketIconId = 0)
            Spacer(modifier = modifier)
            RocketIcon(rocketName = "A", rocketIconId = R.drawable.ariane_5)
            Spacer(modifier = modifier)
            RocketIcon(rocketName = "G", rocketIconId = 0)
        }
    }
}

@Preview
@Composable
fun FilterBarPreview() {
    SpaceTheme {
        FilterBar()
    }
}