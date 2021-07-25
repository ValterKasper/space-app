package sk.kasper.ui_common.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import sk.kasper.ui_common.tag.FilterItemComposable
import sk.kasper.ui_common.tag.FilterTag

@Composable
fun TagsRow(modifier: Modifier, list: List<FilterTag>) {
    Row(modifier = modifier) {
        list.forEach {
            FilterItemComposable(filterItem = it)
            Spacer(modifier = Modifier.requiredWidth(4.dp))
        }
    }
}