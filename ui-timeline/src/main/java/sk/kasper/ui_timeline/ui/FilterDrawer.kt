package sk.kasper.ui_timeline.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import sk.kasper.domain.model.Rocket
import sk.kasper.domain.model.Tag
import sk.kasper.ui_common.theme.SpaceTheme
import sk.kasper.ui_timeline.R
import sk.kasper.ui_timeline.TimelineViewModel
import sk.kasper.ui_timeline.filter.FilterItem
import sk.kasper.ui_timeline.filter.RocketViewModel
import java.util.*


@Composable
fun FilterDrawer(viewModel: TimelineViewModel) {
    val state by viewModel.state.collectAsState()
    SpaceTheme {
        Surface(color = MaterialTheme.colors.background) {
            Column {
                FilterHeader(state.clearButtonVisible, viewModel::onClearAllClick)
                FilterContent(
                    state.filterItems,
                    viewModel::onTagFilterItemChanged,
                    viewModel::onRocketFilterItemChanged
                )
            }
        }
    }
}

@Composable
private fun FilterHeader(clearButtonVisible: Boolean, onClearAllClick: () -> Unit) {
    Surface(elevation = 4.dp) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(
                    start = dimensionResource(id = R.dimen.padding_normal),
                    end = 8.dp,
                    bottom = 16.dp,
                )
                .heightIn(min = 40.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = stringResource(R.string.filter),
                style = MaterialTheme.typography.h6,
                modifier = Modifier
                    .weight(1f)
            )
            if (clearButtonVisible) {
                OutlinedButton(
                    onClick = onClearAllClick,
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colors.secondary)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = stringResource(R.string.clear_all).toUpperCase(
                                Locale.getDefault()
                            )
                        )
                        Icon(
                            painter = painterResource(id = R.drawable.ic_clear_all),
                            contentDescription = null,
                            modifier = Modifier.padding(start = 4.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun FilterContent(
    filterItems: List<FilterItem>,
    onTagCheckChange: (FilterItem.TagFilterItem) -> Unit = {},
    onRocketCheckChange: (FilterItem.RocketFilterItem) -> Unit = {}
) {
    LazyColumn {
        item { Spacer(modifier = Modifier.height(16.dp)) }
        items(filterItems) { filterItem ->
            when (filterItem) {
                is FilterItem.HeaderFilterItem -> Section(stringResource(filterItem.stringRes))
                is FilterItem.TagFilterItem -> TagFilterCheckbox(filterItem, onTagCheckChange)
                is FilterItem.RocketFilterItem -> RocketFilterCheckbox(
                    filterItem,
                    onRocketCheckChange
                )
            }
        }
    }
}

@Composable
private fun FilterCheckbox(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    content: @Composable BoxScope.() -> Unit,
) {
    Box(
        contentAlignment = Alignment.CenterStart,
        modifier = Modifier
            .clickable { onCheckedChange(!checked) }
            .padding(horizontal = dimensionResource(id = R.dimen.padding_normal))
            .requiredHeight(48.dp)
            .fillMaxWidth(),
    ) {
        content()
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange,
            modifier = Modifier.align(Alignment.CenterEnd)
        )
    }
}

@Composable
private fun TagFilterCheckbox(
    filterItem: FilterItem.TagFilterItem,
    onTagCheckChange: (FilterItem.TagFilterItem) -> Unit
) {
    FilterCheckbox(
        filterItem.selected,
        { checked -> onTagCheckChange(filterItem.copy(selected = checked)) }
    ) {
        TagComposable(filterItem.tagType)
    }
}

@Composable
private fun RocketFilterCheckbox(
    filterItem: FilterItem.RocketFilterItem,
    onRocketCheckChange: (FilterItem.RocketFilterItem) -> Unit
) {
    FilterCheckbox(
        checked = filterItem.selected,
        onCheckedChange = { checked -> onRocketCheckChange(filterItem.copy(selected = checked)) }
    ) {
        val rocketViewModel = RocketViewModel(filterItem.rocketId)
        Text(text = stringResource(rocketViewModel.label))
    }
}

@Preview(
    widthDp = 256,
    showBackground = true,
    backgroundColor = android.graphics.Color.WHITE.toLong()
)
@Composable
private fun FilterDrawerPreview() {
    FilterContent(
        listOf(
            FilterItem.HeaderFilterItem(R.string.tags),
            FilterItem.TagFilterItem(Tag.ISS, true),
            FilterItem.TagFilterItem(Tag.MARS, false),
            FilterItem.HeaderFilterItem(R.string.title_rockets),
            FilterItem.RocketFilterItem(Rocket.ARIANE_5, true),
            FilterItem.RocketFilterItem(Rocket.FALCON_9, false),
        )
    )
}