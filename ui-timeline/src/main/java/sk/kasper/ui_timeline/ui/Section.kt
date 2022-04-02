package sk.kasper.ui_timeline.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeightIn
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import sk.kasper.ui_common.theme.section
import sk.kasper.ui_timeline.R
import java.util.*

@Composable
fun Section(text: String) {
    Box(
        modifier = Modifier
            .requiredHeightIn(min = 48.dp)
            .padding(horizontal = dimensionResource(id = R.dimen.padding_normal)),
        contentAlignment = Alignment.CenterStart
    ) {
        Text(
            text = text.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() },
            style = MaterialTheme.typography.section,
        )
    }
}