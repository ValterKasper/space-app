package sk.kasper.ui_common.tag

import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import sk.kasper.ui_common.R


interface FilterItem {
    @get:StringRes
    val label: Int

    @get:ColorRes
    val color: Int
}

data class FilterDefinition<T : FilterItem>(
    val topFilterItems: List<T>,
    val extensionFilterItems: Map<T, List<T>>
)

private data class FilterState<T : FilterItem>(
    val beforeFilterItems: List<T>,
    val beforeFilterItemsVisible: Boolean = true,
    val selectedFilterItems: List<T> = emptyList(),
    val selectedFilterItemsVisible: Boolean = false,
    val afterFilterItems: List<T> = emptyList(),
    val afterFilterItemsVisible: Boolean = false,
    val extensionFilterItems: List<T> = emptyList(),
    val extensionFilterItemsVisible: Boolean = false,
    val clearVisible: Boolean = false
)

@Composable
fun <T : FilterItem> Filter(
    onItemSelected: (T, Boolean) -> Unit = { _, _ -> },
    onClearAll: () -> Unit = {},
    filterDefinition: FilterDefinition<T>
) {
    var filterState by remember {
        mutableStateOf(FilterState(filterDefinition.topFilterItems))
    }

    FilterRow(state = filterState, onClearAllClick = {
        onClearAll()
        filterState = FilterState(filterDefinition.topFilterItems)
    }, onItemSelected = { item ->
        val index = filterDefinition.topFilterItems.indexOf(item)

        val isExtensionFilterItem = index == -1
        if (isExtensionFilterItem) {
            filterState = if (filterState.selectedFilterItems.contains(item)) {
                val selectedFilterItems = filterState.selectedFilterItems - item
                val selectedTopLevelTFilterItems = selectedFilterItems.first()

                onItemSelected(item, false)
                filterState.copy(
                    extensionFilterItems = filterDefinition.extensionFilterItems.getValue(
                        selectedTopLevelTFilterItems
                    ),
                    selectedFilterItems = selectedFilterItems
                )
            } else {
                onItemSelected(item, true)
                filterState.copy(
                    extensionFilterItems = emptyList(),
                    selectedFilterItems = filterState.selectedFilterItems + listOf(item)
                )
            }
        } else {
            filterState = if (filterState.selectedFilterItems.contains(item)) {
                onClearAll()
                FilterState(filterDefinition.topFilterItems)
            } else {
                onItemSelected(item, true)
                FilterState(
                    clearVisible = true,
                    beforeFilterItems = filterDefinition.topFilterItems.subList(0, index),
                    beforeFilterItemsVisible = true,
                    selectedFilterItems = listOf(item),
                    selectedFilterItemsVisible = true,
                    afterFilterItems = filterDefinition.topFilterItems.subList(
                        index + 1,
                        filterDefinition.topFilterItems.size
                    ),
                    afterFilterItemsVisible = true,
                    extensionFilterItems = filterDefinition.extensionFilterItems[item]
                        ?: emptyList(),
                    extensionFilterItemsVisible = true,
                )

            }
        }
    })
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun <TAG : FilterItem> FilterRow(
    state: FilterState<TAG>,
    onClearAllClick: () -> Unit = { },
    onItemSelected: (TAG) -> Unit = { _ -> }
) {
    Box {
        Surface(
            elevation = 4.dp,
            modifier = Modifier
                .background(Color.Green)
                .fillMaxWidth()
                .height(48.dp)
        ) { }

        val s by remember {
            mutableStateOf(ScrollState(0))
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(s)
                .padding(horizontal = 12.dp, vertical = 8.dp)
        ) {

            var exitTransitionEnded by remember { mutableStateOf(false) }
            val exitTransition =
                updateTransition(targetState = !state.clearVisible, label = "exit transition")
            val exitAlpha by exitTransition.animateFloat(label = "exit animate alpha") {
                if (it) 1.0f else 0.0f
            }
            val exitPaddingTop by exitTransition.animateDp(label = "exit animate dp") {
                if (it) 0.dp else 48.dp
            }

            exitTransitionEnded = exitPaddingTop == 48.dp

            AnimatedVisibility(visible = state.clearVisible && exitTransitionEnded) {
                FilterClearButton(onClearAllClick)
            }

            state.beforeFilterItems.forEach { name ->
                AnimatedVisibility(
                    state.beforeFilterItemsVisible && !exitTransitionEnded,
                ) {
                    Box(
                        modifier = Modifier
                            .alpha(exitAlpha)
                            .padding(top = exitPaddingTop),
                    ) {
                        FilterItemComposable(name,
                            selected = false,
                            onItemSelected = { item, _ -> onItemSelected(item) })
                    }
                }
            }

            if (state.selectedFilterItemsVisible) {
                state.selectedFilterItems.forEach { name ->
                    FilterItemComposable(
                        name,
                        selected = state.clearVisible,
                        onItemSelected = { item, _ -> onItemSelected(item) })
                }
            }

            state.afterFilterItems.forEach { name ->
                AnimatedVisibility(state.afterFilterItemsVisible && !exitTransitionEnded) {
                    Box(
                        modifier = Modifier
                            .alpha(exitAlpha)
                            .padding(top = exitPaddingTop)
                    ) {
                        FilterItemComposable(name,
                            selected = false,
                            onItemSelected = { item, _ -> onItemSelected(item) })
                    }
                }
            }

            if (state.extensionFilterItemsVisible) {
                state.extensionFilterItems.forEach { name ->
                    FilterItemComposable(
                        name,
                        selected = false,
                        onItemSelected = { item, _ -> onItemSelected(item) })
                }
            }
        }
    }
}

@Composable
private fun FilterClearButton(onClearAllClick: () -> Unit) {
    Box(modifier = Modifier
        .semantics { contentDescription = "clear button" }
        .size(32.dp)
        .padding(2.dp)
        .border(
            2.dp,
            color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f),
            shape = MaterialTheme.shapes.small.copy(all = CornerSize(percent = 50))
        )
        .clip(shape = MaterialTheme.shapes.small.copy(all = CornerSize(percent = 50)))
        .clickable { onClearAllClick() }
        .padding(4.dp)) {
        Icon(
            modifier = Modifier.align(Alignment.Center),
            painter = painterResource(id = R.drawable.ic_baseline_close_24),
            contentDescription = "Clear filter"
        )
    }
}
