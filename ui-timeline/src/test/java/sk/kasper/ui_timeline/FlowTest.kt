package sk.kasper.ui_timeline

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeoutOrNull
import org.junit.Assert.assertEquals
import org.junit.Test

class FlowTest {

    @Test
    fun simple_suspend_test() = runBlocking {
        assertEquals(listOf(1, 2, 3), simple_suspend())
    }

    @Test
    fun simple_flow_test() = runBlocking {
        val list = mutableListOf<Int>()
        simple_flow().collect {
            list.add(it)
        }
        assertEquals(listOf(1, 2, 3), list)
    }

    @Test
    fun simple_flow_cancellation_test() = runBlocking {
        val list = mutableListOf<Int>()
        withTimeoutOrNull(250) {
            simple_flow().collect {
                list.add(it)
            }
        }
        assertEquals(listOf(1, 2), list)
    }

    private suspend fun simple_suspend(): List<Int> {
        delay(100) // pretend we are doing something asynchronous here
        return listOf(1, 2, 3)
    }

    private fun simple_flow(): Flow<Int> = flow {
        for (i in listOf(1, 2, 3)) {
            delay(100)
            emit(i)
        }
    }
}