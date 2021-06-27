package sk.kasper.ui_common.tag

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
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import sk.kasper.ui_common.R
import sk.kasper.ui_common.theme.SourceSansPro


enum class Tag(val title: String, val color: Color) {
    Mars("Mars", Color(0xFFFF5722)),
    Moon("Moon", Color(0xFF9C27B0)),
    ISS("ISS", Color(0xFF009688)),
    SpaceX("SpaceX", Color(0xFF3F51B5)),
    Falcon("Falcon 9", Color(0xFF9C27B0)),
    FalconHeavy("Falcon heavy", Color(0xFF8BC34A)),
    Starship("Starship", Color(0xFF673AB7)),
    SpaceShuttle("Space shuttle", Color(0xFF03A9F4)),
    Soyuz("Soyuz", Color(0xFFFF9800)),
    Ariane5("Ariane 5", Color(0xFF4CAF50)),
    Rover("Rover", Color(0xFF9C27B0)),
    Cargo("Cargo", Color(0xFF00BCD4)),
    Crew("Crew", Color(0xFF8BC34A)),
}

val topTags = listOf(Tag.Mars, Tag.ISS, Tag.Moon, Tag.SpaceX, Tag.Ariane5, Tag.Soyuz)
val extensionTags = mapOf(
    Tag.ISS to listOf(Tag.SpaceShuttle, Tag.Falcon),
    Tag.SpaceX to listOf(Tag.Falcon, Tag.FalconHeavy, Tag.Starship)
)

private data class FilterState(
    val beforeTags: List<Tag> = topTags,
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
fun FilterScreen(onFilterClicked: () -> Unit = {}) {
    var filterState by remember {
        mutableStateOf(FilterState())
    }

    FilterRow(state = filterState, onClearAllClick = {
        onFilterClicked()
        filterState = filterState.copy(
            clearVisible = false,
            selectedTags = filterState.selectedTags.filter { topTags.contains(it) }
        )
    }, onTagSelected = { tag ->
        onFilterClicked()
        val index = topTags.indexOf(tag)

        if (index == -1) {
            filterState = filterState.copy(
                extensionTags = emptyList(),
                selectedTags = filterState.selectedTags + listOf(tag)
            )
        } else {
            filterState = FilterState(
                clearVisible = true,
                beforeTags = topTags.subList(0, index),
                beforeTagsVisible = true,
                selectedTags = listOf(tag),
                selectedTagsVisible = true,
                afterTags = topTags.subList(index + 1, topTags.size),
                afterTagsVisible = true,
                extensionTags = extensionTags[tag] ?: emptyList(),
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
    onTagSelected: (Tag, Boolean) -> Unit
) {
    val shape = MaterialTheme.shapes.small.copy(all = CornerSize(percent = 50))
    val alpha = if (selected) 0.2f else 0.0f
    val color = tag.color
    Text(
        tag.title,
        style = MaterialTheme.typography.body2.copy(
//            fontWeight = FontWeight.SemiBold,
            fontFamily = SourceSansPro
        ),
        color = lerp(color, MaterialTheme.colors.onSurface, 0.55f),
        modifier = Modifier
            .height(36.dp)
            .padding(3.dp)
            .border(
                2.dp,
                color = color.copy(alpha = 0.7f),
                shape = shape
            )
            .clip(shape = shape)
            .clickable { onTagSelected(tag, !selected) }
            .background(color = color.copy(alpha = alpha))
            .padding(start = 16.dp, end = 16.dp, top = 4.dp)
    )
}