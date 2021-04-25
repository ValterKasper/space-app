package sk.kasper.space.playground

import sk.kasper.ui_common.settings.SettingsManager
import sk.kasper.ui_common.utils.ObservableViewModel
import javax.inject.Inject

class PlaygroundViewModel @Inject constructor(private val settingsManager: SettingsManager): ObservableViewModel()