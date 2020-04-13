package sk.kasper.space.playground

import sk.kasper.space.settings.SettingsManager
import sk.kasper.space.utils.ObservableViewModel
import javax.inject.Inject

class PlaygroundViewModel @Inject constructor(private val settingsManager: SettingsManager): ObservableViewModel()