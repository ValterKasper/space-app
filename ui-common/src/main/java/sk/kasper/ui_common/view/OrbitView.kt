package sk.kasper.ui_common.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import sk.kasper.ui_common.R
import sk.kasper.ui_common.utils.transform
import kotlin.math.min


private const val EARTH_ROTATION_SPEED = 25
private const val LEO_ROTATION_SPEED = 60
private const val GEO_ROTATION_SPEED = EARTH_ROTATION_SPEED

enum class OrbitToDraw {
    LEO, GEO
}

class OrbitView : View {

    private lateinit var selectedOrbitPaint: Paint
    private lateinit var unselectedOrbitPaint: Paint
    private lateinit var satellitePaint: Paint
    private lateinit var earthPaint: Paint
    private lateinit var nightShadowPaint: Paint
    private lateinit var earthBitmap: Bitmap

    private lateinit var leoOrbitPaint: Paint
    private lateinit var geoOrbitPaint: Paint
    private var satelliteRotationSpeed: Int = 0
    private var gradientOffset: Float = 0f
    private var geoRadius = 0f
    private var leoRadius = 0f
    private var earthRadius = 0f
    private var satelliteRadius = 0f
    private var satelliteOrbitRadius = 0f

    private var startTime: Long = 0

    var orbit: OrbitToDraw = OrbitToDraw.LEO
        set(value) {
            field = value
            update()
        }

    constructor(context: Context) : super(context) {
        this.init(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        this.init(context)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        this.init(context)
    }

    private fun init(context: Context) {
        earthBitmap = circleTransform(
            BitmapFactory.decodeResource(
                resources,
                R.drawable.earth_from_north_pole
            )
        )

        earthRadius = (earthBitmap.width / 2).toFloat()
        gradientOffset = earthRadius / 4
        leoRadius = resources.getDimensionPixelOffset(R.dimen.leo_radius).toFloat()
        geoRadius = resources.getDimensionPixelOffset(R.dimen.geo_radius).toFloat()
        satelliteRadius = resources.getDimensionPixelOffset(R.dimen.satellite_radius).toFloat()
        satelliteOrbitRadius = resources.getDimensionPixelOffset(R.dimen.geo_radius).toFloat()
        val orbitStrokeWidth = resources.getDimensionPixelOffset(R.dimen.orbit_stroke_width).toFloat()

        earthPaint = Paint()

        nightShadowPaint = Paint().apply {
            shader = LinearGradient(-gradientOffset, 0f, gradientOffset, 0f, Color.WHITE, Color.parseColor("#444444"), Shader.TileMode.CLAMP)
            xfermode = PorterDuffXfermode(PorterDuff.Mode.MULTIPLY)
        }

        selectedOrbitPaint = Paint().apply {
            color = ContextCompat.getColor(context, R.color.colorPrimaryDark)
            style = Paint.Style.STROKE
            strokeWidth = orbitStrokeWidth
        }

        unselectedOrbitPaint = Paint().apply {
            color = ContextCompat.getColor(context, R.color.iconLetter)
            style = Paint.Style.STROKE
            strokeWidth = orbitStrokeWidth
        }

        satellitePaint = Paint().apply {
            color = ContextCompat.getColor(context, R.color.reddish)
        }

        startTime = System.currentTimeMillis()

        update()
    }

    private fun circleTransform(source: Bitmap): Bitmap {
        val size = min(source.width, source.height)

        val x = (source.width - size) / 2
        val y = (source.height - size) / 2

        val squaredBitmap = Bitmap.createBitmap(source, x, y, size, size)
        if (squaredBitmap != source) {
            source.recycle()
        }

        val config = source.config
        val circleBitmap = Bitmap.createBitmap(size, size, config)

        val canvas = Canvas(circleBitmap)
        val paint = Paint()

        val shader = BitmapShader(squaredBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        paint.shader = shader
        paint.isAntiAlias = true

        val r = size / 2f
        canvas.drawCircle(r, r, r, paint)

        squaredBitmap.recycle()

        return circleBitmap
    }

    private fun update() {
        when (orbit) {
            OrbitToDraw.LEO -> {
                leoOrbitPaint = selectedOrbitPaint
                geoOrbitPaint = unselectedOrbitPaint
                satelliteRotationSpeed = LEO_ROTATION_SPEED
                satelliteOrbitRadius = leoRadius
            }
            OrbitToDraw.GEO -> {
                leoOrbitPaint = unselectedOrbitPaint
                geoOrbitPaint = selectedOrbitPaint
                satelliteRotationSpeed = GEO_ROTATION_SPEED
                satelliteOrbitRadius = geoRadius
            }
        }
    }

    override fun onDraw(canvas: Canvas) {
        canvas.transform {
            translate((width / 2).toFloat(), (height / 2).toFloat())

            drawCircle(0f, 0f, leoRadius, leoOrbitPaint)
            drawCircle(0f, 0f, geoRadius, geoOrbitPaint)

            transform {
                rotate(currentRotationFromDegreesPerSecond(satelliteRotationSpeed))
                drawLine(0f, 0f, satelliteOrbitRadius, 0f, selectedOrbitPaint)
                drawCircle(satelliteOrbitRadius, 0f, satelliteRadius, satellitePaint)
            }

            transform {
                rotate(currentRotationFromDegreesPerSecond(EARTH_ROTATION_SPEED))
                translate(-earthRadius, -earthRadius)
                drawBitmap(earthBitmap, 0f, 0f, earthPaint)
            }

            drawCircle(0f, 0f, earthRadius, nightShadowPaint)
        }

        this.postInvalidateDelayed( 16)
    }

    private fun currentRotationFromDegreesPerSecond(degreesPerSecond: Int): Float {
        val elapsedTime = System.currentTimeMillis() - startTime
        return (elapsedTime / 1000f) * degreesPerSecond
    }

}