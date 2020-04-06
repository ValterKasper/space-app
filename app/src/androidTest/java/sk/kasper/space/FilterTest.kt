package sk.kasper.space

import org.junit.Test
import sk.kasper.domain.model.Tag.Companion.ISS
import sk.kasper.space.robot.droid.LaunchDroid
import sk.kasper.space.robot.timeline

class FilterTest : BaseMainActivityTest() {

    @Test
    fun filter() {
        timeline {
            fromServerReturnLaunches(listOf(
                    LaunchDroid("Electron", listOf(ISS)),
                    LaunchDroid("Ariane")))

            assertHasLaunch("Electron")
            assertHasLaunch("Ariane")

            openFilter()
            filterByTag("ISS")

            assertHasLaunch("Electron")
            assertHasNotLaunch("Ariane")

            clearFilter()

            assertHasLaunch("Electron")
            assertHasLaunch("Ariane")
        }
    }
}