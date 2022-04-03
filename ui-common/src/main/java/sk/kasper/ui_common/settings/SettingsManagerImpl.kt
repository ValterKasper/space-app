package sk.kasper.ui_common.settings

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.PreferenceManager
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import sk.kasper.base.SettingChangeListener
import sk.kasper.base.SettingKey
import sk.kasper.base.SettingsManager
import sk.kasper.base.SettingsManager.Companion.LOCALHOST
import sk.kasper.base.SettingsManager.Companion.PRODUCTION
import sk.kasper.base.SettingsManager.Companion.RASPBERRY
import javax.inject.Inject
import javax.inject.Singleton

private fun isLowerSdkVersionThan(sdkVersion: Int) =
    Build.VERSION.SDK_INT < sdkVersion

@Singleton
internal class SettingsManagerImpl @Inject constructor(@ApplicationContext private val context: Context) :
    SharedPreferences.OnSharedPreferenceChangeListener, SettingsManager {

    private val sharedPreferences: SharedPreferences =
        PreferenceManager.getDefaultSharedPreferences(context)

    private val listeners = mutableListOf<SettingChangeListener>()

    init {
        sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {
        val settingKey = getSettingKeyFromSharedPreferenceKey(key)
        if (settingKey != SettingKey.INVALID) {
            listeners.forEach {
                it(settingKey)
            }
        }
    }

    override fun getSettingChanges(): Flow<SettingKey> = callbackFlow {
        val listener: (SettingKey) -> Unit = { c ->
            trySendBlocking(c)
        }

        addSettingChangeListener(listener)

        awaitClose { removeSettingChangeListener(listener) }
    }

    override fun getIntAsFlow(key: SettingKey): Flow<Int> = callbackFlow {
        val listener: (SettingKey) -> Unit = { c ->
            if (c == key) trySendBlocking(getInt(key))
        }

        addSettingChangeListener(listener)

        awaitClose { removeSettingChangeListener(listener) }
    }

    override fun getBoolAsFlow(key: SettingKey): Flow<Boolean> = callbackFlow {
        val listener: (SettingKey) -> Unit = { c ->
            if (c == key) trySendBlocking(getBoolean(key))
        }

        addSettingChangeListener(listener)

        awaitClose { removeSettingChangeListener(listener) }
    }

    override fun addSettingChangeListener(listener: SettingChangeListener) {
        listeners.add(listener)
    }

    override fun removeSettingChangeListener(listener: SettingChangeListener) {
        listeners.remove(listener)
    }

    override val showUnconfirmedLaunches: Boolean
        get() = getBoolean(SettingKey.SHOW_UNCONFIRMED_LAUNCHES)

    override var showLaunchNotifications: Boolean
        get() = getBoolean(SettingKey.SHOW_LAUNCH_NOTIFICATION)
        set(value) = setBoolean(SettingKey.SHOW_LAUNCH_NOTIFICATION, value)

    override val durationBeforeNotificationIsShown: Int
        get() = getInt(SettingKey.DURATION_BEFORE_NOTIFICATION_IS_SHOWN)

    override var nightMode: Int
        get() = getInt(SettingKey.NIGHT_MODE)
        set(value) {
            setInt(SettingKey.NIGHT_MODE, value)
        }

    override val apiEndpoint: Int
        get() = getInt(SettingKey.API_ENDPOINT)

    override val apiEndPointValues = listOf(PRODUCTION, RASPBERRY, LOCALHOST)

    override fun toggleTheme() {
        nightMode = if (nightMode == AppCompatDelegate.MODE_NIGHT_NO) {
            AppCompatDelegate.MODE_NIGHT_YES
        } else {
            AppCompatDelegate.MODE_NIGHT_NO
        }
    }

    override val durationValues = listOf(30, 60, 120)

    override val nightModeValues: List<Int>
        get() = if (isLowerSdkVersionThan(Build.VERSION_CODES.Q)) {
            listOf(AppCompatDelegate.MODE_NIGHT_NO, AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            listOf(
                AppCompatDelegate.MODE_NIGHT_NO,
                AppCompatDelegate.MODE_NIGHT_YES,
                AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
            )
        }

    override fun getBoolean(key: SettingKey): Boolean {
        return sharedPreferences
            .getBoolean(key.key, defaultValues[key] as Boolean)
    }

    override fun setBoolean(key: SettingKey, value: Boolean) {
        sharedPreferences.edit()
            .putBoolean(key.key, value)
            .apply()
    }

    override fun getInt(key: SettingKey): Int {
        return sharedPreferences.getString(
            key.key,
            defaultValues[key].toString()
        )!!.toInt()
    }

    override fun setInt(key: SettingKey, value: Int) {
        return sharedPreferences
            .edit()
            .putString(key.key, value.toString())
            .apply()
    }

    private val defaultValues: Map<SettingKey, Any> = mapOf(
        SettingKey.NIGHT_MODE to if (isLowerSdkVersionThan(Build.VERSION_CODES.Q)) AppCompatDelegate.MODE_NIGHT_NO else AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM,
        SettingKey.SHOW_UNCONFIRMED_LAUNCHES to true,
        SettingKey.SHOW_LAUNCH_NOTIFICATION to true,
        SettingKey.DURATION_BEFORE_NOTIFICATION_IS_SHOWN to 60,
        SettingKey.API_ENDPOINT to PRODUCTION,
        SettingKey.LAUNCHES_FETCHED_ALREADY to false,
    )

    private fun getSettingKeyFromSharedPreferenceKey(key: String): SettingKey {
        return when (key) {
            "pref_night_mode", "pref_night_pre_q_mode" -> SettingKey.NIGHT_MODE
            "pref_show_unconfirmed_launches" -> SettingKey.SHOW_UNCONFIRMED_LAUNCHES
            "pref_duration_before_notification_is_shown" -> SettingKey.DURATION_BEFORE_NOTIFICATION_IS_SHOWN
            "pref_show_launch_notifications" -> SettingKey.SHOW_LAUNCH_NOTIFICATION
            "pref_api_endpoint" -> SettingKey.API_ENDPOINT
            else -> SettingKey.INVALID
        }
    }

}