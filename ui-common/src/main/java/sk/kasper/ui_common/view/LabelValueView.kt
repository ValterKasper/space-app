package sk.kasper.ui_common.view

import android.content.Context
import android.content.res.Resources
import android.util.AttributeSet
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.BindingAdapter
import sk.kasper.ui_common.R
import sk.kasper.ui_common.utils.FormattedString
import sk.kasper.ui_common.utils.dp
import sk.kasper.ui_common.utils.toPixels
import timber.log.Timber

@BindingAdapter("label")
fun setLabelStringDataView(view: LabelValueView, label: String) {
    view.setLabel("$label:")
}

@BindingAdapter("label")
fun setLabelResDataView(view: LabelValueView, @StringRes labelRes: Int) {
    if (labelRes != 0) {
        view.setLabel(view.resources.getString(labelRes))
    }
}

@BindingAdapter("value")
fun setValueDataView(view: LabelValueView, value: String) {
    view.setValue(value)
}

@BindingAdapter("value")
fun setValueDataView(view: LabelValueView, value: FormattedString) {
    view.setValue(value.toFormattedString(view.context))
}

@BindingAdapter("value")
fun setValueDataView(view: LabelValueView, @StringRes labelRes: Int) {
    if (labelRes != 0) {
        view.setValue(view.resources.getString(labelRes))
    }
}

class LabelValueView : LinearLayout {

    lateinit var labelTextView: TextView
    lateinit var valueTextView: TextView

    constructor(context: Context) : super(context) {
        this.init(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        this.init(context)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        this.init(context)
    }

    private fun init(context: Context) {
        this.orientation = HORIZONTAL
        labelTextView = TextView(context, null, 0, R.style.TextAppearance_AppCompat_Body1).apply {
            try {
                typeface = ResourcesCompat.getFont(context, R.font.source_sans_pro_semibold)
            } catch (e: Resources.NotFoundException) {
                Timber.e(e)
            }
            setPaddingRelative(0, 0, 4f.dp(context).toInt(), 0)
        }
        valueTextView = TextView(context, null, 0, R.style.TextAppearance_AppCompat_Body1)
        addView(labelTextView)
        addView(valueTextView)
        val topBottomPadding = R.dimen.label_value_view_vertical_padding.toPixels(context)
        setPaddingRelative(0, topBottomPadding, 0, topBottomPadding)
    }

    fun setLabel(label: String) {
        labelTextView.text = label
    }

    fun setValue(value: String) {
        valueTextView.text = value
    }

}
