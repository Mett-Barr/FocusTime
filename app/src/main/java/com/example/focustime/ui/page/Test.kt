package com.example.focustime.ui.page

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.glance.LocalSize
import kotlin.math.PI
import kotlin.math.sin

@Composable
fun Dp.dpToPx() = with(LocalDensity.current) { this@dpToPx.toPx() }

@Composable
@Preview
fun PxTest() {
    Box(modifier = Modifier.fillMaxSize())

    val height = LocalSize.current.height
    val px = height.dpToPx()
}

@Composable
fun RisingSmokeAnimation() {
    val infiniteTransition = rememberInfiniteTransition()
    val smokeAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )
    val smokeOffsetY by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = -200f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )


    val smokePath = remember { Path() }
    val swayOffset by infiniteTransition.animateFloat(
        initialValue = -20f,
        targetValue = 20f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )

    Box(modifier = Modifier.fillMaxSize()) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer(translationY = smokeOffsetY)
        ) {
            smokePath.reset()
            smokePath.moveTo(size.width / 2, size.height - 20f)
            val yOffset = size.height / 2
            val xOffset = size.width / 2 + swayOffset * sin((size.height - yOffset) / 100)
            smokePath.cubicTo(
                xOffset - 50, yOffset + 50,
                xOffset + 50, yOffset - 50,
                size.width / 2, 0f
            )

            drawPath(
                path = smokePath,
                color = Color.Gray,
                style = Stroke(width = 4f)
            )
        }
    }
}

@Composable
fun CenteredVerticalWaveAnimation() {

    // Get local density from composable
    val localDensity = LocalDensity.current

    // Create element height in pixel state
    var columnHeightPx by remember {
        mutableStateOf(0f)
    }

    // Create element height in dp state
    var columnHeightDp by remember {
        mutableStateOf(0.dp)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .onGloballyPositioned { coordinates ->
                // Set column height using the LayoutCoordinates
                columnHeightPx = coordinates.size.height.toFloat()
                columnHeightDp = with(localDensity) { coordinates.size.height.toDp() }
            }
    ) {
    }


    // 移動距離
//    val fillHeight = (LocalSize.current.height.value * 0.65).dp.dpToPx()
//        val fillHeight = with(LocalDensity.current) { LocalSize.current.height.value * 0.65 }
    // 移動時間
    val time = 8000
    // 速度
    val va = remember { derivedStateOf { columnHeightPx / time }}
    // 波長
    val waveL = 30f
    // 週期
    val hh by remember { derivedStateOf { waveL / va.value } }

    // 控制波浪周期的動畫
    val infiniteTransition = rememberInfiniteTransition()
    val wavePhase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = (2 * PI).toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = hh.toInt(), easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )

    val smokePath = remember { Path() }

    // 波浪的最大振幅
    val waveAmplitude = 80f


    // 波浪起始的 Y 坐標動畫
    val startY by infiniteTransition.animateFloat(
        initialValue = 0.75f,
        targetValue = 0.1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 8000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )

    // 波浪結束的 Y 坐標動畫
    val endY = 0.75
//    val endY by infiniteTransition.animateFloat(
//        initialValue = 0.75f,
//        targetValue = 0.9f,
//        animationSpec = infiniteRepeatable(
//            animation = tween(durationMillis = 8000, easing = LinearEasing),
//            repeatMode = RepeatMode.Restart
//        )
//    )

    Box(modifier = Modifier.fillMaxSize()) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            smokePath.reset()

            val heightRange = (size.height * (endY - startY)).toInt()
            for (i in 0..heightRange step 10) {
                val yOffset = size.height * startY + i

                // 隨著 Y 軸上的位置接近 endY，波浪振幅變小
                val adjustedWaveAmplitude =
                    waveAmplitude * (1 - (yOffset / (size.height * endY)))

                // 根據距離 endY 的位置，調整波長
//                val wavelength = 10f
                val wavelength = yOffset * 2 * PI / (size.height * endY) * 10

                val xOffset =
                    size.width / 2 + adjustedWaveAmplitude * sin(wavelength * 0.5 + wavePhase)

                if (i == 0) {
                    smokePath.moveTo(xOffset.toFloat(), yOffset)
                } else {
                    smokePath.lineTo(xOffset.toFloat(), yOffset)
                }
            }

            drawPath(
                path = smokePath,
                color = Color.Gray,
                style = Stroke(width = 4f)
            )
        }
    }
}


