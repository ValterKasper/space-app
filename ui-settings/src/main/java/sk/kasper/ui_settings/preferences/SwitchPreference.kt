package sk.kasper.ui_settings.preferences

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ListItem
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import sk.kasper.ui_common.settings.SettingKey

@ExperimentalMaterialApi
@Composable
fun SwitchPreference(
    modifier: Modifier = Modifier,
    settingKey: SettingKey,
    title: Int,
    summary: Int
) {
    val settingsManager = LocalSettingsManager.current
    val checked = settingsManager.getBoolAsFlow(settingKey)
        .collectAsState(settingsManager.getBoolean(settingKey))
    val onClick = {
        settingsManager.setBoolean(
            settingKey,
            !checked.value
        )
    }
    ListItem(
        modifier = modifier
            .padding(vertical = 4.dp)
            .clickable(onClick = onClick),
        text = { Text(stringResource(title)) },
        secondaryText = { Text(stringResource(summary)) },
        trailing = {
            Switch(
                checked = checked.value,
                onCheckedChange = { onClick() })
        }
    )
}
