package sk.kasper.ui_common.utils

import android.graphics.Canvas

fun Canvas.transform(block: Canvas.() -> Unit) {
    save()
    block()
    restore()
}

