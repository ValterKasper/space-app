package sk.kasper.space.robot

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withText
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.containsString
/*

fun launchDetail(func: LaunchDetailRobot.() -> Unit) = LaunchDetailRobot().apply { func() }

class LaunchDetailRobot {

    fun assertLaunchName(name: String) {
        val textView = onView(
                allOf(
                        withText(containsString(name)),
                        isDisplayed()))
        textView.check(matches(withText(containsString(name))))
    }

}*/
