package sk.kasper.ui_common.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val Cyan500 = Color(0xFF00BCD4)
private val Cyan700 = Color(0xFF0097A7)
private val Cyan200 = Color(0xFF80DEEA)
private val Blue500 = Color(0xff2196f3)

private val SpaceLightPalette = lightColors(
        primary = Cyan500,
        primaryVariant = Cyan500,
        secondary = Blue500,
)

private val SpaceDarkPalette = darkColors(
        primary = Cyan700,
        primaryVariant = Cyan500,
        secondary = Blue500,
        onSurface = Cyan200,
        surface = Color.Black,
)

@Composable
fun SpaceTheme(
        isDarkTheme: Boolean = isSystemInDarkTheme(),
        colors: Colors? = null,
        content: @Composable () -> Unit
) {
    val myColors = colors ?: if (isDarkTheme) SpaceDarkPalette else SpaceLightPalette

    MaterialTheme(
            colors = myColors,
            content = content,
            typography = SpaceTypography,
    )
}