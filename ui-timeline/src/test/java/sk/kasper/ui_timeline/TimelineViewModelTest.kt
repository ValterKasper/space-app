package sk.kasper.ui_timeline

import android.view.View
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.threeten.bp.LocalDateTime
import org.threeten.bp.Month
import sk.kasper.domain.model.FilterSpec
import sk.kasper.domain.model.Tag
import sk.kasper.domain.usecase.timeline.GetTimelineItems
import sk.kasper.domain.usecase.timeline.RefreshTimelineItems
import sk.kasper.domain.utils.createLaunch
import sk.kasper.ui_common.settings.SettingsManager
import sk.kasper.ui_timeline.filter.FilterItem
import sk.kasper.ui_timeline.filter.TimelineFilterSpecModel
import sk.kasper.ui_timeline.utils.CoroutinesMainDispatcherRule

private val LOCAL_DATE_TIME_NOW: LocalDateTime = LocalDateTime.of(2000, Month.JANUARY, 1, 12, 0)

class TimelineViewModelTest {

    @get:Rule
    @ExperimentalCoroutinesApi
    var coroutinesMainDispatcherRule = CoroutinesMainDispatcherRule()

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Test
    fun noLaunches_noItems() = timelineListTest {
        expectingNoMatchingLaunchesVisible = false
        expectingRetryToLoadLaunchesVisible = true
    }

    @Test
    fun singleLaunchToday_todayLabelAndOneItem() = timelineListTest {
        accurate launchAt LOCAL_DATE_TIME_NOW.plusHours(2)

        expecting item LabelListItem.Today
        expecting item launch

        expectingNoMatchingLaunchesVisible = false
        expectingRetryToLoadLaunchesVisible = false
    }

    @Test
    fun singleLaunchThisWeek_thisWeekLabelAndOneItem() = timelineListTest {
        accurate launchAt LOCAL_DATE_TIME_NOW.plusDays(4)

        expecting item LabelListItem.ThisWeek
        expecting item launch

        expectingRetryToLoadLaunchesVisible = false
    }

    @Test
    fun singleLaunchAfterThisWeek_monthLabelAndOneItem() = timelineListTest {
        accurate launchAt LOCAL_DATE_TIME_NOW.plusDays(10)

        expecting item LabelListItem.Month(1)
        expecting item launch
    }

    @Test
    fun singleLaunchTomorrow_tomorrowLabelAndOneItem() = timelineListTest {
        accurate launchAt LOCAL_DATE_TIME_NOW.plusHours(25)

        expecting item LabelListItem.Tomorrow
        expecting item launch
    }

    @Test
    @Ignore("oprav lebo neprechadza")
    fun singleLaunchTomorrow_inaccurate_monthLabelAndOneItem() = timelineListTest {
        inaccurate launchAt LOCAL_DATE_TIME_NOW.plusHours(25)

        expecting item LabelListItem.Month(1)
        expecting item launch
    }

    @Test
    fun singleLaunchTomorrow_inaccurate_unconfirmedLaunchesForbidden_noItems() = timelineListTest {
        showUnconfirmedLaunches = false

        inaccurate launchAt LOCAL_DATE_TIME_NOW.plusHours(25)

        expectingRetryToLoadLaunchesVisible = true
    }

    @Test
    fun singleLaunchEarlierToday_monthLabelAndOneItem() = timelineListTest {
        accurate launchAt LOCAL_DATE_TIME_NOW.minusHours(4)

        expecting item LabelListItem.Today
        expecting item launch
    }

    @Test
    fun singleLaunchYesterday_noItems() = timelineListTest {
        accurate launchAt LOCAL_DATE_TIME_NOW.minusDays(1)

        expectingRetryToLoadLaunchesVisible = true
    }

    @Test
    fun oneInaccurateOneAccurate_inaccurateIsLast() = timelineListTest {
        inaccurate launchAt LOCAL_DATE_TIME_NOW.plusDays(1)
        accurate launchAt LOCAL_DATE_TIME_NOW.plusDays(5)

        expecting item LabelListItem.ThisWeek
        expecting item accurate
        expecting item LabelListItem.Month(1)
        expecting item inaccurate
    }

    @Test
    fun twoLaunchesTomorrow_tomorrowLabelAndTwoItems() = timelineListTest {
        LOCAL_DATE_TIME_NOW plusHours 25 isLaunchTimeOf launch
        LOCAL_DATE_TIME_NOW plusHours 26 isLaunchTimeOf launch

        expecting item LabelListItem.Tomorrow
        expecting item launch
        expecting item launch
    }

    @Test
    @Ignore("add test for progress")
    fun loading_progressVisibleAndShowItems() {
    }

    @Test
    @Ignore("add test for error")
    fun error_hideProgress() {
    }

    @Test
    fun `should show unselected filter items when filter is empty`() {
        onViewModel(initialModelFilterSpec = FilterSpec.EMPTY_FILTER) {
            verifyActualFilterItems { values ->
                Assert.assertTrue(values[0] is FilterItem.HeaderFilterItem)
                Assert.assertTrue(values[1] is FilterItem.TagFilterItem)
                Assert.assertFalse((values[1] as FilterItem.TagFilterItem).selected)
                Assert.assertFalse((values[FilterSpec.ALL_TAGS.size + 2] as FilterItem.RocketFilterItem).selected)
            }

            MatcherAssert.assertThat(View.GONE, `is`(viewModel.clearButtonVisibility.get()))
        }
    }

    @Test
    fun `should show selected tag filter item when tag is in filter spec`() {
        onViewModel(initialModelFilterSpec = FilterSpec(tagTypes = setOf(Tag.ISS))) {
            verifyActualFilterItems { values ->
                Assert.assertTrue((values[FilterSpec.ALL_TAGS.indexOf(Tag.ISS) + 1] as FilterItem.TagFilterItem).selected)
            }

            MatcherAssert.assertThat(View.VISIBLE, `is`(viewModel.clearButtonVisibility.get()))
        }
    }

