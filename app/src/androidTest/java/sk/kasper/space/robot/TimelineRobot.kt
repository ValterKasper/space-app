package sk.kasper.space.robot

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
