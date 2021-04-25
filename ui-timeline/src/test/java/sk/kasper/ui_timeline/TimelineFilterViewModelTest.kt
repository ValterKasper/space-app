package sk.kasper.ui_timeline

import android.view.View
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockitokotlin2.whenever
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import sk.kasper.domain.model.FilterSpec
import sk.kasper.domain.model.Tag
import sk.kasper.ui_timeline.filter.FilterItem
import sk.kasper.ui_timeline.filter.TimelineFilterSpecModel
import sk.kasper.ui_timeline.filter.TimelineFilterViewModel

class TimelineFilterViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Test
    fun `should show unselected filter items when filter is empty`() {
        onViewModel(initialModelFilterSpec = FilterSpec.EMPTY_FILTER) {
            verifyActualFilterItems { values ->
                assertTrue(values[0] is FilterItem.HeaderFilterItem)
                assertTrue(values[1] is FilterItem.TagFilterItem)
                assertFalse((values[1] as FilterItem.TagFilterItem).selected)
                assertFalse((values[FilterSpec.ALL_TAGS.size + 2] as FilterItem.RocketFilterItem).selected)
            }

            assertThat(View.GONE, `is`(viewModel.clearButtonVisibility.get()))
        }
    }

    @Test
    fun `should show selected tag filter item when tag is in filter spec`() {
        onViewModel(initialModelFilterSpec = FilterSpec(tagTypes = setOf(Tag.ISS))) {
            verifyActualFilterItems { values ->
                assertTrue((values[FilterSpec.ALL_TAGS.indexOf(Tag.ISS) + 1] as FilterItem.TagFilterItem).selected)
            }

            assertThat(View.VISIBLE, `is`(viewModel.clearButtonVisibility.get()))
        }
    }

    private fun onViewModel(initialModelFilterSpec: FilterSpec, func: TimelineFilterViewModelRobot.() -> Unit)
            = TimelineFilterViewModelRobot(initialModelFilterSpec).apply {
        func()
    }

    private class TimelineFilterViewModelRobot(initialModelValue: FilterSpec) {

        val viewModel: TimelineFilterViewModel

        @Mock
        private lateinit var timelineFilterSpecModel: TimelineFilterSpecModel

        init {
            MockitoAnnotations.initMocks(this)

            whenever(timelineFilterSpecModel.value).thenReturn(initialModelValue)

            viewModel = TimelineFilterViewModel(timelineFilterSpecModel)
        }

        fun verifyActualFilterItems(func: (List<FilterItem>) -> Unit) {
            val list = viewModel.filterItems.value

            assertNotNull(list)

            list?.let {
                assertThat(filterItemsSize(), `is`(list.size))
                assertTrue(list[FilterSpec.ALL_TAGS.size + 1] is FilterItem.HeaderFilterItem)
                func(list)
            }
        }

        fun filterItemsSize() = 1 + 1 + FilterSpec.ALL_ROCKETS.size + FilterSpec.ALL_TAGS.size

    }
}