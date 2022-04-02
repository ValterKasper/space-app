package sk.kasper.space.utils

import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert.assertThat
import org.junit.Test
import sk.kasper.entity.utils.toTimeStamp


class DateUtilsTest {

    @Test
    fun toTimeStamp() {
        assertThat("June 4, 2010 18:45:00 UTC".toTimeStamp(), `is`(1275677100000L))
    }

    @Test
    fun toTimeStamp_nonSenseValue_wontCrash() {
        assertThat("Nonsense value xyz".toTimeStamp(), `is`(0L))
    }

}