package sk.kasper.ui_timeline

import android.content.Context
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import java.text.SimpleDateFormat
import java.util.*

class LabelListItemViewModel : BaseObservable() {

    private companion object {
        val simpleDateFormat = SimpleDateFormat("LLLL", Locale.getDefault())
        val calendar = GregorianCalendar()
    }

    var labelListItem: LabelListItem? = null
        set(value) {
            field = value
            notifyPropertyChanged(BR.labelListItem)
        }
        @Bindable get

    fun text(context: Context, labelListItem: LabelListItem?): String = when (labelListItem) {
        is LabelListItem.Today -> context.getString(R.string.today)
        is LabelListItem.Tomorrow -> context.getString(R.string.tomorrow)
        is LabelListItem.ThisWeek -> context.getString(R.string.this_week)
        is LabelListItem.Month -> {
            calendar.set(Calendar.DAY_OF_MONTH, 1)
            calendar.set(Calendar.MONTH, labelListItem.month - 1)
            simpleDateFormat.format(calendar.time)
        }
        null -> ""
    }

}