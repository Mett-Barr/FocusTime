package com.example.focustime.ui.test

import android.graphics.BlurMaskFilter
import android.util.Log
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.*
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.util.*
import kotlin.math.PI
import kotlin.math.roundToInt
import kotlin.math.sin

@Preview
@Composable
fun Incense(
    waveAmplitude: Float = 300f,
    waveLengthRatio: Float = 0.5f,
    burnDuration: Int = 15 * 60 * 1000
) {
    // Constants
    val screenTop = 0f
    val incenseTop = 0.3f
    val incenseEnd = 0.85f
    val incenseWidth = 20f
    val smokeAnimationDuration = 10000
    val waveCycle = (smokeAnimationDuration * waveLengthRatio / (incenseTop - screenTop)).roundToInt()

    // Animations
    val infiniteTransition = rememberInfiniteTransition()
    val wavePhase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 2 * PI.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = waveCycle, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )

    // Incense burning state
    var isBurning by remember {
        mutableStateOf(false)
    }

    // Calculate the end time and smokeEndY based on burnDuration
    val endTime = remember { Calendar.getInstance().timeInMillis + burnDuration }
    var smokeEndY by remember { mutableStateOf(0f) }

    LaunchedEffect(Unit) {
        withContext(Dispatchers.Default) {
            isBurning = true

            while (isBurning) {
                val remainingTimeRatio =
                    ((endTime - Calendar.getInstance().timeInMillis) / burnDuration.toFloat()).coerceIn(
                        0f,
                        1f
                    )
                smokeEndY = incenseTop + (incenseEnd - incenseTop) * (1 - remainingTimeRatio)
                if (smokeEndY >= incenseEnd) isBurning = false
                delay(10)

                Log.d("!!!", "WaveTest: $smokeEndY")
            }
        }
    }

    // Initial smoke start position
    var isInitial by remember { mutableStateOf(true) }
    val smokeStartY by animateFloatAsState(
        targetValue = if (isInitial) incenseTop else screenTop,
        animationSpec = tween(smokeAnimationDuration, easing = LinearEasing)
    )
    LaunchedEffect(Unit) {
        delay(3000)
        isInitial = false
    }

    // Smoke extremity animation
    val smokeExtremity by animateFloatAsState(
        targetValue = if (smokeEndY < incenseEnd) 1f else 0f,
        animationSpec = tween((smokeAnimationDuration / incenseTop * incenseEnd).toInt(), easing = LinearEasing)
    )

    // Flame animations
    val flameAlpha by animateFloatAsState(if (isBurning) 1f else 0f, animationSpec = tween(3000, easing = LinearEasing))
    val flameBrightness by infiniteTransition.animateFloat(
        initialValue = 0.9f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(600, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    // Drawing components
    val smokePath = remember { Path() }
    Box(modifier = Modifier.fillMaxSize()) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawIncense(
                incenseTop = incenseTop,
                incenseEnd = incenseEnd,
                incenseWidth = 20f,
                offsetY = smokeEndY
            )

            drawSmoke(
                waveAmplitude = waveAmplitude,
                waveLengthRatio = waveLengthRatio,
                wavePhase = wavePhase,
                smokeEndY = smokeEndY,
                smokeStartY = smokeStartY,
                smokePath = smokePath,
                smokeExtremity = smokeExtremity
            )

            drawFlame(
                incenseWidth = incenseWidth,
                incenseTop = smokeEndY,
                flameAlpha = flameAlpha,
                flameBrightness = flameBrightness,
            )
        }
    }
}

fun DrawScope.drawSmoke(
    waveAmplitude: Float,
    waveLengthRatio: Float,
    wavePhase: Float,
    smokeEndY: Float,
    smokeStartY: Float,
    smokePath: Path,
    smokeExtremity: Float
) {
    smokePath.reset()

    val heightRange = ((size.height * (smokeEndY - smokeStartY)).roundToInt())

    val smokeExtremityY = smokeExtremity * size.height * (smokeEndY - smokeStartY)

    for (i in 0..heightRange step 1) {
        val yOffset = size.height * smokeStartY + i

        val adjustedWaveAmplitude =
            waveAmplitude * ((size.height * smokeEndY - yOffset) / (size.height))
        val phaseDelta = 2 * PI.toFloat() * (yOffset / (size.height * waveLengthRatio))
        val xOffset = size.width / 2f + adjustedWaveAmplitude * sin(wavePhase + phaseDelta)

        if (i == 0) {
            smokePath.moveTo(xOffset, yOffset)
        } else if (i <= smokeExtremityY) {
            smokePath.lineTo(xOffset, yOffset)
        }
    }

    drawIntoCanvas { canvas ->
        val blurMaskFilter = BlurMaskFilter(30f, BlurMaskFilter.Blur.NORMAL)
        val paint = android.graphics.Paint().apply {
            maskFilter = blurMaskFilter
            color = android.graphics.Color.GRAY
            strokeWidth = 30f
            style = android.graphics.Paint.Style.STROKE
            isAntiAlias = true
        }

        canvas.nativeCanvas.drawPath(smokePath.asAndroidPath(), paint)
    }
}

