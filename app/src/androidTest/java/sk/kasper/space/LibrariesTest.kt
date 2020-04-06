package sk.kasper.space

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import sk.kasper.space.robot.libraries
import sk.kasper.space.robot.settings
import sk.kasper.space.robot.timeline


@RunWith(AndroidJUnit4::class)
class LibrariesTest : BaseMainActivityTest() {

    @Test
    fun openLibraries() {
        timeline {
            fromServerReturnLaunches(emptyList())
            openSettings()
        }

        settings {
            openAbout()
        }

        libraries {
            assertHasLibraries()
        }
    }
}