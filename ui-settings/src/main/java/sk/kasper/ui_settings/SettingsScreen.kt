package sk.kasper.ui_settings.preferences

import androidx.compose.foundation.layout.Column
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.insets.navigationBarsPadding
import sk.kasper.ui_common.theme.SpaceTheme
import sk.kasper.ui_common.ui.InsetAwareTopAppBar
import sk.kasper.ui_settings.R
import sk.kasper.ui_settings.SettingItem
import sk.kasper.ui_settings.SettingsSideEffect
import sk.kasper.ui_settings.SettingsViewModel

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SettingsScreen(
    showToast: (String) -> Unit,
    navigate: (String) -> Unit,
    navigateUp: () -> Unit
) {
    val settingsViewModel = hiltViewModel<SettingsViewModel>()
    LaunchedEffect(Unit) {
        settingsViewModel.sideEffects.collect {
            if (it == SettingsSideEffect.SHOW_RESTART_APP_TOAST) {
                showToast("Restart app to use selected api endpoint")
            }
        }
    }
    SpaceTheme {
        ProvideWindowInsets {
            Column {
                val state by settingsViewModel.state.collectAsState()
                TopAppBar(navigate, navigateUp)
                Surface(modifier = Modifier.navigationBarsPadding()) {
                    Preferences(list = state.settings, viewModel = settingsViewModel)
                }
            }
        }
    }
}

@ExperimentalMaterialApi
@Composable
private fun Preferences(list: List<SettingItem>, viewModel: SettingsViewModel) {
    Column {
        list.forEach {
            when (it) {
                is SettingItem.Category -> {
                    PreferenceCategory(title = it.title) {
                        Preferences(list = it.items, viewModel = viewModel)
                    }
                }
                is SettingItem.Choice -> {
                    ListPreference(
                        settingKey = it.key,
                        title = it.title,
                        values = it.values,
                        selectedValue = it.selected,
                        onValueChange = { key, value -> viewModel.setIntValue(key, value) }
                    )
                }
                is SettingItem.Switch -> {
                    SwitchPreference(
                        settingKey = it.key,
                        title = it.title,
                        summary = it.summary,
                        checked = it.checked,
                        onValueChange = { key, value -> viewModel.setBooleanValue(key, value) }
                    )
                }
            }
        }
    }
}

@Composable
private fun TopAppBar(navigate: (String) -> Unit = {}, navigateUp: () -> Unit) {
    InsetAwareTopAppBar(
        title = {
            Text(
                text = stringResource(id = R.string.settings),
                maxLines = 1,
                style = MaterialTheme.typography.h6,
                overflow = TextOverflow.Ellipsis,
            )
        },
        navigationIcon = {
            IconButton(onClick = navigateUp) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_arrow_back),
                    contentDescription = "back",
                )
            }
        },
        actions = {
            var expanded by remember { mutableStateOf(false) }
            IconButton(onClick = { expanded = true }) {
                Icon(
                    painterResource(id = R.drawable.ic_baseline_more_vert_24),
                    contentDescription = null
                )
            }

            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                DropdownMenuItem(onClick = { navigate("libraries") }) {
                    Text(stringResource(id = R.string.libraries))
                }
            }
        }
    )
}