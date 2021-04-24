package sk.kasper.ui_common.utils

import android.content.Context
import androidx.annotation.StringRes

data class FormattedString (@StringRes private val stringRes: Int = 0, private val textData: Any? = null) {

    companion object {
        fun empty(): FormattedString = FormattedString(textData = "")
        fun from(@StringRes stringRes: Int = 0) = FormattedString(stringRes = stringRes)
        fun from(text: Any? = null) = FormattedString(textData = text)
    }

    fun toFormattedString(context: Context): String = if (stringRes == 0) {
        textData?.toString() ?: throw IllegalArgumentException("Missing textData argument")
    } else {
        val text = context.getString(stringRes)
        if (textData != null) {
            String.format(text, textData)
        } else {
            text
        }
    }

}