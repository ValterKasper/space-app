package sk.kasper.ui_settings

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.insets.navigationBarsPadding
import dagger.hilt.android.AndroidEntryPoint
import sk.kasper.base.SettingKey
import sk.kasper.base.SettingsManager
import sk.kasper.ui_common.BaseFragment
import sk.kasper.ui_common.theme.SpaceTheme
import sk.kasper.ui_common.ui.InsetAwareTopAppBar
import sk.kasper.ui_common.utils.createSlideAnimNavOptions
import sk.kasper.ui_settings.preferences.ListPreference
import sk.kasper.ui_settings.preferences.PreferenceCategory
import sk.kasper.ui_settings.preferences.SwitchPreference
import javax.inject.Inject

@AndroidEntryPoint
class SettingsFragment : BaseFragment() {

    @Inject
    lateinit var settingsManager: SettingsManager

    @OptIn(ExperimentalMaterialApi::class)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        lifecycleScope.launchWhenStarted {
            settingsManager.getIntAsFlow(SettingKey.API_ENDPOINT).collect {
                Toast.makeText(
                    requireContext(),
                    "Restart app to use selected api endpoint",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        val viewModel: SettingsViewModel by viewModels()

        return ComposeView(requireContext()).apply {
            setContent {
                SpaceTheme {
                    ProvideWindowInsets {
                        Column {
                            val state by viewModel.state.collectAsState()
                            TopAppBar {
                                openLibraries()
                            }
                            Surface(modifier = Modifier.navigationBarsPadding()) {
                                Preferences(list = state.settings, viewModel = viewModel)
                            }
                        }
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

    private fun openLibraries() {
        val uri = Uri.parse("spaceapp://libraries")
        findNavController().navigate(uri, createSlideAnimNavOptions())
    }

    @Composable
    private fun TopAppBar(onShowLibraries: () -> Unit = {}) {
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
                IconButton(onClick = { findNavController().popBackStack() }) {
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
                    DropdownMenuItem(onClick = { onShowLibraries() }) {
                        Text(stringResource(id = R.string.libraries))
                    }
                }
            }
        )
    }


}