    private fun onViewModel(
        initialModelFilterSpec: FilterSpec,
        func: TimelineFilterViewModelRobot.() -> Unit
    ) = TimelineFilterViewModelRobot(initialModelFilterSpec).apply {
        func()
    }

    private class TimelineFilterViewModelRobot(initialModelValue: FilterSpec) {
        // todo fix
        lateinit var viewModel: TimelineViewModel

        @Mock
        private lateinit var timelineFilterSpecModel: TimelineFilterSpecModel

        init {
            MockitoAnnotations.initMocks(this)

            whenever(timelineFilterSpecModel.value).thenReturn(initialModelValue)

            // todo fix
//            viewModel = TimelineViewModel(timelineFilterSpecModel)
        }

        fun verifyActualFilterItems(func: (List<FilterItem>) -> Unit) {
            // todo fix
            //val list = viewModel.filterItems.value
            val list = emptyList<FilterItem>()

            Assert.assertNotNull(list)

            list?.let {
                MatcherAssert.assertThat(filterItemsSize(), `is`(list.size))
                Assert.assertTrue(list[FilterSpec.ALL_TAGS.size + 1] is FilterItem.HeaderFilterItem)
                func(list)
            }
        }

        fun filterItemsSize() = 1 + 1 + FilterSpec.ALL_ROCKETS.size + FilterSpec.ALL_TAGS.size

    }

    private infix fun LocalDateTime.plusHours(hours: Long): LocalDateTime {
        return this.plusHours(hours)
    }

    private infix fun LocalDateTime.plusDays(days: Long): LocalDateTime {
        return this.plusDays(days)
    }

    private fun timelineListTest(given: suspend TimelineListTestBuilder.() -> Unit) = runBlocking {
        TimelineListTestBuilder().apply {
            given()
            check()
        }

        Unit
    }

    private class TimelineListTestBuilder {

        @Mock
        private lateinit var getTimelineItems: GetTimelineItems

        @Mock
        private lateinit var refreshTimelineItems: RefreshTimelineItems

        @Mock
        private lateinit var settingsManager: SettingsManager

        private lateinit var viewModel: TimelineViewModelUnderTest

        private val filterModel = TimelineFilterSpecModel()
        private val launches = mutableListOf<sk.kasper.domain.model.Launch>()
        private val expectedItems = mutableListOf<ExpectedListItem>()

        val accurate = true
        val launch = true
        val inaccurate = false
        val expecting = "expecting"

        var showUnconfirmedLaunches = true

        var expectingNoMatchingLaunchesVisible = false
        var expectingRetryToLoadLaunchesVisible = false

        init {
            MockitoAnnotations.initMocks(this)
        }

        suspend fun check() {
            whenever(getTimelineItems.getTimelineItems(FilterSpec.EMPTY_FILTER)).thenReturn(launches)
            whenever(settingsManager.showUnconfirmedLaunches).thenReturn(showUnconfirmedLaunches)

            viewModel = TimelineViewModelUnderTest()

            Assert.assertThat(getCurrentTimelineItems().size, `is`(expectedItems.size))
            assertEquals(viewModel.showNoMatchingLaunches, expectingNoMatchingLaunchesVisible)
            assertEquals(viewModel.showRetryToLoadLaunches, expectingRetryToLoadLaunchesVisible)
            assertEquals(false, viewModel.progressVisible.value)

            expectedItems.forEachIndexed { index, expectedListItem ->
                val actualListItem = getCurrentTimelineItems()[index]

                when (expectedListItem) {
                    is ExpectedListItem.Launch-> {
                        Assert.assertTrue(actualListItem is LaunchListItem)
                        if (actualListItem is LaunchListItem) {
                            val message = "of launch at position $index is expected to be accurate=${expectedListItem.accurate}"
                            assertEquals("date $message", expectedListItem.accurate, actualListItem.accurateDate)
                            assertEquals("time $message", expectedListItem.accurate, actualListItem.accurateTime)
                        }
                    }
                    is ExpectedListItem.Label -> {
                        Assert.assertTrue(actualListItem is LabelListItem)
                        if (actualListItem is LabelListItem) {
                            assertEquals(
                                    "expected label ${expectedListItem.labelListItem.javaClass.simpleName} at position $index but was ${actualListItem.javaClass.simpleName}",
                                    expectedListItem.labelListItem,
                                    actualListItem)
                        }
                    }
                }
            }
        }

        infix fun Boolean.launchAt(localDateTime: LocalDateTime) {
            launches.add(createLaunch(launchDateTime = localDateTime, accurateDate = this, accurateTime = this))
        }

        infix fun LocalDateTime.isLaunchTimeOf(accurate: Boolean) {
            launches.add(createLaunch(launchDateTime = this, accurateDate = accurate, accurateTime = accurate))
        }

        infix fun String.item(accurate: Boolean) {
            expectedItems.add(ExpectedListItem.Launch(accurate))
        }

        infix fun String.item(label: LabelListItem) {
            expectedItems.add(ExpectedListItem.Label(label))
        }

        private fun getCurrentTimelineItems() = viewModel.timelineItems.value!!

        private sealed class ExpectedListItem {
            data class Launch(val accurate: Boolean): ExpectedListItem()
            data class Label(val labelListItem: LabelListItem): ExpectedListItem()
        }

        private inner class TimelineViewModelUnderTest : TimelineViewModel(getTimelineItems, refreshTimelineItems, settingsManager, filterModel) {
            override fun getCurrentDateTime(): LocalDateTime = LOCAL_DATE_TIME_NOW
        }
    }

}