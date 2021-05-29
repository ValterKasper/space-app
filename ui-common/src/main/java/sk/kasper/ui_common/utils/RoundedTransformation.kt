package sk.kasper.ui_common.utils

import android.graphics.*

sealed class RoundedTransformation(private val cornerRadiusPx: Float) {

    fun transform(source: Bitmap): Bitmap {
        val (width, height) = getTargetSize(source)

        val x = (source.width - width) / 2
        val y = (source.height - height) / 2

        val squaredSourceBitmap = Bitmap.createBitmap(source, x, y, width, height)
        if (squaredSourceBitmap != source) {
            source.recycle()
        }

        val config = source.config
        val roundedBitmap = Bitmap.createBitmap(width, height, config)

        val canvas = Canvas(roundedBitmap)
        val paint = Paint()

        val shader = BitmapShader(squaredSourceBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        paint.shader = shader
        paint.isAntiAlias = true

        canvas.drawRoundRect(.0f, .0f, width.toFloat(), height.toFloat(), cornerRadiusPx, cornerRadiusPx, paint)

        squaredSourceBitmap.recycle()

        return roundedBitmap
    }

    protected abstract fun getTargetSize(source: Bitmap): Pair<Int, Int>

    protected abstract val roundedKey: String

}

class RoundedRectangleTransformation(cornerRadiusPx: Float): RoundedTransformation(cornerRadiusPx) {

    override val roundedKey = "rectangle"

    override fun getTargetSize(source: Bitmap): Pair<Int, Int> {
        return Pair(source.width, source.height)
    }

}

class RoundedSquareTransformation(cornerRadiusPx: Float): RoundedTransformation(cornerRadiusPx) {

    override val roundedKey = "square"

    override fun getTargetSize(source: Bitmap): Pair<Int, Int> {
        val size = Math.min(source.width, source.height)
        return Pair(size, size)
    }

}
