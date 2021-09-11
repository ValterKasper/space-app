package sk.kasper.space.robot

import androidx.appcompat.app.AppCompatDelegate
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.platform.app.InstrumentationRegistry
import junit.framework.Assert.assertEquals
import org.hamcrest.CoreMatchers.containsString
import org.hamcrest.Matchers
import sk.kasper.space.R
/*

fun settings(func: SettingsRobot.() -> Unit) = SettingsRobot().apply { func() }

class SettingsRobot {

    enum class Theme {
        DARK,
        LIGHT
    }

    init {
        onView(Matchers.allOf(withText(R.string.settings)))
                        .check(ViewAssertions.matches(isDisplayed()))
    }

    fun assertHasNotificationsSection() {
        onView(withText(R.string.notifications))
                .check(ViewAssertions.matches(isDisplayed()))
    }

    fun selectTheme(theme: Theme) {
        onView(withText(R.string.choose_theme))
                .perform(ViewActions.click())

        val matcher = when (theme) {
            Theme.DARK -> containsString("Dark")
            Theme.LIGHT -> containsString("Light")
        }

        onData(matcher)
                .inAdapterView(withId(R.id.select_dialog_listview))
                .perform(ViewActions.click())

        val expectedNightMode = when (theme) {
            Theme.DARK -> AppCompatDelegate.MODE_NIGHT_YES
            Theme.LIGHT -> AppCompatDelegate.MODE_NIGHT_NO
        }

        assertEquals(expectedNightMode, AppCompatDelegate.getDefaultNightMode())
    }

    fun openAbout() {
        Espresso.openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getInstrumentation().targetContext)
        onView(withText(R.string.libraries))
                .perform(ViewActions.click())
    }

}*/
