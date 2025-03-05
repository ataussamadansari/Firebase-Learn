package com.example.firebase.canvas

import android.text.TextPaint
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp

@Composable
fun AnimatedWavingText() {
    val isDarkTheme = MaterialTheme.colorScheme.background.luminance() < 0.5f
    val textColor = if (isDarkTheme) Color.White else Color.Black

    val textPaint = remember {
        TextPaint().apply {
            color = textColor.toArgb()
            textSize = 100f
            isAntiAlias = true
        }
    }

    // ✅ Wave Animation for each letter
    val infiniteTransition = rememberInfiniteTransition()
    val waveAnimation = List(7) { index ->
        infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = 20f,
            animationSpec = infiniteRepeatable(
                animation = tween(600, easing = LinearEasing),
                repeatMode = RepeatMode.Reverse
            ),
            label = "wave_$index"
        )
    }

    // ✅ Bounce Animation for whole text
    val bounceAnimation = infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 30f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "bounce"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val baseX = size.width / 3
            val baseY = size.height / 2 + bounceAnimation.value

            val text = "Loading..."
            text.forEachIndexed { index, char ->
                drawContext.canvas.nativeCanvas.drawText(
                    char.toString(),
                    baseX + index * 60f,
                    baseY + waveAnimation[index % waveAnimation.size].value,
                    textPaint
                )
            }
        }
    }
}
