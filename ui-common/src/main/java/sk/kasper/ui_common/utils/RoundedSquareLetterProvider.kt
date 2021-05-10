package sk.kasper.ui_common.utils

import android.content.Context
import android.graphics.*
import androidx.core.content.ContextCompat
import sk.kasper.ui_common.R


class RoundedSquareLetterProvider(context: Context) {

    private var size: Int = R.dimen.rocket_icon_size.toPixels(context)
    private var cornerRadius = R.dimen.rounded_corners_radius.toPixels(context).toFloat()
    private var center: Float = size.toFloat() / 2
    private var bounds: Rect = Rect()
    private var iconLetterBackgroundPaint: Paint = Paint()
    private var textPaint: Paint

    init {
        iconLetterBackgroundPaint.isAntiAlias = true
        iconLetterBackgroundPaint.color =
            ContextCompat.getColor(context, R.color.iconLetterBackground)

        textPaint = Paint()
        textPaint.color = ContextCompat.getColor(context, R.color.iconLetter)
        textPaint.typeface = Typeface.create("sans-serif", Typeface.NORMAL)
        textPaint.textSize = R.dimen.rocket_icon_text_size.toPixels(context).toFloat()
        textPaint.isAntiAlias = true
        textPaint.textAlign = Paint.Align.CENTER
    }

    fun createLetter(letter: Char): Bitmap {
        val bmp = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)

        // square
        val canvas = Canvas(bmp)
        drawLetterOnCanvas(canvas, letter)

        return bmp
    }

    fun drawLetterOnCanvas(canvas: Canvas, letter: Char) {
        canvas.drawRoundRect(
            .0f,
            .0f,
            size.toFloat(),
            size.toFloat(),
            cornerRadius,
            cornerRadius,
            iconLetterBackgroundPaint
        )

        // letter
        textPaint.getTextBounds(letter.toString(), 0, 1, bounds)
        canvas.drawText(
            letter.toUpperCase().toString(), 0, 1, 0 + center, 0 + center
                    + (bounds.bottom - bounds.top) / 2, textPaint
        )
    }

}