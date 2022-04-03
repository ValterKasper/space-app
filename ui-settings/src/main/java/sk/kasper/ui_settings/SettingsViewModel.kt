package sk.kasper.ui_settings

import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import sk.kasper.base.SettingKey
import sk.kasper.base.SettingsManager
import sk.kasper.ui_common.viewmodel.ReducerViewModel
import javax.inject.Inject


@OptIn(ExperimentalStdlibApi::class)
@HiltViewModel
class SettingsViewModel @Inject constructor(private val settingsManager: SettingsManager) :
    ReducerViewModel<SettingsState, SettingsSideEffect>(SettingsState(emptyList())) {

    private fun createSettings() = Settings {
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

        if (BuildConfig.SHOW_API_ENDPOINTS_PREFERENCE) {
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
        reloadSettings()

        viewModelScope.launch {
            settingsManager.getSettingChanges().collect {
                onSettingChanged(it)
            }
        }
    }

    fun setBooleanValue(key: SettingKey, value: Boolean) = action {
        settingsManager.setBoolean(key, value)
    }

    fun setIntValue(key: SettingKey, value: Int) = action {
        settingsManager.setInt(key, value)
    }

    private fun reloadSettings() = action {
        reduce {
            copy(settings = createSettings())
        }
    }

    private fun onSettingChanged(settingKey: SettingKey) = action {
        reduce {
            copy(settings = createSettings())
        }

        emitSideEffect(SettingsSideEffect.SHOW_RESTART_APP_TOAST)
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

enum class SettingsSideEffect {
    SHOW_RESTART_APP_TOAST
}