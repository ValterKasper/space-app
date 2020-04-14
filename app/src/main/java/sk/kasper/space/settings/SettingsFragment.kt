package sk.kasper.space.settings

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import org.threeten.bp.LocalDateTime
import sk.kasper.domain.model.Rocket
import sk.kasper.space.BuildConfig
import sk.kasper.space.R
import sk.kasper.space.about.LibrariesActivity
import sk.kasper.space.analytics.Analytics
import sk.kasper.space.notification.LaunchNotificationInfo
import sk.kasper.space.notification.NotificationsHelper
import sk.kasper.space.utils.applySystemWindows
import sk.kasper.space.view.TopToolbar

class SettingsFragment : PreferenceFragmentCompat(), SharedPreferences.OnSharedPreferenceChangeListener {

    private lateinit var showBeforePreference: ListPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (BuildConfig.DEBUG) {

            // todo move to playground
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(view.findViewById<TopToolbar>(R.id.toolbar)) {
            inflateMenu(R.menu.menu_settings)
            setOnMenuItemClickListener(::onMenuItemClicked)
            setNavigationOnClickListener {
                parentFragmentManager.popBackStack()
            }
            applySystemWindows(this, applyLeft = true, applyTop = true, applyBottom = false, applyRight = true)
        }
        with(view.findViewById<View>(android.R.id.list_container)) {
            applySystemWindows(this, applyLeft = true, applyTop = false, applyBottom = true, applyRight = true)
        }
    }

    override fun onResume() {
        super.onResume()

        view?.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
    }

    private fun onMenuItemClicked(item: MenuItem): Boolean = when (item.itemId) {
        R.id.menu_libraries -> {
            startActivity(Intent(requireContext(), LibrariesActivity::class.java))
            requireActivity().overridePendingTransition(R.anim.enter_left, R.anim.exit_left)
            true
        }
        else -> false
    }

}