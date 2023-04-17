package com.example.focustime.ui.page

import android.graphics.BlurMaskFilter
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.*
import androidx.compose.ui.graphics.ClipOp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.*
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.tooling.preview.Preview
import com.example.focustime.ui.test.drawFlame
import kotlin.random.Random

@Composable
fun DrawIncense(incenseTop: Float, incenseEnd: Float, incenseWidth: Float) {
    val randomDots = remember { List(5) { Random.nextFloat() } }
    val incenseHeight = 400f

    Box(modifier = Modifier.fillMaxSize()) {
        // Draw the incense stick with rounded edges
        Canvas(modifier = Modifier.fillMaxSize()) {
            val cornerRadius = incenseWidth / 2
            drawRoundRect(
                color = Color(165, 119, 96),
                topLeft = Offset(
                    x = size.width / 2 - incenseWidth / 2,
                    y = size.height * incenseTop
                ),
                size = Size(width = incenseWidth, height = incenseHeight),
                cornerRadius = CornerRadius(cornerRadius, cornerRadius),
                style = Fill
            )

            // Draw multiple small fixed dots on the incense stick
            val dotRadius = incenseWidth / 8
            randomDots.forEach { randomPosition ->
                drawCircle(
                    color = Color(80, 50, 40),
                    center = Offset(
                        x = size.width / 2,
                        y = size.height * incenseTop + incenseHeight * randomPosition
                    ),
                    radius = dotRadius,
                    style = Fill
                )
            }

            // Draw the faint flame
            drawFlame(incenseWidth = incenseWidth, incenseTop = incenseTop)
//            val flameRadius = incenseWidth * 1.5f
//            drawIntoCanvas { canvas ->
//                val blurMaskFilter = BlurMaskFilter(flameRadius / 2, BlurMaskFilter.Blur.NORMAL)
//                val paint = android.graphics.Paint().apply {
//                    maskFilter = blurMaskFilter
//                    color = android.graphics.Color.argb(150, 255, 140, 0) // Faint orange color
//                    style = android.graphics.Paint.Style.FILL
//                    isAntiAlias = true
//                }
//
//                canvas.nativeCanvas.drawCircle(
//                    size.width / 2,
//                    size.height * incenseTop,
//                    flameRadius,
//                    paint
//                )
//            }
        }
    }
}


@Preview
@Composable
fun DrawIncensePreview() {
    val incenseTop = remember { Animatable(0.3f) }

    LaunchedEffect(Unit) {
        with(incenseTop) {
            while (true) {
                animateTo(0.3f, tween(2000))
                animateTo(0.7f, tween(2000))
            }
        }
    }

    DrawIncense(incenseTop = 0.3f, incenseEnd = 0.7f, incenseWidth = 20f)
//    DrawIncense(incenseTop = incenseTop.value, incenseEnd = 0.7f, incenseWidth = 20f)
}

//fun DrawScope.drawFlame(incenseWidth: Float, incenseTop: Float) {
//    val flameRadius = incenseWidth * 1.5f
//    drawIntoCanvas { canvas ->
//        val blurMaskFilter = BlurMaskFilter(flameRadius / 2, BlurMaskFilter.Blur.NORMAL)
//        val paint = android.graphics.Paint().apply {
//            maskFilter = blurMaskFilter
//            color = android.graphics.Color.argb(150, 255, 140, 0) // Faint orange color
//            style = android.graphics.Paint.Style.FILL
//            isAntiAlias = true
//        }
//
//        canvas.nativeCanvas.drawCircle(
//            size.width / 2,
//            size.height * incenseTop,
//            flameRadius,
//            paint
//        )
//    }
//}

