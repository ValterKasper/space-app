package sk.kasper.ui_common.tag

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import sk.kasper.ui_common.R
import sk.kasper.ui_common.theme.SourceSansPro


interface Tag {
    val title: String
    val color: Color
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
fun Filter(onFilterClicked: () -> Unit = {}, filterDefinition: FilterDefinition) {
    var filterState by remember {
        mutableStateOf(FilterState(filterDefinition.topTags))
    }

    FilterRow(state = filterState, onClearAllClick = {
        onFilterClicked()
        filterState = filterState.copy(
            clearVisible = false,
            selectedTags = filterState.selectedTags.filter { filterDefinition.topTags.contains(it) }
        )
    }, onTagSelected = { tag ->
        onFilterClicked()
        val index = filterDefinition.topTags.indexOf(tag)

        if (index == -1) {
            filterState = filterState.copy(
                extensionTags = emptyList(),
                selectedTags = filterState.selectedTags + listOf(tag)
            )
        } else {
            filterState = FilterState(
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

@Composable
private fun FilterTag(
    tag: Tag,
    selected: Boolean,
    onTagSelected: (Tag, Boolean) -> Unit,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) {
    val shape = MaterialTheme.shapes.small.copy(all = CornerSize(percent = 50))
    val alpha = if (selected) 0.2f else 0.0f
    val color = tag.color
    val toggleableModifier =
        Modifier.toggleable(
            value = selected,
            onValueChange = { newValue -> onTagSelected(tag, newValue) },
            role = Role.Checkbox,
            interactionSource = interactionSource,
            indication = null
        )
    Text(
        tag.title,
        style = MaterialTheme.typography.body2.copy(
//            fontWeight = FontWeight.SemiBold,
            fontFamily = SourceSansPro
        ),
        color = lerp(color, MaterialTheme.colors.onSurface, 0.55f),
        modifier = Modifier
            .then(toggleableModifier)
            .height(36.dp)
            .padding(3.dp)
            .border(
                2.dp,
                color = color.copy(alpha = 0.7f),
                shape = shape
            )
            .clip(shape = shape)
            .background(color = color.copy(alpha = alpha))
            .padding(start = 16.dp, end = 16.dp, top = 4.dp)
    )
}