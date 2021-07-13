package sk.kasper.ui_common.settings

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.PreferenceManager
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.sendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject
import javax.inject.Singleton

enum class SettingKey(val key: String) {
    NIGHT_MODE(if (isLowerSdkVersionThan(Build.VERSION_CODES.Q)) "pref_night_pre_q_mode" else "pref_night_mode"),
    SHOW_UNCONFIRMED_LAUNCHES("pref_show_unconfirmed_launches"),
    DURATION_BEFORE_NOTIFICATION_IS_SHOWN("pref_duration_before_notification_is_shown"),
    SHOW_LAUNCH_NOTIFICATION("pref_show_launch_notifications"),
    API_ENDPOINT("pref_api_endpoint"),
    INVALID("pref_invalid")
}

private fun isLowerSdkVersionThan(sdkVersion: Int) =
    Build.VERSION.SDK_INT < sdkVersion

typealias SettingChangeListener = (SettingKey) -> Unit

@Singleton
class SettingsManager @Inject constructor(@ApplicationContext private val context: Context) :
    SharedPreferences.OnSharedPreferenceChangeListener {

    companion object {
        const val PRODUCTION = 0
        const val LOCALHOST = 1
        const val RASPBERRY = 2
    }

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

    fun getSettingChanges(): Flow<SettingKey> = callbackFlow {
        val listener: (SettingKey) -> Unit = { c ->
            sendBlocking(c)
        }

        addSettingChangeListener(listener)

        awaitClose { removeSettingChangeListener(listener) }
    }

    fun getIntAsFlow(key: SettingKey): Flow<Int> = callbackFlow {
        val listener: (SettingKey) -> Unit = { c ->
            if (c == key) sendBlocking(getInt(key))
        }

        addSettingChangeListener(listener)

        awaitClose { removeSettingChangeListener(listener) }
    }

    fun getBoolAsFlow(key: SettingKey): Flow<Boolean> = callbackFlow {
        val listener: (SettingKey) -> Unit = { c ->
            if (c == key) sendBlocking(getBoolean(key))
        }

        addSettingChangeListener(listener)

        awaitClose { removeSettingChangeListener(listener) }
    }

    fun addSettingChangeListener(listener: SettingChangeListener) {
        listeners.add(listener)
    }

    fun removeSettingChangeListener(listener: SettingChangeListener) {
        listeners.remove(listener)
    }

    val showUnconfirmedLaunches: Boolean
        get() = getBoolean(SettingKey.SHOW_UNCONFIRMED_LAUNCHES)

    var showLaunchNotifications: Boolean
        get() = getBoolean(SettingKey.SHOW_LAUNCH_NOTIFICATION)
        set(value) = setBoolean(SettingKey.SHOW_LAUNCH_NOTIFICATION, value)

    val durationBeforeNotificationIsShown: Int
        get() = getInt(SettingKey.DURATION_BEFORE_NOTIFICATION_IS_SHOWN)

    var nightMode: Int
        get() = getInt(SettingKey.NIGHT_MODE)
        set(value) {
            setInt(SettingKey.NIGHT_MODE, value)
        }

    val apiEndpoint: Int
        get() = getInt(SettingKey.API_ENDPOINT)

    val apiEndPointValues = listOf(PRODUCTION, RASPBERRY, LOCALHOST)

    fun toggleTheme() {
        nightMode = if (nightMode == AppCompatDelegate.MODE_NIGHT_NO) {
            AppCompatDelegate.MODE_NIGHT_YES
        } else {
            AppCompatDelegate.MODE_NIGHT_NO
        }
    }

    val durationValues = listOf(30, 60, 120)

    val nightModeValues: List<Int>
        get() = if (isLowerSdkVersionThan(Build.VERSION_CODES.Q)) {
            listOf(AppCompatDelegate.MODE_NIGHT_NO, AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            listOf(
                AppCompatDelegate.MODE_NIGHT_NO,
                AppCompatDelegate.MODE_NIGHT_YES,
                AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
            )
        }

    fun getBoolean(key: SettingKey): Boolean {
        return sharedPreferences
            .getBoolean(key.key, defaultValues[key] as Boolean)
    }

    fun setBoolean(key: SettingKey, value: Boolean) {
        sharedPreferences.edit()
            .putBoolean(key.key, value)
            .apply()
    }

    fun getInt(key: SettingKey): Int {
        return sharedPreferences.getString(
            key.key,
            defaultValues[key].toString()
        )!!.toInt()
    }

    fun setInt(key: SettingKey, value: Int) {
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