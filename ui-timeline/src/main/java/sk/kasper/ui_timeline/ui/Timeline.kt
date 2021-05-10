package sk.kasper.ui_timeline.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import org.threeten.bp.LocalDateTime
import sk.kasper.domain.model.Rocket
import sk.kasper.domain.model.Tag
import sk.kasper.ui_common.theme.SpaceTheme
import sk.kasper.ui_common.ui.LaunchDateTime
import sk.kasper.ui_common.utils.RoundedSquareLetterProvider
import sk.kasper.ui_timeline.*
import sk.kasper.ui_timeline.R
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun Timeline(viewModel: TimelineViewModel) {
    val state by viewModel.state.collectAsState()

    SpaceTheme {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            if (state.clearButtonVisible) {
                FilterBar(viewModel::onFilterBarClick, viewModel::onClearAllClick)
            }

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

                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(state.timelineItems) { item ->
                        when (item) {
                            is LaunchListItem -> LaunchListItem(item, viewModel::onItemClick)
                            is LabelListItem -> LabelListItem(item)
                        }
                    }
                }
            }
        }
    }
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
                .requiredHeight(56.dp),
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
private fun LaunchListItem(item: LaunchListItem, onItemClick: (LaunchListItem) -> Unit = {}) {
    Row(Modifier
        .fillMaxWidth()
        .clickable { onItemClick(item) }
        .padding(vertical = 12.dp)
    ) {
        val viewModel =
            LaunchListItemViewModel(object : LaunchListItemViewModel.OnListInteractionListener {
                override fun onItemClick(item: LaunchListItem) {
                }
            }).apply {
                launchListItem = item
            }

        RocketIcon(
            modifier = Modifier.padding(
                start = dimensionResource(id = R.dimen.padding_normal),
                top = 4.dp,
                bottom = 8.dp
            ),
            rocketName = viewModel.title,
            rocketIconId = viewModel.rocketIcon
        )
        Column(modifier = Modifier.padding(horizontal = dimensionResource(id = R.dimen.padding_normal))) {
            Text(
                text = viewModel.title,
                style = MaterialTheme.typography.body1
            )
            LaunchDateTime(
                viewModel.launchDateTime,
                viewModel.formattedTimeType,
                viewModel.dateConfirmed,
                viewModel.prettyTimeVisible,
                viewModel.formattedTimeVisible
            )
            Row(modifier = Modifier.padding(top = 4.dp)) {
                item.tags.forEach {
                    TagComposable(tagType = it.type)
                    Spacer(modifier = Modifier.requiredWidth(2.dp))
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
    Section(text = text)
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
        // todo compose for some reason doesn't work with webp
        Icon(
            painter = painterResource(id = rocketIconId),
            modifier = modifier
                .clip(RoundedCornerShape(dimensionResource(id = R.dimen.rounded_corners_radius)))
                .requiredSize(dimensionResource(id = R.dimen.rocket_icon_size)),
            contentDescription = "null"
        )
    }
}

@Preview(
    showBackground = true,
    backgroundColor = android.graphics.Color.WHITE.toLong()
)
@Composable
fun LaunchListItemPreview() {
    SpaceTheme {
        LaunchListItem(
            LaunchListItem(
                "id",
                "Atlas V 421 | SBIRS GEO-5",
                LocalDateTime.of(2021, 7, 25, 10, 59),
                rocketId = Rocket.ATLAS_5,
                rocketName = "Atlas V",
                accurateDate = true,
                accurateTime = true,
                tags = listOf(Tag.CUBE_SAT, Tag.ISS, Tag.MARS).map { Tag("id", it) }),
        )
    }
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