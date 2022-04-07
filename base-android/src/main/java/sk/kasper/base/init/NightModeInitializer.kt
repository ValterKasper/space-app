package sk.kasper.base.init

import androidx.appcompat.app.AppCompatDelegate
import sk.kasper.base.SettingKey
import sk.kasper.base.SettingsManager
import javax.inject.Inject

class NightModeInitializer @Inject constructor(private val settingsManager: SettingsManager) : AppInitializer {

    override fun init() {
        AppCompatDelegate.setDefaultNightMode(settingsManager.nightMode)

        settingsManager.addSettingChangeListener { settingKey ->
            if (settingKey == SettingKey.NIGHT_MODE) {
                AppCompatDelegate.setDefaultNightMode(settingsManager.nightMode)
            }
        }
    }

}