package sk.kasper.ui_settings

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.layout.Column
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.insets.navigationBarsPadding
import kotlinx.coroutines.flow.collect
import sk.kasper.ui_common.BaseFragment
import sk.kasper.ui_common.settings.SettingKey
import sk.kasper.ui_common.settings.SettingsManager
import sk.kasper.ui_common.theme.SpaceTheme
import sk.kasper.ui_common.ui.InsetAwareTopAppBar
import sk.kasper.ui_common.utils.createSlideAnimNavOptions
import sk.kasper.ui_settings.preferences.ListPreference
import sk.kasper.ui_settings.preferences.PreferenceCategory
import sk.kasper.ui_settings.preferences.ProvideSettingsManager
import sk.kasper.ui_settings.preferences.SwitchPreference
import javax.inject.Inject

class SettingsFragment : BaseFragment() {

    @Inject
    lateinit var settingsManager: SettingsManager

    @ExperimentalMaterialApi
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val endpointValues = settingsManager.apiEndPointValues.associateWith {
            mapOf(
                SettingsManager.PRODUCTION to R.string.production,
                SettingsManager.LOCALHOST to R.string.localhost,
                SettingsManager.RASPBERRY to R.string.raspberry,
            ).getValue(it)
        }
        val durationValues = settingsManager.durationValues.associateWith {
            mapOf(
                30 to R.string.half_hour,
                60 to R.string.one_hour,
                120 to R.string.two_hours,
            ).getValue(it)
        }
        val themeValues: Map<Int, Int> = settingsManager.nightModeValues.associateWith {
            mapOf(
                AppCompatDelegate.MODE_NIGHT_NO to R.string.light,
                AppCompatDelegate.MODE_NIGHT_YES to R.string.dark,
                AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM to R.string.follow_system
            ).getValue(it)
        }

        lifecycleScope.launchWhenStarted {
            settingsManager.getIntAsFlow(SettingKey.API_ENDPOINT).collect {
                Toast.makeText(
                    requireContext(),
                    "Restart app to use selected api endpoint",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        return ComposeView(requireContext()).apply {
            setContent {
                SpaceTheme {
                    ProvideWindowInsets {
                        Column {
                            TopAppBar {
                                openLibraries()
                            }
                            ProvideSettingsManager(settingsManager = settingsManager) {
                                Column(
                                    modifier = Modifier.navigationBarsPadding()
                                ) {

                                    ListPreference(
                                        settingKey = SettingKey.NIGHT_MODE,
                                        title = R.string.choose_theme,
                                        values = themeValues
                                    )

                                    SwitchPreference(
                                        settingKey = SettingKey.SHOW_UNCONFIRMED_LAUNCHES,
                                        title = R.string.show_unconfirmed_launches,
                                        summary = R.string.show_unconfirmed_launches_summary
                                    )

                                    PreferenceCategory(title = R.string.notifications) {
                                        SwitchPreference(
                                            settingKey = SettingKey.SHOW_LAUNCH_NOTIFICATION,
                                            title = R.string.show_launch_notifications,
                                            summary = R.string.show_launch_notifications_summary
                                        )

                                        ListPreference(
                                            settingKey = SettingKey.DURATION_BEFORE_NOTIFICATION_IS_SHOWN,
                                            title = R.string.show_launch_notifications_before,
                                            values = durationValues
                                        )
                                    }

                                    if (BuildConfig.DEBUG) {
                                        PreferenceCategory(title = R.string.debug) {
                                            ListPreference(
                                                settingKey = SettingKey.API_ENDPOINT,
                                                title = R.string.api_endpoint,
                                                values = endpointValues
                                            )
                                        }
                                    }
                                }
                            }
                        }
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