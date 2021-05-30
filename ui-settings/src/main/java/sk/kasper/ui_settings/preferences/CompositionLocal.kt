package sk.kasper.ui_settings.preferences

import androidx.compose.runtime.*
import sk.kasper.ui_common.settings.SettingsManager

val LocalSettingsManager: ProvidableCompositionLocal<SettingsManager> =
    compositionLocalOf(structuralEqualityPolicy()) { error("No settings manager found") }

@Composable
fun ProvideSettingsManager(
    settingsManager: SettingsManager,
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(LocalSettingsManager provides settingsManager) {
        content()
    }
}