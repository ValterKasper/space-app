package sk.kasper.ui_common.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

private val Cyan500 = Color(0xFF00BCD4)
private val Cyan700 = Color(0xFF0097A7)
private val Cyan200 = Color(0xFF80DEEA)
private val Blue500 = Color(0xff2196f3)

private val negroni = Color(0xFFFEE5BF)

private val SpaceLightPalette = lightColors(
        primary = Cyan500,
        primaryVariant = Cyan500,
        secondary = Blue500,
)

private val SpaceDarkPalette = darkColors(
        primary = Cyan700,
        primaryVariant = Cyan500,
        secondary = Blue500,
        onSurface = negroni,
        onBackground = negroni,
        surface = Color.Black,
)

private val Shapes = Shapes(
        small = RoundedCornerShape(6.dp),
        medium = RoundedCornerShape(6.dp),
        large = CutCornerShape(
                topStart = 16.dp,
                topEnd = 16.dp,
                bottomStart = 16.dp,
                bottomEnd = 16.dp
        )
)

val Shapes.tag: CornerBasedShape
        get() = RoundedCornerShape(percent = 50)

@Composable
fun SpaceTheme(
        isDarkTheme: Boolean = isSystemInDarkTheme(),
        colors: Colors? = null,
        content: @Composable () -> Unit
) {
        val myColors = colors ?: if (isDarkTheme) SpaceDarkPalette else SpaceLightPalette

        MaterialTheme(
                content = content,
                colors = myColors,
                shapes = Shapes,
                typography = SpaceTypography,
        )
}