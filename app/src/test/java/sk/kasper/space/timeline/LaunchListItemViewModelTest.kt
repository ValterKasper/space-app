package sk.kasper.space.timeline

import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.threeten.bp.LocalDateTime
import org.threeten.bp.Month
import sk.kasper.space.utils.FormattedTimeType


@RunWith(MockitoJUnitRunner::class)
class LaunchListItemViewModelTest {

    private companion object {
        val LOCAL_DATE_TIME_NOW: LocalDateTime = LocalDateTime.of(2000, Month.JANUARY, 1, 12, 0)
    }

    private lateinit var launchListItemViewModel: LaunchListItemViewModel

    @Mock
    private lateinit var listener: LaunchListItemViewModel.OnListInteractionListener

    @Before
    fun setUp() {
        launchListItemViewModel = object : LaunchListItemViewModel(listener) {
            override fun getCurrentDateTime(): LocalDateTime {
                return LOCAL_DATE_TIME_NOW
            }
        }
    }

    @Test
    fun setLaunchListItem_today_accurateDateAndTime_showTime_showPrettyTime_showFormattedTime() {
        launchListItemViewModel.launchListItem = createLaunchListItem(
                accurateDate = true,
                accurateTime = true,
                dateTime = LOCAL_DATE_TIME_NOW.plusHours(1)
        )

        assertDateTimeFormatting(
                formattedTimeType = FormattedTimeType.TIME,
                formattedTimeVisible = true,
                prettyTimeVisible = true
        )
    }

    @Test
    fun setLaunchListItem_accurateJustDate_showDate() {
        launchListItemViewModel.launchListItem = createLaunchListItem(
                accurateDate = true,
                accurateTime = false,
                dateTime = LOCAL_DATE_TIME_NOW.plusDays(5)
        )

        assertDateTimeFormatting(
                formattedTimeType = FormattedTimeType.DATE,
                formattedTimeVisible = true,
                prettyTimeVisible = false
        )
    }

    @Test
    fun setLaunchListItem_dateNotAccurate_setDateNotConfirmed() {
        launchListItemViewModel.launchListItem = createLaunchListItem(
                accurateDate = false,
                accurateTime = false,
                dateTime = LOCAL_DATE_TIME_NOW.plusDays(5)
        )

        assertThat(launchListItemViewModel.dateConfirmed, `is`(false))
    }

    private fun createLaunchListItem(dateTime: LocalDateTime, accurateDate: Boolean = true, accurateTime: Boolean = true): LaunchListItem {
        return LaunchListItem(
                10L,
                "Name",
                dateTime,
                null,
                null,
                accurateDate,
                accurateTime,
                emptyList())
    }

    private fun assertDateTimeFormatting(formattedTimeType: FormattedTimeType = FormattedTimeType.FULL, prettyTimeVisible: Boolean = true, formattedTimeVisible: Boolean = true, dateConfirmed: Boolean = true) {
        assertThat(launchListItemViewModel.formattedTimeType, `is`(formattedTimeType))
        assertThat(launchListItemViewModel.formattedTimeVisible, `is`(formattedTimeVisible))
        assertThat(launchListItemViewModel.prettyTimeVisible, `is`(prettyTimeVisible))
        assertThat(launchListItemViewModel.dateConfirmed, `is`(dateConfirmed))
    }

}