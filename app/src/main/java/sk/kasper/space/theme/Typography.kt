package sk.kasper.space.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.font
import androidx.compose.ui.text.font.fontFamily
import androidx.compose.ui.unit.sp
import sk.kasper.space.R

private val SourceSansProFamily = fontFamily(
    font(R.font.source_sans_pro),
    font(R.font.source_sans_pro_semibold, FontWeight.SemiBold)
)

private val MontserratFontFamily = fontFamily(
    font(R.font.montserrat_regular),
    font(R.font.montserrat_medium, FontWeight.Medium),
)

val SpaceTypography = Typography(
    defaultFontFamily = SourceSansProFamily,
    h6 = TextStyle(
        fontFamily = MontserratFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
    ),
)
