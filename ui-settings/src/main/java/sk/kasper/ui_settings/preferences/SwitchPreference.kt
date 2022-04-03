package sk.kasper.ui_settings.preferences

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ListItem
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import sk.kasper.base.SettingKey

@ExperimentalMaterialApi
@Composable
fun SwitchPreference(
    modifier: Modifier = Modifier,
    settingKey: SettingKey,
    title: Int,
    summary: Int,
    checked: Boolean,
    onValueChange: (SettingKey, Boolean) -> Unit
) {
    val onClick = {
        onValueChange(settingKey, !checked)
    }
    ListItem(
        modifier = modifier
            .padding(vertical = 4.dp)
            .clickable(onClick = onClick),
        text = { Text(stringResource(title)) },
        secondaryText = { Text(stringResource(summary)) },
        trailing = {
            Switch(
                checked = checked,
                onCheckedChange = { onClick() })
        }
    )
}
