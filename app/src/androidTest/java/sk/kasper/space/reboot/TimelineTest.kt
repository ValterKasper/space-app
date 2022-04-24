package sk.kasper.space.reboot

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import sk.kasper.base.SettingsManager
import sk.kasper.database.dao.LaunchSiteDao
import sk.kasper.space.MainActivity
import sk.kasper.space.fake.FakeRemoteApi
import javax.inject.Inject

@HiltAndroidTest
class TimelineTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Inject
    lateinit var getLaunchSiteDao: LaunchSiteDao

    @Inject
    lateinit var fakeRemoteApi: FakeRemoteApi

    @Inject
    lateinit var settingsManager: SettingsManager

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @Test
    fun example_test() {
        Thread.sleep(2000)
        composeTestRule.onNodeWithText("ISS").performClick()
    }
}