package sk.kasper.space.settings

import android.content.SharedPreferences
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.google.android.material.appbar.MaterialToolbar
import sk.kasper.space.BuildConfig
import sk.kasper.space.R
import sk.kasper.space.analytics.Analytics
import sk.kasper.space.utils.applySystemWindows

class SettingsFragment : PreferenceFragmentCompat(), SharedPreferences.OnSharedPreferenceChangeListener {

    private lateinit var showBeforePreference: ListPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!BuildConfig.DEBUG) {
            preferenceManager.findPreference<Preference>(getString(R.string.pref_debug_category))?.isVisible = false
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

        with(view.findViewById<MaterialToolbar>(R.id.toolbar)) {
            inflateMenu(R.menu.menu_settings)
            setOnMenuItemClickListener(::onMenuItemClicked)
            NavigationUI.setupWithNavController(this, findNavController())
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
            findNavController().navigate(R.id.action_settingsFragment_to_librariesFragment)
            true
        }
        else -> false
    }

}