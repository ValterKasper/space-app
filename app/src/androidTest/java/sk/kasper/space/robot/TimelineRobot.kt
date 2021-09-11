package sk.kasper.space.robot

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.CoreMatchers.containsString
import sk.kasper.space.R
/*

fun timeline(func: TimelineRobot.() -> Unit) = TimelineRobot().apply { func() }

class TimelineRobot {

    fun openFilter() {
        onView(allOf(withId(R.id.menu_filter), withContentDescription("Filter")))
                .perform(click())
    }

    fun clearFilter() {
        onView(withId(R.id.clear_filters))
                .perform(click())
    }

    fun filterByTag(tag: String) {
        onView(allOf(withId(R.id.filterCheckbox), hasSibling(hasDescendant(withText(tag)))))
                .perform(click())
    }

    fun openLaunchWithName(name: String): LaunchDetailRobot {
        onView(withText(containsString(name)))
                .perform(click())

        return LaunchDetailRobot()
    }

    fun openSettings() {
        openActionBarOverflowOrOptionsMenu(getInstrumentation().targetContext)
        onView(withText("Settings"))
                .perform(click())
    }

    infix fun openFirstLaunch(func: LaunchDetailRobot.() -> Unit): LaunchDetailRobot {
        onView(withId(R.id.launchesRecyclerView))
                                .perform(actionOnItemAtPosition<RecyclerView.ViewHolder>(1, click()))

        return LaunchDetailRobot().apply { func() }
    }

    fun assertHasHeader(headerName: String) {
        onView(withText(headerName))
                .check(matches(isDisplayed()))
    }

    fun assertHasLaunch(launchName: String) {
        onView(withText(launchName))
                .check(matches(isDisplayed()))
    }

    fun assertHasNotLaunch(launchName: String) {
        onView(withText(launchName))
                .check(doesNotExist())
    }

}*/
