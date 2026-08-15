package com.konkuk.moru.core.util

import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.AdaptiveIconDrawable
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.core.graphics.createBitmap

fun Drawable.toBitmapSafe(): Bitmap {
    return when (this) {
        is BitmapDrawable -> this.bitmap
        is AdaptiveIconDrawable -> {
            val bmp = createBitmap(intrinsicWidth.coerceAtLeast(1), intrinsicHeight.coerceAtLeast(1))
            val canvas = Canvas(bmp)
            setBounds(0, 0, canvas.width, canvas.height)
            draw(canvas)
            bmp
        }
        else -> {
            val bmp = createBitmap(intrinsicWidth.coerceAtLeast(1), intrinsicHeight.coerceAtLeast(1))
            val canvas = Canvas(bmp)
            setBounds(0, 0, canvas.width, canvas.height)
            draw(canvas)
            bmp
        }
    }
}

fun getAppIconImageBitmap(pm: PackageManager, packageName: String): ImageBitmap? = runCatching {
    val drawable = pm.getApplicationIcon(packageName)
    drawable.toBitmapSafe().asImageBitmap()
}.getOrNull()

fun getAppLabel(pm: PackageManager, packageName: String): String? = runCatching {
    val ai = pm.getApplicationInfo(packageName, 0)
    pm.getApplicationLabel(ai).toString()
}.getOrNull()