fun DrawScope.drawIncense(
    incenseTop: Float,
    incenseEnd: Float,
    incenseWidth: Float,
    offsetY: Float,
    isInitial: Boolean = true
) {
    val incenseColor = Color(165, 119, 96)
    val stripeColor = Color(120, 80, 60)
    val handleColor = Color(128, 0, 0)
    val handleWidth = 5f

    val incenseExtremity = incenseEnd + 0.05f

    val cornerRadius = incenseWidth / 2
    val incenseHeight = size.height * (incenseEnd - incenseTop)

    val clipPath = Path().apply {
        moveTo(size.width / 2 - incenseWidth / 2, size.height * offsetY)
        arcTo(
            rect = Rect(
                left = size.width / 2 - incenseWidth / 2,
                top = size.height * offsetY - cornerRadius * (1 - offsetY),
                right = size.width / 2 + incenseWidth / 2,
                bottom = size.height * offsetY + cornerRadius * (1 - offsetY)
            ),
            startAngleDegrees = 180f,
            sweepAngleDegrees = 180f,
            forceMoveTo = false
        )
        lineTo(size.width / 2 + incenseWidth / 2, size.height * incenseEnd)
        arcTo(
            rect = Rect(
                left = size.width / 2 - incenseWidth / 2,
                top = size.height * incenseEnd - cornerRadius * (1 - offsetY),
                right = size.width / 2 + incenseWidth / 2,
                bottom = size.height * incenseEnd + cornerRadius * (1 - offsetY)
            ),
            startAngleDegrees = 0f,
            sweepAngleDegrees = 180f,
            forceMoveTo = false
        )
        close()
    }

    val handlePath = Path().apply {
        addRoundRect(
            roundRect = RoundRect(
                left = size.width / 2 - handleWidth / 2,
                top = size.height * incenseExtremity,
                right = size.width / 2 + handleWidth / 2,
                bottom = size.height,
                radiusX = handleWidth / 2,
                radiusY = handleWidth / 2
            )
        )
    }
    drawPath(path = handlePath, color = handleColor, style = Fill)

    val nonCombustiblePath = Path().apply {
        moveTo(size.width / 2 - incenseWidth / 2, size.height * incenseEnd)
        arcTo(
            rect = Rect(
                left = size.width / 2 - incenseWidth / 2,
                top = size.height * incenseEnd - cornerRadius,
                right = size.width / 2 + incenseWidth / 2,
                bottom = size.height * incenseEnd + cornerRadius
            ),
            startAngleDegrees = 180f,
            sweepAngleDegrees = 180f,
            forceMoveTo = false
        )
        lineTo(size.width / 2 + incenseWidth / 2, size.height * incenseExtremity)
        arcTo(
            rect = Rect(
                left = size.width / 2 - incenseWidth / 2,
                top = size.height * incenseExtremity - cornerRadius,
                right = size.width / 2 + incenseWidth / 2,
                bottom = size.height * incenseExtremity + cornerRadius
            ),
            startAngleDegrees = 0f,
            sweepAngleDegrees = 180f,
            forceMoveTo = false
        )
        lineTo(size.width / 2 - incenseWidth / 2, size.height * incenseEnd)
        close()
    }
    drawPath(path = nonCombustiblePath, color = stripeColor, style = Fill)

    clipPath(path = clipPath, clipOp = ClipOp.Intersect) {
        drawPath(path = clipPath, color = incenseColor, style = Fill)

        val stripeCount = 30
        val stripeSpacing = incenseHeight / stripeCount

        for (i in 0..stripeCount) {
            val y = size.height * incenseTop + i * stripeSpacing

            drawPath(
                path = Path().apply {
                    arcTo(
                        rect = Rect(
                            left = size.width / 2 - incenseWidth / 2,
                            top = y - cornerRadius,
                            right = size.width / 2 + incenseWidth / 2,
                            bottom = y + cornerRadius
                        ),
                        startAngleDegrees = 0f,
                        sweepAngleDegrees = 180f,
                        forceMoveTo = true
                    )
                },
                color = stripeColor,
                style = Stroke(width = 5f)
            )
        }
    }

    if (isInitial) {
        drawAsh(incenseWidth = incenseWidth, incenseTop = offsetY)
    }
}

fun DrawScope.drawAsh(incenseWidth: Float, incenseTop: Float) {
    val ashColor = Color.Gray
    val ashWidth = incenseWidth
    val ashHeight = incenseWidth
    val ashRadius = ashWidth / 2

    drawRoundRect(
        color = ashColor,
        topLeft = Offset(size.width / 2 - ashWidth / 2, size.height * incenseTop - ashHeight / 2),
        size = Size(ashWidth, ashHeight),
        cornerRadius = CornerRadius(ashRadius, ashRadius)
    )
}

fun DrawScope.drawFlame(incenseWidth: Float, incenseTop: Float) {
    drawIntoCanvas { canvas ->
        val blurMaskFilter = BlurMaskFilter(incenseWidth / 2, BlurMaskFilter.Blur.NORMAL)
        val paint = android.graphics.Paint().apply {
            maskFilter = blurMaskFilter
            color = android.graphics.Color.argb(150, 255, 140, 0) // Faint orange color
            style = android.graphics.Paint.Style.FILL
            isAntiAlias = true
        }

        canvas.nativeCanvas.drawCircle(
            size.width / 2,
            size.height * incenseTop,
            incenseWidth,
            paint
        )
    }
}

fun DrawScope.drawFlame(
    incenseWidth: Float,
    incenseTop: Float,
    flameAlpha: Float,
    flameBrightness: Float
) {
    drawIntoCanvas { canvas ->
        val blurMaskFilter = BlurMaskFilter(incenseWidth / 2, BlurMaskFilter.Blur.NORMAL)
        val paint = android.graphics.Paint().apply {
            maskFilter = blurMaskFilter
            color = android.graphics.Color.argb(
                (150 * flameAlpha * flameBrightness).toInt(),
                255,
                140,
                0
            ) // Faint orange color
            style = android.graphics.Paint.Style.FILL
            isAntiAlias = true
        }

        canvas.nativeCanvas.drawCircle(
            size.width / 2,
            size.height * incenseTop,
            incenseWidth,
            paint
        )
    }
}

