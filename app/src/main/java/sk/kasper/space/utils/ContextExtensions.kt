package sk.kasper.space.utils

import android.content.Context
import java.io.InputStream

fun Context.readFileFromAssets(fileName: String): String {
    val inputStream: InputStream = assets.open(fileName)
    val size: Int = inputStream.available()
    val buffer = ByteArray(size)
    inputStream.read(buffer)
    inputStream.close()
    return String(buffer, Charsets.UTF_8)
}