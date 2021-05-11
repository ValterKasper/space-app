package sk.kasper.ui_timeline

import androidx.annotation.DrawableRes
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableList
import org.threeten.bp.LocalDateTime
import org.threeten.bp.LocalTime
import sk.kasper.ui_common.tag.UiTag
import sk.kasper.ui_common.utils.FormattedTimeType

open class LaunchListItemViewModel(val item: LaunchListItem) {

    val tags: ObservableList<UiTag> = ObservableArrayList()

    val tagsVisible: Boolean

    val title: String

    val launchDateTime: LocalDateTime

    var prettyTimeVisible: Boolean

    val formattedTimeVisible: Boolean

    var formattedTimeType: FormattedTimeType

    var dateConfirmed: Boolean

    @get:DrawableRes
    val rocketIcon: Int

    init {
        tags.clear()
        tags.addAll(item.tags)

        rocketIcon = item.rocketResId

        tagsVisible = tags.isNotEmpty()

        title =
            item.launchName.replace("|", "•") // launchName contains both launch and rocket name
        launchDateTime = item.launchDateTime

        dateConfirmed = true
        val currentDateTime = LocalDateTime.now()
        val weekLaterDateTime = currentDateTime.plusDays(7)
        val todayMidnightDateTime =
            LocalDateTime.of(currentDateTime.toLocalDate(), LocalTime.MIDNIGHT).plusDays(1)
        when {
            launchDateTime.isBefore(todayMidnightDateTime) -> {
                formattedTimeType = FormattedTimeType.TIME
                formattedTimeVisible = true
                prettyTimeVisible = true
            }
            launchDateTime.isBefore(weekLaterDateTime) -> {
                formattedTimeType = FormattedTimeType.WEEKDAY_TIME
                formattedTimeVisible = true
                prettyTimeVisible = true
            }
            else -> {
                formattedTimeType = FormattedTimeType.DATE
                formattedTimeVisible = true
                prettyTimeVisible = false
            }
        }
        if (!item.accurateTime) {
            prettyTimeVisible = false
            formattedTimeType = FormattedTimeType.DATE
        }
        dateConfirmed = item.accurateDate
    }

    open fun getCurrentDateTime(): LocalDateTime = LocalDateTime.now()

}

