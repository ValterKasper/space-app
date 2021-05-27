package sk.kasper.ui_launch.section

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun LabelValue(label: Int, value: String) {
    Row(modifier = Modifier.padding(vertical = 1.dp)) {
        Text(
            text = stringResource(id = label),
            style = MaterialTheme.typography.body1.copy(fontWeight = FontWeight.SemiBold)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = ": $value",
            style = MaterialTheme.typography.body1
        )
    }
}