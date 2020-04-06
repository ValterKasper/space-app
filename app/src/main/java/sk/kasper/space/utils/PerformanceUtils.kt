package sk.kasper.space.utils

import androidx.core.os.TraceCompat

fun <R> track(traceName: String, block: () -> R): R {
    TraceCompat.beginSection(traceName)
    val ret: R = block()
    TraceCompat.endSection()
    return ret
}

