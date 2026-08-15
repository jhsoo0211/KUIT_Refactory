package com.konkuk.moru.ui.theme

import android.R.attr.fontFamily
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.unit.sp
import com.konkuk.moru.R

val moruFontExtraBold = FontFamily(Font(R.font.pretendard_extra_bold))
val moruFontBold = FontFamily(Font(R.font.pretendard_bold))
val moruFontSemiBold = FontFamily(Font(R.font.pretendard_semi_bold))
val moruFontMedium = FontFamily(Font(R.font.pretendard_medium))
val moruFontRegular = FontFamily(Font(R.font.pretendard_regular))
val moruFontLight = FontFamily(Font(R.font.pretendard_light))

@Immutable
data class MoruTypography(
    val head_EB_24: TextStyle,

    val title_B_24: TextStyle,
    val title_B_20: TextStyle,
    val title_B_14: TextStyle,
    val title_B_12: TextStyle,

    val body_SB_24: TextStyle,
    val body_SB_20: TextStyle,
    val body_SB_16: TextStyle,
    val body_SB_14: TextStyle,

    val desc_M_20: TextStyle,
    val desc_M_16: TextStyle,
    val desc_M_14: TextStyle,
    val desc_M_12: TextStyle,

    val time_R_24: TextStyle,
    val time_R_16: TextStyle,
    val time_R_14: TextStyle,
    val time_R_12: TextStyle,
    val time_R_10: TextStyle,

    val caption_L_12: TextStyle
)

val defaultMoruTypography = MoruTypography(
    head_EB_24 = TextStyle(
        fontFamily = moruFontExtraBold,
        fontSize = 24.sp,
        lineHeight = 24.sp
    ),

    title_B_24 = TextStyle(
        fontFamily = moruFontBold,
        fontSize = 24.sp,
        lineHeight = 24.sp
    ),
    title_B_20 = TextStyle(
        fontFamily = moruFontBold,
        fontSize = 20.sp,
        lineHeight = 20.sp
    ),
    title_B_14 = TextStyle(
        fontFamily = moruFontBold,
        fontSize = 14.sp,
        lineHeight = 14.sp
    ),
    title_B_12 = TextStyle(
        fontFamily = moruFontBold,
        fontSize = 12.sp,
        lineHeight = 12.sp
    ),

    body_SB_24 = TextStyle(
        fontFamily = moruFontSemiBold,
        fontSize = 24.sp,
        lineHeight = 24.sp
    ),
    body_SB_20 = TextStyle(
        fontFamily = moruFontSemiBold,
        fontSize = 20.sp,
        lineHeight = 20.sp
    ),
    body_SB_16 = TextStyle(
        fontFamily = moruFontSemiBold,
        fontSize = 16.sp,
        lineHeight = 16.sp
    ),
    body_SB_14 = TextStyle(
        fontFamily = moruFontSemiBold,
        fontSize = 14.sp,
        lineHeight = 14.sp
    ),

    desc_M_20 = TextStyle(
        fontFamily = moruFontMedium,
        fontSize = 20.sp,
        lineHeight = 20.sp
    ),
    desc_M_16 = TextStyle(
        fontFamily = moruFontMedium,
        fontSize = 16.sp,
        lineHeight = 16.sp
    ),
    desc_M_14 = TextStyle(
        fontFamily = moruFontMedium,
        fontSize = 14.sp,
        lineHeight = 14.sp
    ),
    desc_M_12 = TextStyle(
        fontFamily = moruFontMedium,
        fontSize = 12.sp,
        lineHeight = 12.sp
    ),

    time_R_24 = TextStyle(
        fontFamily = moruFontRegular,
        fontSize = 24.sp,
        lineHeight = 24.sp
    ),
    time_R_16 = TextStyle(
        fontFamily = moruFontRegular,
        fontSize = 16.sp,
        lineHeight = 16.sp
    ),
    time_R_14 = TextStyle(
        fontFamily = moruFontRegular,
        fontSize = 14.sp,
        lineHeight = 14.sp
    ),
    time_R_12 = TextStyle(
        fontFamily = moruFontRegular,
        fontSize = 12.sp,
        lineHeight = 12.sp
    ),
    time_R_10 = TextStyle(
        fontFamily = moruFontRegular,
        fontSize = 10.sp,
        lineHeight = 10.sp
    ),

    caption_L_12 = TextStyle(
        fontFamily = moruFontLight,
        fontSize = 12.sp,
        lineHeight = 12.sp
    )
)

val LocalMoruTypographyProvider = staticCompositionLocalOf { defaultMoruTypography }