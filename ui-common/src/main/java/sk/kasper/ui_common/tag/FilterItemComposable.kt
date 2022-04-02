package sk.kasper.ui_common.tag

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import sk.kasper.ui_common.theme.SourceSansPro
import sk.kasper.ui_common.theme.SpaceTheme
import java.util.*

@Composable
fun <T : FilterItem> FilterItemComposable(
    filterItem: T,
    selected: Boolean = true,
    onItemSelected: (T, Boolean) -> Unit = { _, _ -> },
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) {
    val shape = MaterialTheme.shapes.small.copy(all = CornerSize(percent = 50))
    val alpha = if (selected) 0.2f else 0.0f
    val color = colorResource(id = filterItem.color)
    val toggleableModifier =
        Modifier.toggleable(
            value = selected,
            onValueChange = { newValue -> onItemSelected(filterItem, newValue) },
            role = Role.Checkbox,
            interactionSource = interactionSource,
            indication = null
        )
    Text(
        stringResource(filterItem.label).replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() },
        style = MaterialTheme.typography.body2.copy(
            fontFamily = SourceSansPro
        ),
        color = lerp(color, MaterialTheme.colors.onSurface, 0.55f),
        modifier = Modifier
            .then(toggleableModifier)
            .height(32.dp)
            .padding(3.dp)
            .border(
                2.dp,
                color = color.copy(alpha = 0.7f),
                shape = shape
            )
            .clip(shape = shape)
            .background(color = color.copy(alpha = alpha))
            .padding(start = 16.dp, end = 16.dp, top = 2.dp)
    )
}


@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, name = "Day")
fun FilterItemComposablePreviewDay() {
    SpaceTheme {
        Surface {
            Column {
                FilterItemComposable(filterItem = FilterTag.MARS, selected = false)
                FilterItemComposable(filterItem = FilterTag.MARS, selected = true)
            }
        }
    }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, name = "Night")
fun FilterItemComposablePreviewNight() {
    SpaceTheme {
        Surface {
            Column {
                FilterItemComposable(filterItem = FilterTag.MARS, selected = false)
                FilterItemComposable(filterItem = FilterTag.MARS, selected = true)
            }
        }
    }
}