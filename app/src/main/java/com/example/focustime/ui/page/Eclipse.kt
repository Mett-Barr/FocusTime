package com.example.focustime.ui.page

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Preview
@Composable
fun Test3() {
    Canvas(modifier = Modifier.size(200.dp)) {
        val rect = Rect(size.width / 2, size.height / -2, size.width / 2 * 3, size.height / 2)
        val pathC = Path().apply {
            arcTo(rect, 0F, 180F, false)
            arcTo(rect, 180F, 180F, false)
        }
//        drawPath(pathC, Color.Blue, style = Fill)
        val path = Path().apply {
            moveTo(0f, 0f)
            lineTo(size.width * 2, size.height * 2)
            lineTo(size.width, 0f)
//            lineTo(0f, 0f)
//            close()
        }
        clipPath(pathC) {
            drawCircle(Color.White)
        }
//        clipRect(
//            0f, 0f, 0.5f, 0.5f
//        ) {
//
//        }
    }
}