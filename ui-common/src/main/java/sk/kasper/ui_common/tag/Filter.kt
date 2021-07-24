package sk.kasper.ui_common.tag

import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import sk.kasper.ui_common.R


interface Tag {
    @get:StringRes
    val label: Int

    @get:ColorRes
    val color: Int
}

data class FilterDefinition(val topTags: List<Tag>, val extensionTags: Map<Tag, List<Tag>>)

private data class FilterState(
    val beforeTags: List<Tag>,
    val beforeTagsVisible: Boolean = true,
    val selectedTags: List<Tag> = emptyList(),
    val selectedTagsVisible: Boolean = false,
    val afterTags: List<Tag> = emptyList(),
    val afterTagsVisible: Boolean = false,
    val extensionTags: List<Tag> = emptyList(),
    val extensionTagsVisible: Boolean = false,
    val clearVisible: Boolean = false
)

@Composable
fun Filter(
    onTagSelected: (Tag, Boolean) -> Unit = { _, _ -> },
    onClearAll: () -> Unit = {},
    filterDefinition: FilterDefinition
) {
    var filterState by remember {
        mutableStateOf(FilterState(filterDefinition.topTags))
    }

    FilterRow(state = filterState, onClearAllClick = {
        onClearAll()
        filterState = FilterState(filterDefinition.topTags)
    }, onTagSelected = { tag ->
        val index = filterDefinition.topTags.indexOf(tag)

        val isExtensionTag = index == -1
        if (isExtensionTag) {
            filterState = if (filterState.selectedTags.contains(tag)) {
                val selectedTags = filterState.selectedTags - tag
                val selectedTopLevelTag = selectedTags.first()

                onTagSelected(tag, false)
                filterState.copy(
                    extensionTags = filterDefinition.extensionTags.getValue(selectedTopLevelTag),
                    selectedTags = selectedTags
                )
            } else {
                onTagSelected(tag, true)
                filterState.copy(
                    extensionTags = emptyList(),
                    selectedTags = filterState.selectedTags + listOf(tag)
                )
            }
        } else {
            filterState = if (filterState.selectedTags.contains(tag)) {
                onClearAll()
                FilterState(filterDefinition.topTags)
            } else {
                onTagSelected(tag, true)
                FilterState(
                    clearVisible = true,
                    beforeTags = filterDefinition.topTags.subList(0, index),
                    beforeTagsVisible = true,
                    selectedTags = listOf(tag),
                    selectedTagsVisible = true,
                    afterTags = filterDefinition.topTags.subList(
                        index + 1,
                        filterDefinition.topTags.size
                    ),
                    afterTagsVisible = true,
                    extensionTags = filterDefinition.extensionTags[tag] ?: emptyList(),
                    extensionTagsVisible = true,
                )

            }
        }
    })
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun FilterRow(
    state: FilterState,
    onClearAllClick: () -> Unit = { },
    onTagSelected: (Tag) -> Unit = { _ -> }
) {
    Surface {
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

            state.beforeTags.forEach { name ->
                AnimatedVisibility(
                    state.beforeTagsVisible && !exitTransitionEnded,
                    initiallyVisible = true
                ) {
                    Box(
                        modifier = Modifier
                            .alpha(exitAlpha)
                            .padding(top = exitPaddingTop),
                    ) {
                        FilterTag(name,
                            selected = false,
                            onTagSelected = { tag, _ -> onTagSelected(tag) })
                    }
                }
            }

            if (state.selectedTagsVisible) {
                state.selectedTags.forEach { name ->
                    FilterTag(
                        name,
                        selected = state.clearVisible,
                        onTagSelected = { tag, _ -> onTagSelected(tag) })
                }
            }

            state.afterTags.forEach { name ->
                AnimatedVisibility(state.afterTagsVisible && !exitTransitionEnded) {
                    Box(
                        modifier = Modifier
                            .alpha(exitAlpha)
                            .padding(top = exitPaddingTop)
                    ) {
                        FilterTag(name,
                            selected = false,
                            onTagSelected = { tag, _ -> onTagSelected(tag) })
                    }
                }
            }

            if (state.extensionTagsVisible) {
                state.extensionTags.forEach { name ->
                    FilterTag(
                        name,
                        selected = false,
                        onTagSelected = { tag, _ -> onTagSelected(tag) })
                }
            }
        }
    }
}

@Composable
private fun FilterClearButton(onClearAllClick: () -> Unit) {
    Box(modifier = Modifier
        .semantics { contentDescription = "clear button" }
        .size(36.dp)
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
