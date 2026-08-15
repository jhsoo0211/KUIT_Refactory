package com.konkuk.moru.data.model

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.core.graphics.createBitmap

data class UsedAppInRoutine(
    val appName: String,
    val appIcon: ImageBitmap,
    val packageName: String
)

fun placeholderIcon(size: Int = 100): ImageBitmap {
    val bitmap = createBitmap(size, size)
    val canvas = android.graphics.Canvas(bitmap)
    val paint = android.graphics.Paint().apply {
        color = android.graphics.Color.LTGRAY
        style = android.graphics.Paint.Style.FILL
    }
    canvas.drawRect(0f, 0f, size.toFloat(), size.toFloat(), paint)
    return bitmap.asImageBitmap()
}
