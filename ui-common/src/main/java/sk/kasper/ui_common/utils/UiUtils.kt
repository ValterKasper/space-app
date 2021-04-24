package sk.kasper.ui_common.utils

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.Color.MAGENTA
import android.util.TypedValue
import android.view.View
import android.view.WindowInsets
import androidx.annotation.ColorInt
import androidx.navigation.NavOptions
import sk.kasper.ui_common.R


fun Int.toPixels(context: Context): Int = context.resources.getDimensionPixelSize(this)

fun Float.dp(context: Context): Float {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this, context.resources.displayMetrics)
}

/***
 * This is @AttrRes
 */
@ColorInt
fun Int.getThemeColor(context: Context): Int {
    val a = context.obtainStyledAttributes(null, intArrayOf(this))

    try {
        return a.getColor(0, MAGENTA)
    } finally {
        a.recycle()
    }
}

val Resources.isLandscape: Boolean
    get() = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

val Resources.isPortrait: Boolean
    get() = !isLandscape

fun View.doOnApplyWindowInsets(f: View.(WindowInsets, InitialPadding) -> Unit) {
    // Create a snapshot of the view's padding state
    val initialPadding = recordInitialPaddingForView(this)
    // Set an actual OnApplyWindowInsetsListener which proxies to the given
    // lambda, also passing in the original padding state
    setOnApplyWindowInsetsListener { _, insets ->
        f(insets, initialPadding)
        // Always return the insets, so that children can also use them
        insets
    }
    // request some insets
    requestApplyInsetsWhenAttached()
}

fun createSlideAnimNavOptions(): NavOptions {
    return NavOptions.Builder()
        .setEnterAnim(R.anim.enter_left)
        .setExitAnim(R.anim.exit_left)
        .setPopEnterAnim(R.anim.enter_right)
        .setPopExitAnim(R.anim.exit_right)
        .build()
}

data class InitialPadding(val left: Int, val top: Int,
                          val right: Int, val bottom: Int)

private fun recordInitialPaddingForView(view: View) = InitialPadding(
        view.paddingLeft, view.paddingTop, view.paddingRight, view.paddingBottom)

private fun View.requestApplyInsetsWhenAttached() {
    if (isAttachedToWindow) {
        // We're already attached, just request as normal
        requestApplyInsets()
    } else {
        // We're not attached to the hierarchy, add a listener to
        // request when we are
        addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
            override fun onViewAttachedToWindow(v: View) {
                v.removeOnAttachStateChangeListener(this)
                v.requestApplyInsets()
            }

            override fun onViewDetachedFromWindow(v: View) = Unit
        })
    }
}