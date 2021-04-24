package sk.kasper.ui_common.utils

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.text.format.DateUtils
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.databinding.BindingAdapter
import com.google.android.material.shape.MaterialShapeDrawable
import com.squareup.picasso.Picasso
import org.ocpsoft.prettytime.PrettyTime
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneId
import sk.kasper.ui_common.R
import java.util.*


// objects to avoid allocation
val launchPrettyTime = PrettyTime()
val launchFormatTimeParts = mutableListOf<String>()
val launchDate = Date()

@BindingAdapter(
    value = ["launchDateTime", "prettyTimeVisible", "formattedTimeVisible", "formattedTimeType", "dateConfirmed"],
    requireAll = true
)
fun setLaunchTimeFormat(
    view: TextView,
    launchDateTime: LocalDateTime,
    prettyTimeVisible: Boolean,
    formattedTimeVisible: Boolean,
    formattedTimeType: FormattedTimeType,
    dateConfirmed: Boolean
) {
    if (!dateConfirmed) {
        view.setText(R.string.date_not_confirmed)
        return
    }

    launchFormatTimeParts.clear()
    val timeStamp = launchDateTime.atZone(ZoneId.systemDefault()).toEpochSecond() * 1000

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

        launchFormatTimeParts.add(DateUtils.formatDateTime(view.context, timeStamp, flags))
    }

    view.text = launchFormatTimeParts.joinToString(separator = " • ")
}

@BindingAdapter(value = ["srcCompat", "tint"])
fun setTint(imageView: ImageView, @DrawableRes drawableRes: Int, @ColorRes tintColor: Int) {
    val context = imageView.context
    imageView.setImageDrawable(
        getVectorDrawableWithTint(
            context,
            drawableRes,
            ContextCompat.getColor(context, tintColor)
        )
    )
}

@BindingAdapter(value = ["tint"])
fun setTint(imageView: ImageView, @ColorRes tintColor: Int) {
    if (tintColor != 0) {
        imageView.setImageDrawable(
            setTint(
                imageView.drawable,
                ContextCompat.getColor(imageView.context, tintColor)
            )
        )
    }
}

@BindingAdapter(value = ["srcAsync", "fallbackDrawable"])
fun setImageAsync(imageView: ImageView, uri: String?, @DrawableRes fallbackDrawable: Int) {
    Picasso.with(imageView.context)
        .load(uri)
        .error(fallbackDrawable)
        .into(imageView)
}

@BindingAdapter("srcCompat")
fun setImageResource(imageView: ImageView, @DrawableRes drawableRes: Int) {
    imageView.setImageResource(drawableRes)
}

@BindingAdapter(
    value = ["circleResource", "fallbackName", "roundedSquareLetterProvider"],
    requireAll = true
)
fun setCircleImage(
    imageView: ImageView,
    @DrawableRes circleResource: Int,
    fallbackName: String,
    roundedSquareLetterProvider: RoundedSquareLetterProvider
) {
    if (circleResource == 0) {
        imageView.setImageBitmap(
            roundedSquareLetterProvider.createLetter(
                fallbackName.first()
            )
        )
    } else {
        Picasso.with(imageView.context)
            .load(circleResource)
            .transform(
                RoundedSquareTransformation(
                    R.dimen.rounded_corners_radius.toPixels(imageView.context).toFloat()
                )
            )
            .resizeDimen(R.dimen.rocket_icon_size, R.dimen.rocket_icon_size)
            .into(imageView)
    }
}

@BindingAdapter("capitalizeText")
fun setCapitalizeText(view: TextView, @StringRes stringRes: Int) {
    view.text = if (stringRes != 0) view.context.resources.getString(stringRes).capitalize() else ""
}

@BindingAdapter("capitalizeText")
fun setCapitalizeText(view: TextView, text: String) {
    view.text = text.capitalize()
}

@BindingAdapter("textRes")
fun setResourceText(view: TextView, @StringRes stringRes: Int) {
    view.text = if (stringRes != 0) view.context.resources.getString(stringRes) else ""
}

@BindingAdapter("backgroundTint")
fun setBackgroundTint(view: View, @ColorRes colorRes: Int) {
    view.backgroundTintList = if (colorRes != 0) ColorStateList.valueOf(
        ContextCompat.getColor(
            view.context,
            colorRes
        )
    ) else null
}

@BindingAdapter("visible")
fun setVisible(view: View, visible: Boolean) {
    view.visibility = if (visible) View.VISIBLE else View.GONE
}

fun getVectorDrawable(context: Context, resourceId: Int): Drawable? =
    AppCompatResources.getDrawable(context, resourceId)

fun getVectorDrawableWithTint(
    context: Context,
    @DrawableRes resourceId: Int,
    @ColorInt color: Color
): Drawable? {
    val drawable = getVectorDrawable(context, resourceId)
    return setTint(drawable, color)
}

typealias Color = Int

fun setTint(drawable: Drawable?, @ColorInt color: Color): Drawable? {
    if (drawable != null && color != 0) {
        val wrapper = DrawableCompat.wrap(drawable)
        wrapper.mutate()
        DrawableCompat.setTintList(wrapper, null)
        DrawableCompat.setTint(wrapper, color)

        return wrapper
    }

    return drawable
}

@BindingAdapter("formattedText")
fun setFormattedText(textView: TextView, formattedString: FormattedString) {
    textView.text = formattedString.toFormattedString(textView.context)
}

@BindingAdapter("elevationWithOverlay")
fun setElevationWithOverlay(view: View, elevation: Float) {
    val px = elevation.dp(view.context)
    view.elevation = px
    view.background = MaterialShapeDrawable.createWithElevationOverlay(view.context, px)
}

@BindingAdapter(
    "paddingLeftSystemWindowInsets",
    "paddingTopSystemWindowInsets",
    "paddingRightSystemWindowInsets",
    "paddingBottomSystemWindowInsets",
    requireAll = false
)
fun applySystemWindows(
    view: View,
        applyLeft: Boolean,
        applyTop: Boolean,
        applyRight: Boolean,
        applyBottom: Boolean
) {
    view.doOnApplyWindowInsets { insets, padding ->
        val left = if (applyLeft) insets.systemWindowInsetLeft else 0
        val top = if (applyTop) insets.systemWindowInsetTop else 0
        val right = if (applyRight) insets.systemWindowInsetRight else 0
        val bottom = if (applyBottom) insets.systemWindowInsetBottom else 0

        setPadding(
                padding.left + left,
                padding.top + top,
                padding.right + right,
                padding.bottom + bottom
        )
    }
}