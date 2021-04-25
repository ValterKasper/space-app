package sk.kasper.ui_common.utils.backpress

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BackPressManager @Inject constructor() {

    private val listeners: MutableList<OnBackPressListener> = mutableListOf()

    fun addListener(listener: OnBackPressListener) {
        listeners.add(listener)
    }

    fun removeListener(listener: OnBackPressListener) {
        listeners.remove(listener)
    }

    fun onBackPress(): Boolean = listeners.any { it.onBackPress() }

}