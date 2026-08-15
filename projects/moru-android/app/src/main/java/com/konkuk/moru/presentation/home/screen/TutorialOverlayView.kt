// TutorialOverlayView.kt
package com.konkuk.moru.presentation.home.screen

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.util.AttributeSet
import android.view.View

class TutorialOverlayView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : View(context, attrs) {

    var holes: List<HolePx> = emptyList()
        set(value) {
            field = value
            invalidate()
        }

    private val overlayPaint = Paint().apply {
        // Android View 클래스(TutorialOverlayView)에서는 Compose Color를 쓸 수 없기 때문에 직접 색 명시함
        color = Color.parseColor("#80000000")
    }

    private val clearPaint = Paint().apply {
        xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
        isAntiAlias = true
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val saved = canvas.saveLayer(null, null)
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), overlayPaint)

        holes.forEach { hole ->
            if (hole.isCircle) {
                canvas.drawCircle(hole.centerX, hole.centerY, hole.radius, clearPaint)
            } else {
                canvas.drawRoundRect(
                    hole.left, hole.top, hole.right, hole.bottom,
                    hole.cornerRadius, hole.cornerRadius,
                    clearPaint
                )
            }
        }

        canvas.restoreToCount(saved)
    }

    data class HolePx(
        val left: Float,
        val top: Float,
        val right: Float,
        val bottom: Float,
        val cornerRadius: Float = 0f,
        val isCircle: Boolean = false
    ) {
        val centerX: Float get() = (left + right) / 2
        val centerY: Float get() = (top + bottom) / 2
        val radius: Float get() = (right - left) / 2
    }
}