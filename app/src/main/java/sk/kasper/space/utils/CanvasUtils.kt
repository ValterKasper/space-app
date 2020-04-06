package sk.kasper.space.utils

import android.graphics.Canvas

fun Canvas.transform(block: Canvas.() -> Unit) {
    save()
    block()
    restore()
}

