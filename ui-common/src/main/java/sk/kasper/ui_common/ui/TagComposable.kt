package sk.kasper.ui_common.ui

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import sk.kasper.ui_common.R
import sk.kasper.ui_common.tag.UiTag
import sk.kasper.ui_common.theme.tag
import java.util.*

@Composable
fun TagComposable(tag: UiTag) {
    Surface(
        shape = MaterialTheme.shapes.tag,
        color = colorResource(id = R.color.tagBackground),
        contentColor = MaterialTheme.colors.onBackground
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(start = 12.dp, end = 8.dp, top = 4.dp, bottom = 3.dp)
        ) {
            Text(
                style = MaterialTheme.typography.body2,
                text = stringResource(tag.label).capitalize(Locale.getDefault())
            )

            Spacer(modifier = Modifier.size(6.dp))

            // just circle
            Box(
                modifier = Modifier
                    .background(colorResource(id = tag.color), CircleShape)
                    .size(dimensionResource(id = R.dimen.launch_tag_circle_size))
            ) {}
        }
    }
}

@Composable
@Preview(uiMode = UI_MODE_NIGHT_YES, name = "Night")
fun TagComposablePreviewNight() {
    TagComposable(tag = UiTag.MARS)
}

@Composable
@Preview(uiMode = UI_MODE_NIGHT_NO, name = "Day")
fun TagComposablePreviewDay() {
    TagComposable(tag = UiTag.MARS)
}