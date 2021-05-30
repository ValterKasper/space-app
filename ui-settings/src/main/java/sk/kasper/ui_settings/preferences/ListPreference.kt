package sk.kasper.ui_settings.preferences

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Alignment.Companion.End
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import sk.kasper.ui_common.settings.SettingKey
import java.util.*

@ExperimentalMaterialApi
@Composable
fun ListPreference(
    modifier: Modifier = Modifier,
    settingKey: SettingKey,
    title: Int,
    values: Map<Int, Int>
) {
    val settingsManager = LocalSettingsManager.current

    val selectedValue = settingsManager.getIntAsFlow(settingKey)
        .collectAsState(settingsManager.getInt(settingKey))

    var showDialog by remember { mutableStateOf(false) }

    ListItem(
        modifier = modifier
            .padding(vertical = 4.dp)
            .clickable { showDialog = true },
        text = { Text(stringResource(title)) },
        secondaryText = { Text(stringResource(id = values.getValue(selectedValue.value))) }
    )

    if (showDialog) {
        Dialog(onDismissRequest = { showDialog = false }) {
            Surface(
                shape = MaterialTheme.shapes.large,
                color = MaterialTheme.colors.surface
            ) {
                Column(modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp)) {
                    Text(
                        stringResource(title),
                        modifier = Modifier
                            .padding(bottom = 16.dp)
                            .padding(top = 8.dp),
                        style = MaterialTheme.typography.h6
                    )
                    values.forEach { (value, name) ->
                        Row(
                            verticalAlignment = CenterVertically,
                            modifier = Modifier
                                .clickable {
                                    settingsManager.setInt(settingKey, value)
                                    showDialog = false
                                }
                                .height(48.dp)
                                .fillMaxWidth(),
                        ) {
                            RadioButton(
                                selected = value == selectedValue.value,
                                modifier = Modifier.padding(end = 24.dp),
                                onClick = null
                            )
                            Text(
                                text = stringResource(id = name),
                                style = MaterialTheme.typography.body1
                            )
                        }
                    }
                    TextButton(onClick = { showDialog = false }, modifier = Modifier.align(End)) {
                        Text(
                            text = stringResource(id = android.R.string.cancel).toUpperCase(
                                Locale.getDefault()
                            )
                        )
                    }
                }

            }
        }
    }
}
