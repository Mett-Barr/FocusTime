package com.example.focustime.ui.compoment

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat

@Composable
fun SystemBarMode(isFullScreenMode: Boolean) {
    val context = LocalContext.current
    val view = LocalView.current
    val window = (context as? Activity)?.window
    val controller = window?.let { WindowCompat.getInsetsController(it, view) }

    LaunchedEffect(isFullScreenMode) {
        if (isFullScreenMode) {
            controller?.hide(WindowInsetsCompat.Type.systemBars())
        } else {
            controller?.show(WindowInsetsCompat.Type.systemBars())
            controller?.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }
}