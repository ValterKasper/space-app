package sk.kasper.ui_settings.preferences

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp

@Composable
internal fun PreferenceCategory(
    modifier: Modifier = Modifier,
    title: Int,
    content: @Composable ColumnScope.() -> Unit
) {

    Column(modifier = modifier) {
        Divider(modifier = Modifier.fillMaxWidth())
        Text(
            stringResource(title),
            modifier = Modifier
                .fillMaxWidth()
                .semantics { heading() }
                .padding(16.dp),
            style = MaterialTheme.typography.body1.copy(color = MaterialTheme.colors.secondary)
        )
        content()
    }
}