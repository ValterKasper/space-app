package sk.kasper.ui_common.ui

import android.text.format.DateUtils
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import org.ocpsoft.prettytime.PrettyTime
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneId
import org.threeten.bp.ZoneOffset
import sk.kasper.ui_common.R
import sk.kasper.ui_common.utils.FormattedTimeType
import java.util.*

@Composable
fun LaunchDateTime(
    modifier: Modifier = Modifier,
    launchDateTime: LocalDateTime,
    formattedTimeType: FormattedTimeType,
    dateConfirmed: Boolean,
    prettyTimeVisible: Boolean,
    formattedTimeVisible: Boolean
) {
    val text: String
    if (!dateConfirmed) {
        text = stringResource(id = R.string.date_not_confirmed)
    } else {
        val launchPrettyTime = PrettyTime()
        val launchFormatTimeParts = mutableListOf<String>()
        val launchDate = Date()
        launchFormatTimeParts.clear()

        val timeStamp = if (LocalInspectionMode.current) {
            LocalDateTime.of(2020, 4, 6, 12, 30).toEpochSecond(ZoneOffset.UTC)
        } else {
            launchDateTime.atZone(ZoneId.systemDefault()).toEpochSecond() * 1000
        }

        if (prettyTimeVisible) {
            launchDate.time = timeStamp
            launchFormatTimeParts.add(launchPrettyTime.format(launchDate))
        }

        if (formattedTimeVisible) {
            val flags: Int = when (formattedTimeType) {
                FormattedTimeType.WEEKDAY_TIME -> DateUtils.FORMAT_SHOW_WEEKDAY or DateUtils.FORMAT_SHOW_TIME
                FormattedTimeType.DATE -> DateUtils.FORMAT_SHOW_DATE
                FormattedTimeType.TIME -> DateUtils.FORMAT_SHOW_TIME
                FormattedTimeType.FULL -> DateUtils.FORMAT_SHOW_WEEKDAY or DateUtils.FORMAT_SHOW_TIME or DateUtils.FORMAT_SHOW_DATE
            }

            launchFormatTimeParts.add(
                DateUtils.formatDateTime(
                    LocalContext.current,
                    timeStamp,
                    flags
                )
            )
        }

        text = launchFormatTimeParts.joinToString(separator = " • ")
    }
    Text(
        maxLines = 2,
        overflow = TextOverflow.Ellipsis,
        text = text,
        style = MaterialTheme.typography.body2,
        modifier = modifier
    )
}