//@Composable
//fun CenteredVerticalWaveAnimation() {
//    // 控制波浪周期的動畫
//    val infiniteTransition = rememberInfiniteTransition()
//    val wavePhase by infiniteTransition.animateFloat(
//        initialValue = Math.toRadians(0.0).toFloat(),
//        targetValue = Math.toRadians(360.0).toFloat(),
//        animationSpec = infiniteRepeatable(
//            animation = tween(durationMillis = 1000, easing = LinearEasing),
//            repeatMode = RepeatMode.Restart
//        )
//    )
//
//    val smokePath = remember { Path() }
//
//    // 波浪的最大振幅
//    val waveAmplitude = 80f
//
//    // 波長
//    val waveL = 30f
//
//    // 波浪起始的 Y 坐標動畫
//    val startY by infiniteTransition.animateFloat(
//        initialValue = 0.75f,
//        targetValue = 0.1f,
//        animationSpec = infiniteRepeatable(
//            animation = tween(durationMillis = 8000, easing = LinearEasing),
//            repeatMode = RepeatMode.Restart
//        )
//    )
//
//    // 波浪結束的 Y 坐標動畫
//    val endY = 0.75
////    val endY by infiniteTransition.animateFloat(
////        initialValue = 0.75f,
////        targetValue = 0.9f,
////        animationSpec = infiniteRepeatable(
////            animation = tween(durationMillis = 8000, easing = LinearEasing),
////            repeatMode = RepeatMode.Restart
////        )
////    )
//
//    Box(modifier = Modifier.fillMaxSize()) {
//        Canvas(modifier = Modifier.fillMaxSize()) {
//            smokePath.reset()
//
//            val heightRange = (size.height * (endY - startY)).toInt()
//            for (i in 0..heightRange step 10) {
//                val yOffset = size.height * startY + i
//
//                // 隨著 Y 軸上的位置接近 endY，波浪振幅變小
//                val adjustedWaveAmplitude = waveAmplitude * (1 - (yOffset / (size.height * endY)))
//
//                // 根據距離 endY 的位置，調整波長
////                val wavelength = 10f
//                val wavelength = yOffset * 2 * PI / (size.height * endY) * 10
//
//                val xOffset = size.width / 2 + adjustedWaveAmplitude * sin(wavelength * 0.5 + wavePhase)
//
//                if (i == 0) {
//                    smokePath.moveTo(xOffset.toFloat(), yOffset)
//                } else {
//                    smokePath.lineTo(xOffset.toFloat(), yOffset)
//                }
//            }
//
//            drawPath(
//                path = smokePath,
//                color = Color.Gray,
//                style = Stroke(width = 4f)
//            )
//        }
//    }
//}


//@Composable
//fun CenteredVerticalWaveAnimation() {
//    // 控制波浪周期的動畫
//    val infiniteTransition = rememberInfiniteTransition()
//    val wavePhase by infiniteTransition.animateFloat(
//        initialValue = 0f,
//        targetValue = 100f,
//        animationSpec = infiniteRepeatable(
//            animation = tween(durationMillis = 8000, easing = LinearEasing),
//            repeatMode = RepeatMode.Restart
//        )
//    )
//
//    val smokePath = remember { Path() }
//
//    // 波浪的最大振幅
//    val waveAmplitude = 80f
//
//    // 波浪起始的 Y 坐標動畫
//    val startY by infiniteTransition.animateFloat(
//        initialValue = 0.75f,
//        targetValue = 0.1f,
//        animationSpec = infiniteRepeatable(
//            animation = tween(durationMillis = 8000, easing = LinearEasing),
//            repeatMode = RepeatMode.Restart
//        )
//    )
//
//    // 波浪結束的 Y 坐標動畫
//    val endY by infiniteTransition.animateFloat(
//        initialValue = 0.75f,
//        targetValue = 0.9f,
//        animationSpec = infiniteRepeatable(
//            animation = tween(durationMillis = 8000, easing = LinearEasing),
//            repeatMode = RepeatMode.Restart
//        )
//    )
//
//    Box(modifier = Modifier.fillMaxSize()) {
//        Canvas(modifier = Modifier.fillMaxSize()) {
//            smokePath.reset()
//
//            val heightRange = (size.height * (endY - startY)).toInt()
//            val waveTravelRange = size.height * (endY - startY) / 2
//            for (i in 0..heightRange step 10) {
//                val yOffset = size.height * startY + i
//
//                // 隨著 Y 軸上的位置接近 endY，波浪振幅變小
//                val adjustedWaveAmplitude = waveAmplitude * (1 - (yOffset / (size.height * endY)))
//
//                // 根據距離 endY 的位置，調整波長
//                val wavelength = yOffset * 2 * PI / (size.height * endY) * 10
//
//                // Calculate yOffset progress in percentage
//                val yOffsetProgress = yOffset / (size.height * endY)
//
//                // Calculate xOffset based on the yOffset progress
//                val xOffset = size.width / 2 + adjustedWaveAmplitude * sin(wavelength * 0.5 + wavePhase - yOffsetProgress * waveTravelRange)
//
//                if (i == 0) {
//                    smokePath.moveTo(xOffset.toFloat(), yOffset)
//                } else {
//                    smokePath.lineTo(xOffset.toFloat(), yOffset)
//                }
//            }
//
//            drawPath(
//                path = smokePath,
//                color = Color.Gray,
//                style = Stroke(width = 4f)
//            )
//        }
//    }
//}


