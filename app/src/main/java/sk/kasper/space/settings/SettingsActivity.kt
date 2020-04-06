package sk.kasper.space.settings

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import kotlinx.android.synthetic.main.activity_settings.*
import org.threeten.bp.LocalDateTime
import sk.kasper.domain.model.Rocket
import sk.kasper.space.BuildConfig
import sk.kasper.space.R
import sk.kasper.space.about.LibrariesActivity
import sk.kasper.space.analytics.Analytics
import sk.kasper.space.notification.LaunchNotificationInfo
import sk.kasper.space.notification.NotificationsHelper


// todo netreba aby boli v aktivite
class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        with(toolbar) {
            inflateMenu(R.menu.menu_settings)
            setOnMenuItemClickListener(::onMenuItemClicked)
            setNavigationOnClickListener {
                finish()
            }
        }
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.enter_right, R.anim.exit_right)
    }

    private fun onMenuItemClicked(item: MenuItem): Boolean = when (item.itemId) {
        R.id.menu_libraries -> {
            startActivity(Intent(this, LibrariesActivity::class.java))
            overridePendingTransition(R.anim.enter_left, R.anim.exit_left)
            true
        }
        else -> false
    }

    class SettingsFragment : PreferenceFragmentCompat(), SharedPreferences.OnSharedPreferenceChangeListener {

        private lateinit var showBeforePreference: ListPreference

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)

            if (BuildConfig.DEBUG) {
                preferenceManager.findPreference<Preference>(getString(R.string.pref_debug_show_demo_notification))?.setOnPreferenceClickListener {
                    NotificationsHelper(requireContext()).showLaunchNotification(LaunchNotificationInfo(
                            99L,
                            Rocket.FALCON_HEAVY,
                            "Falcon Heavy",
                            "Some video url",
                            "Arabsat-6A",
                            LocalDateTime.now().plusMinutes(30)
                    ))
                    true
                }
            } else {
                preferenceManager.findPreference<Preference>(getString(R.string.pref_debug_category))?.isVisible = false
                preferenceManager.findPreference<Preference>(getString(R.string.pref_debug_show_demo_notification))?.isVisible = false
                preferenceManager.findPreference<Preference>(getString(R.string.pref_api_endpoint))?.isVisible = false
            }

            if (android.os.Build.VERSION.SDK_INT <= android.os.Build.VERSION_CODES.P) {
                preferenceManager.findPreference<Preference>(getString(R.string.pref_night_mode))?.isVisible = false
            } else {
                preferenceManager.findPreference<Preference>(getString(R.string.pref_night_pre_q_mode))?.isVisible = false
            }

            showBeforePreference = preferenceManager.findPreference<Preference>(getString(R.string.pref_duration_before_notification_is_shown)) as ListPreference
            preferenceManager.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
            updateDurationToLaunchSummary()
        }

        override fun onDestroy() {
            super.onDestroy()
            preferenceManager.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
        }

        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            addPreferencesFromResource(R.xml.settings)
        }

        override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {
            when (key) {
                getString(R.string.pref_duration_before_notification_is_shown) -> {
                    updateDurationToLaunchSummary()

                    Analytics.log(
                            Analytics.Event.SETTING_SHOW_BEFORE,
                            mapOf(Analytics.Param.VALUE to showBeforePreference.value))
                }
                getString(R.string.pref_api_endpoint) -> Toast.makeText(requireContext(), "Restart app to use selected api endpoint", Toast.LENGTH_SHORT).show()
            }
        }

        private fun updateDurationToLaunchSummary() {
            showBeforePreference.summary = showBeforePreference.entry
        }
        
    }
}
