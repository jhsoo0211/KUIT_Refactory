package com.example.kuitandroidapiexample.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

val White = Color(0xFFFFFFFF)
val Black = Color(0xFF111111)

val Green = Color(0xFF10B436)
val LightGreen = Color(0xFFDEFDE4)
val Blue = Color(0xFF1B64DA)
val LightBlue = Color(0xFFDEEBFD)
val Red = Color(0xFFEF4346)
val LightRed = Color(0xFFFFE7E8)

val Orange = Color(0xFFFFA938)

val Gray100 = Color(0xFF585858)
val Gray200 = Color(0xFFF3F3F3)


@Immutable
data class FindUColors(
    val white: Color,
    val black: Color,
    val green: Color,
    val lightGreen: Color,
    val blue: Color,
    val lightBlue: Color,
    val red: Color,
    val lightRed: Color,
    val orange: Color,
    val gray100: Color,
    val gray200: Color
)

val defaultFindUColors = FindUColors(
    white = White,
    black = Black,
    green = Green,
    lightGreen = LightGreen,
    blue = Blue,
    lightBlue = LightBlue,
    red = Red,
    lightRed = LightRed,
    orange = Orange,
    gray100 = Gray100,
    gray200 = Gray200
)

val LocalFindUColorsProvider = staticCompositionLocalOf { defaultFindUColors }

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)