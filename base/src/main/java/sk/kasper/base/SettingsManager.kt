package sk.kasper.base

import kotlinx.coroutines.flow.Flow

interface SettingsManager {

    companion object {
        const val PRODUCTION = 0
        const val LOCALHOST = 1
        const val RASPBERRY = 2
    }

    val showUnconfirmedLaunches: Boolean
    var showLaunchNotifications: Boolean
    val durationBeforeNotificationIsShown: Int
    var nightMode: Int
    val apiEndpoint: Int
    val apiEndPointValues: List<Int>
    val durationValues: List<Int>
    val nightModeValues: List<Int>
    fun getSettingChanges(): Flow<SettingKey>
    fun getIntAsFlow(key: SettingKey): Flow<Int>
    fun getBoolAsFlow(key: SettingKey): Flow<Boolean>
    fun addSettingChangeListener(listener: SettingChangeListener)
    fun removeSettingChangeListener(listener: SettingChangeListener)
    fun toggleTheme()
    fun getBoolean(key: SettingKey): Boolean
    fun setBoolean(key: SettingKey, value: Boolean)
    fun getInt(key: SettingKey): Int
    fun setInt(key: SettingKey, value: Int)
}