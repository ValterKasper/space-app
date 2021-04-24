package sk.kasper.ui_common.utils

import android.view.View
import androidx.databinding.BindingAdapter

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