package sk.kasper.ui_common.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import sk.kasper.ui_common.R

val Montserrat = FontFamily(
    Font(R.font.montserrat_regular),
    Font(R.font.montserrat_medium, FontWeight.Medium),
    Font(R.font.montserrat_bold, FontWeight.Bold),
    Font(R.font.montserrat_semibold, FontWeight.SemiBold),
)

val SourceSansPro = FontFamily(
    Font(R.font.source_sans_pro_regular),
    Font(R.font.source_sans_pro_semibold, FontWeight.SemiBold),
    Font(R.font.source_sans_pro_bold, FontWeight.Bold),
)

val SpaceTypography = Typography(
    h1 = TextStyle(
        fontFamily = Montserrat,
        fontSize = 96.sp
    ),
    h2 = TextStyle(
        fontFamily = Montserrat,
        fontSize = 60.sp
    ),
    h3 = TextStyle(
        fontFamily = Montserrat,
        fontSize = 48.sp
    ),
    h4 = TextStyle(
        fontFamily = Montserrat,
        fontWeight = FontWeight.Medium,
        fontSize = 32.sp
    ),
    h5 = TextStyle(
        fontFamily = Montserrat,
        fontWeight = FontWeight.Medium,
        fontSize = 24.sp
    ),
    h6 = TextStyle(
        fontFamily = Montserrat,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp
    ),
    subtitle1 = TextStyle(
        fontFamily = Montserrat,
        fontWeight = FontWeight.Normal,
        fontSize = 18.sp,
        letterSpacing = 0.15.sp
    ),
    subtitle2 = TextStyle(
        fontFamily = Montserrat,
        fontSize = 16.sp,
        fontWeight = FontWeight.Medium,
    ),
    body1 = TextStyle(
        fontFamily = SourceSansPro,
        fontSize = 18.sp,
        letterSpacing = 0.5.sp
    ),
    body2 = TextStyle(
        fontFamily = SourceSansPro,
        fontSize = 16.sp,
        letterSpacing = 0.25.sp
    ),
    button = TextStyle(
        fontFamily = SourceSansPro,
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp,
        letterSpacing = 1.25.sp
    ),
    caption = TextStyle(
        fontFamily = SourceSansPro,
        fontSize = 14.sp
    ),
    overline = TextStyle(
        fontFamily = SourceSansPro,
        fontSize = 12.sp,
        letterSpacing = 1.5.sp
    )
)

val Typography.section: TextStyle
    get() = TextStyle(
        fontWeight = FontWeight.Medium,
        fontFamily = Montserrat,
        fontSize = 22.sp
    )
