package com.example.kuitandroidapiexample.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.kuitandroidapiexample.R

val findUFontSemiBold = FontFamily(Font(R.font.pretendard_semibold))
val findUFontRegular = FontFamily(Font(R.font.pretendard_regular))

@Immutable
data class FindUTypography(
    val semiBold : TextStyle,
    val regular : TextStyle,
)

val defaultFindUTypography = FindUTypography(
    semiBold = TextStyle(
        fontFamily = findUFontSemiBold,
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp
    ),
    regular = TextStyle(
        fontFamily = findUFontRegular,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    )
)

val LocalFindUTypographyProvider = staticCompositionLocalOf { defaultFindUTypography }




// Set of Material typography styles to start with
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )
    /* Other default text styles to override
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
    */
)