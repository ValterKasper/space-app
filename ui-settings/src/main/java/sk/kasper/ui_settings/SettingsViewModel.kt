package sk.kasper.ui_settings

import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import sk.kasper.ui_common.settings.SettingKey
import sk.kasper.ui_common.settings.SettingsManager
import sk.kasper.ui_common.viewmodel.ReducerViewModel
import javax.inject.Inject

class SettingsViewModel @Inject constructor(private val settingsManager: SettingsManager) :
    ReducerViewModel<SettingsState, SettingsAction, SettingsSideEffect>(
        SettingsState(emptyList())
    ) {

    @ExperimentalStdlibApi
    private fun createSettingItems(): List<SettingItem> {
        return Settings {
            Choice(
                key = SettingKey.NIGHT_MODE,
                title = R.string.choose_theme,
                values = themeValues,
                selected = settingsManager.getInt(SettingKey.NIGHT_MODE),
            )

            Switch(
                key = SettingKey.SHOW_UNCONFIRMED_LAUNCHES,
                checked = settingsManager.getBoolean(SettingKey.SHOW_UNCONFIRMED_LAUNCHES),
                title = R.string.show_unconfirmed_launches,
                summary = R.string.show_unconfirmed_launches_summary
            )

            Category(title = R.string.notifications) {
                Switch(
                    key = SettingKey.SHOW_LAUNCH_NOTIFICATION,
                    title = R.string.show_launch_notifications,
                    checked = settingsManager.getBoolean(SettingKey.SHOW_LAUNCH_NOTIFICATION),
                    summary = R.string.show_launch_notifications_summary
                )

                Choice(
                    key = SettingKey.DURATION_BEFORE_NOTIFICATION_IS_SHOWN,
                    title = R.string.show_launch_notifications_before,
                    values = durationValues,
                    selected = settingsManager.getInt(SettingKey.DURATION_BEFORE_NOTIFICATION_IS_SHOWN),
                )
            }

            if (BuildConfig.DEBUG) {
                Category(title = R.string.debug) {
                    Choice(
                        key = SettingKey.API_ENDPOINT,
                        title = R.string.api_endpoint,
                        values = endpointValues,
                        selected = settingsManager.getInt(SettingKey.API_ENDPOINT)
                    )
                }
            }
        }
    }

    private val themeValues: Map<Int, Int> = settingsManager.nightModeValues.associateWith {
        mapOf(
            AppCompatDelegate.MODE_NIGHT_NO to R.string.light,
            AppCompatDelegate.MODE_NIGHT_YES to R.string.dark,
            AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM to R.string.follow_system
        ).getValue(it)
    }

    private val endpointValues = settingsManager.apiEndPointValues.associateWith {
        mapOf(
            SettingsManager.PRODUCTION to R.string.production,
            SettingsManager.LOCALHOST to R.string.localhost,
            SettingsManager.RASPBERRY to R.string.raspberry,
        ).getValue(it)
    }

    private val durationValues = settingsManager.durationValues.associateWith {
        mapOf(
            30 to R.string.half_hour,
            60 to R.string.one_hour,
            120 to R.string.two_hours,
        ).getValue(it)
    }

    init {
        submitAction(SettingsAction.ReloadSettings)

        viewModelScope.launch {
            settingsManager.getSettingChanges().collect {
                submitAction(SettingsAction.SettingChanged(it))
            }
        }
    }

    @ExperimentalStdlibApi
    override fun ScanScope.scan(action: SettingsAction, oldState: SettingsState): SettingsState {
        return when (action) {
            is SettingsAction.SettingChanged -> {
                emitSideEffect(SettingsSideEffect.SHOW_RESTART_APP_TOAST)
                SettingsState(createSettingItems())
            }
            SettingsAction.ReloadSettings -> {
                SettingsState(createSettingItems())
            }
            else -> oldState
        }
    }

    override fun mapActionToActionFlow(action: SettingsAction): Flow<SettingsAction> {
        return when (action) {
            is SettingsAction.SetBooleanValue -> {
                settingsManager.setBoolean(action.key, action.value)
                flowOf()
            }
            is SettingsAction.SetIntValue -> {
                settingsManager.setInt(action.key, action.value)
                flowOf()
            }
            else -> super.mapActionToActionFlow(action)
        }
    }

    private fun Settings(buildingBlock: SettingsBuilder.() -> Unit): List<SettingItem> {
        return SettingsBuilder().apply {
            buildingBlock()
        }.items
    }

    private inner class SettingsBuilder {
        private var _items: MutableList<SettingItem> = mutableListOf()
        val items: List<SettingItem> = _items

        fun Switch(
            key: SettingKey,
            @StringRes title: Int,
            summary: Int,
            checked: Boolean
        ) {
            _items.add(SettingItem.Switch(key, title, summary, checked))
        }

        fun Choice(
            @StringRes title: Int,
            key: SettingKey,
            values: Map<Int, Int>,
            selected: Int
        ) {
            _items.add(SettingItem.Choice(title, key, values, selected))
        }

        @ExperimentalStdlibApi
        fun Category(@StringRes title: Int, buildingBlock: SettingsBuilder.() -> Unit) {
            _items.add(
                SettingItem.Category(
                    items = Settings(buildingBlock),
                    title = title
                )
            )
        }
    }
}

data class SettingsState(val settings: List<SettingItem>)

sealed class SettingItem {
    data class Category(@StringRes val title: Int, val items: List<SettingItem>) :
        SettingItem()

    data class Choice(
        @StringRes val title: Int,
        val key: SettingKey,
        val values: Map<Int, Int>,
        val selected: Int,
    ) : SettingItem()

    data class Switch(
        val key: SettingKey = SettingKey.INVALID,
        @StringRes val title: Int,
        val summary: Int = 0,
        val checked: Boolean = false,
    ) : SettingItem()
}

sealed class SettingsAction {
    object ReloadSettings : SettingsAction()
    data class SettingChanged(val settingKey: SettingKey) : SettingsAction()
    data class SetIntValue(val key: SettingKey, val value: Int) : SettingsAction()
    data class SetBooleanValue(val key: SettingKey, val value: Boolean) : SettingsAction()
}

enum class SettingsSideEffect {
    SHOW_RESTART_APP_TOAST
}