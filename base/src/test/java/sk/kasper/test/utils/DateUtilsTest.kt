package sk.kasper.test.utils

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import sk.kasper.base.utils.toTimeStamp


class DateUtilsTest {

    @Test
    fun toTimeStamp() {
        assertThat("June 4, 2010 18:45:00 UTC".toTimeStamp())
            .isEqualTo(1275677100000L)
    }

    @Test
    fun toTimeStamp_nonSenseValue_wontCrash() {
        assertThat("Nonsense value xyz".toTimeStamp())
            .isEqualTo(0L)
    }

}