package sk.kasper.ui_timeline

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import sk.kasper.domain.model.FilterSpec
import sk.kasper.ui_timeline.filter.TimelineFilterSpecModel

@RunWith(MockitoJUnitRunner::class)
class TimelineFilterSpecModelTest {

    private lateinit var model: TimelineFilterSpecModel

    @Before
    fun setUp() {
        model = TimelineFilterSpecModel()
    }

    @Test
    fun getFilter_firstCall_emptyFilter() {
        assertThat(model.value, `is`(FilterSpec.EMPTY_FILTER))
    }

    @Test
    fun getFlow() = runBlocking {
        val filter = FilterSpec(setOf(10), emptySet())

        launch {
            val receivedFilter = model.flow.first()
            assertEquals(filter, receivedFilter)
        }

        model.value = filter
    }

}