@Preview
@Composable
fun DrawIncenseTest(
    burnDuration: Int = 3000
) {

    val incenseTop = 0.35f
    val incenseEnd = 0.9f
    val incenseWidth = 20f

    // Create an infinite animation
    val infiniteTransition = rememberInfiniteTransition()
    val offsetY by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = (incenseEnd - incenseTop),
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = burnDuration,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Restart
        )
    )

    val incenseColor = Color(165, 119, 96)
    val stripeColor = Color(120, 80, 60)
    val handleColor = Color(128, 0, 0)
    val handleWidth = 5f

    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color.White)) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val cornerRadius = incenseWidth / 2
            val incenseHeight = size.height * (incenseEnd - incenseTop)

            // Create the clip path
            val path = Path()
            path.addRoundRect(
                roundRect = RoundRect(
                    left = size.width / 2 - incenseWidth / 2,
                    top = size.height * incenseTop + offsetY * size.height,
                    right = size.width / 2 + incenseWidth / 2,
                    bottom = size.height * incenseEnd,
                    radiusX = cornerRadius,
                    radiusY = cornerRadius
                )
            )

            // Clip the incense stick with the animated offset
            clipPath(
                path = path,
                clipOp = ClipOp.Intersect
            ) {
                // Draw the incense stick
                drawRoundRect(
                    color = incenseColor,
                    topLeft = Offset(
                        x = size.width / 2 - incenseWidth / 2,
                        y = size.height * incenseTop
                    ),
                    size = Size(width = incenseWidth, height = incenseHeight),
                    cornerRadius = CornerRadius(cornerRadius, cornerRadius),
                    style = Fill
                )

                // Draw curved horizontal stripes
                val stripeCount = 30
                val stripeSpacing = incenseHeight / stripeCount

                for (i in 0..stripeCount) {
                    val y = size.height * incenseTop + i * stripeSpacing

                    // Draw the curved horizontal stripe
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

            // Draw the handle
            drawRoundRect(
                color = handleColor,
                topLeft = Offset(
                    x = size.width / 2 - handleWidth / 2,
                    y = size.height * incenseEnd
                ),
                size = Size(width = handleWidth, height = size.height * (1 - incenseEnd)),
                cornerRadius = CornerRadius(handleWidth / 2, handleWidth / 2),
                style = Fill
            )
        }
    }
}

@Preview
@Composable
fun DrawIncenseTest2(
    incenseTop: Float = 0.2f,
    incenseEnd: Float = 0.8f,
    incenseWidth: Float = 20f
) {
    // Create an infinite animation
    val animationDurationMillis = 3000
    val infiniteTransition = rememberInfiniteTransition()
    val offsetY by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = (incenseEnd - incenseTop),
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = animationDurationMillis,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Restart
        )
    )

    // Generate texture for the incense
    val textureRowCount = 40
    val textureColumnCount = 10
    val texture = remember {
        List(textureRowCount) { rowIndex ->
            List(textureColumnCount) { columnIndex ->
                val baseColor = Color(165, 119, 96)
                val randomAdjustment = Random.nextInt(-20, 20)
                baseColor.copy(
                        red = (baseColor.red + randomAdjustment).coerceIn(0f, 1f),
                    green = (baseColor.green + randomAdjustment).coerceIn(0f, 1f),
                    blue = (baseColor.blue + randomAdjustment).coerceIn(0f, 1f)
                )
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val cornerRadius = incenseWidth / 2
            val incenseHeight = size.height * (incenseEnd - incenseTop)

            // Create the clip path
            val path = Path()
            path.addRoundRect(
                roundRect = RoundRect(
                    left = size.width / 2 - incenseWidth / 2,
                    top = size.height * incenseTop + offsetY * size.height,
                    right = size.width / 2 + incenseWidth / 2,
                    bottom = size.height * incenseEnd,
                    radiusX = cornerRadius,
                    radiusY = cornerRadius
                )
            )

            // Clip the incense stick with the animated offset
            clipPath(
                path = path,
                clipOp = ClipOp.Intersect
            ) {
                // Draw the incense stick with texture
                for (row in 0 until textureRowCount) {
                    for (column in 0 until textureColumnCount) {
                        drawRect(
                            color = texture[row][column],
                            topLeft = Offset(
                                x = size.width / 2 - incenseWidth / 2 + column * (incenseWidth / textureColumnCount),
                                y = size.height * incenseTop + row * (incenseHeight / textureRowCount)
                            ),
                            size = Size(
                                width = incenseWidth / textureColumnCount,
                                height = incenseHeight /
                                        textureRowCount
                            )
                        )
                    }
                }

                // Draw the incense stick edges
                drawRoundRect(
                    color = Color(90, 60, 50),
                    topLeft = Offset(
                        x = size.width / 2 - incenseWidth / 2,
                        y = size.height * incenseTop
                    ),
                    size = Size(width = incenseWidth, height = incenseHeight),
                    cornerRadius = CornerRadius(cornerRadius, cornerRadius),
                    style = Stroke(width = 1f)
                )
            }
        }
    }
}

fun randomColorVariation(baseColor: Color, maxAdjustment: Float = 0.1f): Color {
    val randomAdjustment = (Random.nextFloat() * maxAdjustment) - (maxAdjustment / 2)
    return baseColor.copy(
        red = (baseColor.red + randomAdjustment).coerceIn(0f, 1f),
        green = (baseColor.green + randomAdjustment).coerceIn(0f, 1f),
        blue = (baseColor.blue + randomAdjustment).coerceIn(0f, 1f)
    )
}
