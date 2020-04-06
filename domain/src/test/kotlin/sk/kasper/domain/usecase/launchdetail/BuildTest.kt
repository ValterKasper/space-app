package sk.kasper.domain.usecase.launchdetail

import junit.framework.Assert.assertEquals
import org.junit.Test

// todo rm
class BuildTest {


    @Test
    fun buildString() {
        val string = buildString {
            append("foo")
            if (true) {
                append("echt")
            }
            append("trollo")
        }
        assertEquals("fooechttrollo", string)
    }

}