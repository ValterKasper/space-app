package sk.kasper.space.settings

import android.content.Context
import android.content.SharedPreferences
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.PreferenceManager
import sk.kasper.space.R
import javax.inject.Inject

enum class SettingKey(@StringRes val stringRes: Int) {
    NIGHT_MODE(R.string.pref_night_mode),
    NIGHT_MODE_PRE_Q(R.string.pref_night_pre_q_mode),
    SHOW_UNCONFIRMED_LAUNCHES(R.string.pref_show_unconfirmed_launches),
    DURATION_BEFORE_NOTIFICATION_IS_SHOWN(R.string.pref_duration_before_notification_is_shown),
    SHOW_LAUNCH_NOTIFICATION(R.string.pref_show_launch_notifications),
    INVALID(0)
}

typealias SettingChangeListener = (SettingKey) -> Unit

open class SettingsManager @Inject constructor(private val context: Context) : SharedPreferences.OnSharedPreferenceChangeListener {

    enum class ApiEndpoint {
        PRODUCTION,
        LOCALHOST,
        RASPBERRY
    }

    private val sharedPreferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

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

    fun addSettingChangeListener(listener: SettingChangeListener) {
        listeners.add(listener)
    }

    fun removeSettingChangeListener(listener: SettingChangeListener) {
        listeners.remove(listener)
    }

    open val showUnconfirmedLaunches: Boolean
        get() = getBoolean(SettingKey.SHOW_UNCONFIRMED_LAUNCHES, true)

    open val showLaunchNotifications: Boolean
        get() = getBoolean(SettingKey.SHOW_LAUNCH_NOTIFICATION, true)

    open val durationBeforeNotificationIsShown: Int
        get() = getIntFromString(SettingKey.DURATION_BEFORE_NOTIFICATION_IS_SHOWN, 60)

    open var nightMode: Int
        get() = if (android.os.Build.VERSION.SDK_INT <= android.os.Build.VERSION_CODES.P) {
            getIntFromString(SettingKey.NIGHT_MODE_PRE_Q, AppCompatDelegate.MODE_NIGHT_NO)
        } else {
            getIntFromString(SettingKey.NIGHT_MODE, AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        }
        set(value) {
            val key = if (android.os.Build.VERSION.SDK_INT <= android.os.Build.VERSION_CODES.P) {
                SettingKey.NIGHT_MODE_PRE_Q
            } else {
                SettingKey.NIGHT_MODE
            }
            setIntToString(key, value)
        }

    val apiEndpoint: ApiEndpoint
        get() = when (sharedPreferences.getString(context.getString(R.string.pref_api_endpoint), "0")) {
            "0" -> ApiEndpoint.PRODUCTION
            "1" -> ApiEndpoint.LOCALHOST
            "2" -> ApiEndpoint.RASPBERRY
            else -> ApiEndpoint.PRODUCTION
        }

    private fun getBoolean(key: SettingKey, defaultValue: Boolean): Boolean {
        return sharedPreferences.getBoolean(getSharedPreferenceKeyFromSettingKey(key), defaultValue)
    }

    private fun getIntFromString(key: SettingKey, defaultValue: Int): Int {
        return sharedPreferences.getString(getSharedPreferenceKeyFromSettingKey(key), defaultValue.toString()).toInt()
    }

    private fun setIntToString(key: SettingKey, value: Int) {
        return sharedPreferences
                .edit()
                .putString(getSharedPreferenceKeyFromSettingKey(key), value.toString())
                .apply()
    }

    private fun getSettingKeyFromSharedPreferenceKey(key: String): SettingKey {
        return when (context.resources.getIdentifier(key, "string", context.packageName)) {
            R.string.pref_night_mode -> SettingKey.NIGHT_MODE
            R.string.pref_night_pre_q_mode -> SettingKey.NIGHT_MODE_PRE_Q
            R.string.pref_show_unconfirmed_launches -> SettingKey.SHOW_UNCONFIRMED_LAUNCHES
            R.string.pref_duration_before_notification_is_shown -> SettingKey.DURATION_BEFORE_NOTIFICATION_IS_SHOWN
            R.string.pref_show_launch_notifications -> SettingKey.SHOW_LAUNCH_NOTIFICATION
            else -> SettingKey.INVALID
        }
    }

    private fun getSharedPreferenceKeyFromSettingKey(key: SettingKey): String {
        return context.getString(key.stringRes)
    }

}