package sk.kasper.space.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val Cyan500 = Color(0x00BCD4)
private val Cyan700 = Color(0x0097A7)
private val Cyan200 = Color(0x80DEEA)
private val Blue500 = Color(0x2196f3)

private val SpaceLightPalette = lightColors(
        primary = Cyan500,
        primaryVariant = Cyan500,
        onPrimary = Color.White,
        secondary = Blue500,
        onSecondary = Color.Black,
        onSurface = Color.Black,
        onBackground = Color.Black,
)

private val SpaceDarkPalette = darkColors(
        primary = Cyan700,
        primaryVariant = Cyan500,
        onPrimary = Color.Black,
        secondary = Blue500,
        onSurface = Cyan200,
        onBackground = Color.White,
        background = Color.Black,
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