package com.example.firebase.canvas

import android.text.TextPaint
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import android.graphics.Color
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.unit.dp
import kotlin.math.sin

@Composable
fun WavingText(modifier: Modifier = Modifier, text: String = "Loading...") {
    val textColor = if (isSystemInDarkTheme()) Color.WHITE else Color.BLACK
    val textPaint = remember {
        TextPaint().apply {
            color = textColor
            textSize = 100f
            isAntiAlias = true
        }
    }

    val infiniteTransition = rememberInfiniteTransition()
    val waveOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 2 * Math.PI.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )

    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(100.dp)
    ) {
        val canvasWidth = size.width
        val startX = (canvasWidth - textPaint.measureText(text)) / 2
        val baselineY = size.height / 2

        drawIntoCanvas { canvas ->
            text.forEachIndexed { index, char ->
                val charX = startX + textPaint.measureText(text.substring(0, index))
                val waveY = baselineY + sin(waveOffset + index * 0.5f) * 20f // 🔹 Waving effect

                canvas.nativeCanvas.drawText(char.toString(), charX, waveY, textPaint)
            }
        }
    }
}