//@Composable
//fun CenteredVerticalWaveAnimation() {
//    // 控制波浪周期的动画
//    val infiniteTransition = rememberInfiniteTransition()
//    val wavePhase by infiniteTransition.animateFloat(
//        initialValue = 0f,
//        targetValue = 100f,
//        animationSpec = infiniteRepeatable(
//            animation = tween(durationMillis = 8000, easing = LinearEasing),
//            repeatMode = RepeatMode.Restart
//        )
//    )
//
//    val smokePath = remember { Path() }
//
//    // 波浪的最大振幅
//    val waveAmplitude = 80f
//
//    // 波浪起始的 Y 坐标动画
//    val startY by infiniteTransition.animateFloat(
//        initialValue = 0.75f,
//        targetValue = 0.1f,
//        animationSpec = infiniteRepeatable(
//            animation = tween(durationMillis = 8000, easing = LinearEasing),
//            repeatMode = RepeatMode.Restart
//        )
//    )
//
//    // 波浪结束的 Y 坐标动画
//    val endY by infiniteTransition.animateFloat(
//        initialValue = 0.75f,
//        targetValue = 0.9f,
//        animationSpec = infiniteRepeatable(
//            animation = tween(durationMillis = 8000, easing = LinearEasing),
//            repeatMode = RepeatMode.Restart
//        )
//    )
//
//    Box(modifier = Modifier.fillMaxSize()) {
//        Canvas(modifier = Modifier.fillMaxSize()) {
//            smokePath.reset()
//
//            val heightRange = (size.height * (endY - startY)).toInt()
//            val waveTravelRange = size.height * (endY - startY) / 2
//            for (i in 0..heightRange step 10) {
//                val yOffset = size.height * startY + i
//
//                // 随着 Y 轴上的位置接近 endY，波浪振幅变小
//                val adjustedWaveAmplitude = waveAmplitude * (1 - (yOffset / (size.height * endY)))
//
//                // 根据距离 endY 的位置，调整波长
//                val wavelength = 100f
//
//                // Calculate yOffset progress in percentage
//                val yOffsetProgress = yOffset / (size.height * endY)
//
//                // Calculate xOffset based on the yOffset progress
//                val xOffset =
//                    size.width / 2 + adjustedWaveAmplitude * sin(wavelength * 0.5 * (yOffset / size.height) + wavePhase - yOffsetProgress * waveTravelRange)
//
//                if (i == 0) {
//                    smokePath.moveTo(xOffset.toFloat(), yOffset)
//                } else {
//                    smokePath.lineTo(xOffset.toFloat(), yOffset)
//                }
//            }
//
//            drawPath(
//                path = smokePath,
//                color = Color.Gray,
//                style = Stroke(width = 4f)
//            )
//        }
//    }
//}


@Preview(showBackground = true)
@Composable
fun RisingSmokeAnimationPreview() {
    MaterialTheme {
        CenteredVerticalWaveAnimation()
    }
}