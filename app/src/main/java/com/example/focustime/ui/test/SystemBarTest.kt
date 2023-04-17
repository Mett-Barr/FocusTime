package com.example.focustime.ui.test

import android.app.Activity
import android.os.Build
import android.view.View
import android.view.Window
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat


@Composable
fun EnterFullScreen() {
    val context = LocalContext.current
    val window = (context as? Activity)?.window
    val controller = window?.let { WindowCompat.getInsetsController(it, it.decorView) }
    controller?.let {
        // 隐藏状态栏和导航栏
        it.hide(WindowInsetsCompat.Type.systemBars())
    }
}

@Composable
fun ExitFullScreen() {
    val context = LocalContext.current
    val window = (context as? Activity)?.window
    val controller = window?.let { WindowCompat.getInsetsController(it, it.decorView) }
    controller?.let {
        // 显示状态栏和导航栏
        it.show(WindowInsetsCompat.Type.systemBars())
    }
}

@Composable
fun SystemBarTest() {
    var boo by remember { mutableStateOf(true) }
    Button(onClick = { boo = !boo }, modifier = Modifier.wrapContentSize()) {
        Text(text = "test")
    }

    com.example.focustime.ui.compoment.SystemBarMode(isFullScreenMode = boo)
}



//@Composable
//fun SystemBarMode() {
//    // 记住当前System Bar模式
//    val systemBarMode = remember { mutableStateOf(MySystemBarMode.FULLSCREEN) }
//
//
//    // 获取窗口和WindowInsetsController
//    val context = LocalContext.current
//    val view = LocalView.current
//    val window = (context as? Activity)?.window
//    val controller = window?.let { WindowCompat.getInsetsController(it, view) }
//
//    // 切换System Bar模式
//    LaunchedEffect(systemBarMode.value) {
//        when (systemBarMode.value) {
//            MySystemBarMode.FULLSCREEN -> {
//                controller?.let {
//                    it.hide(WindowInsetsCompat.Type.systemBars())
//                }
//            }
//            MySystemBarMode.IMMERSIVE -> {
//                controller?.let {
//                    it.show(WindowInsetsCompat.Type.systemBars())
//                    it.systemBarsBehavior =
//                        WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
//                }
//            }
//        }
//    }
//
//    // 返回一个可点击的元素
//    Box(
//        modifier = Modifier.fillMaxSize(),
//        contentAlignment = Alignment.Center
//    ) {
//        Text(
//            text = "Click me to toggle system bar mode",
//            style = TextStyle(fontSize = 24.sp),
//            modifier = Modifier.clickable {
//                systemBarMode.value =
//                    if (systemBarMode.value == MySystemBarMode.FULLSCREEN) {
//                        MySystemBarMode.IMMERSIVE
//                    } else {
//                        MySystemBarMode.FULLSCREEN
//                    }
//            }
//        )
//    }
//}

// 定义System Bar模式
enum class MySystemBarMode {
    FULLSCREEN,
    IMMERSIVE
}
