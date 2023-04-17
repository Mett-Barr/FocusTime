package com.example.focustime

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import com.example.focustime.ui.compoment.SystemBarMode
import com.example.focustime.ui.page.*
import com.example.focustime.ui.test.Incense
import com.example.focustime.ui.test.SystemBarTest
import com.example.focustime.ui.theme.FocusTimeTheme
import java.util.*


class MainActivity : ComponentActivity() {

    var isFromWireless = false
    lateinit var receiver: BatteryState

    var lastTouch = 0L

    @OptIn(ExperimentalComposeUiApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

//        startActivity(Intent(ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS))
//        (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager).apply {
//            setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_PRIORITY)
//        }

//        val params = window.attributes
//        params.screenBrightness = 0.2f
//        window.attributes = params
//        window.addFlags(WindowManager.LayoutParams.FLAGS_CHANGED)

//        val screenBrightness = window.attributes.screenBrightness

        window.decorView.setOnSystemUiVisibilityChangeListener { visibility ->
            if (visibility and View.SYSTEM_UI_FLAG_FULLSCREEN == 0) {
                Log.d("!!!", "1")

            } else Log.d("!!!", "2")
        }


//        val onWindowInsetApplied: (view: View, window:WindowInsets) -> WindowInsets = { _, windowInsets ->
//            val areSystemBarsVisible = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//                windowInsets.isVisible(WindowInsets.Type.systemBars())
//            } else {
//                false
////                TODO("VERSION.SDK_INT < R")
//            }
//            //Do something
//
//            if (areSystemBarsVisible) {
//                window.attributes.screenBrightness = screenBrightness
//            } else {
//                window.attributes.screenBrightness = screenBrightness * 0.2f
//
//            }
//            Log.d("!!!", "areSystemBarsVisible = $areSystemBarsVisible")
//
//            windowInsets
//        }
//
//        View.OnApplyWindowInsetsListener(onWindowInsetApplied)

//        window.attributes.screenBrightness = 0.02f


//        window.attributes.screenBrightness = WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_OFF

//        val lp = WindowManager.LayoutParams()
//        lp.screenBrightness = 0.005f
//        window.attributes = lp

        val filter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)

        val batteryIntent = registerReceiver(null, filter)

        if (batteryIntent != null) {
            if (
                batteryIntent.getIntExtra(
                    BatteryManager.EXTRA_PLUGGED,
                    -1
                ) == BatteryManager.BATTERY_PLUGGED_WIRELESS
            ) {
                isFromWireless = true
            } else {
//                Log.d(
//                    "!!!", batteryIntent.getIntExtra(
//                        BatteryManager.EXTRA_PLUGGED,
//                        -1
//                    ).toString()
//                )
            }
        } else {
        }
//            Log.d("!!!", "null")

//        Log.d("!!!", isFromWireless.toString())

//        CoroutineScope(Dispatchers.Default).launch {
//            delay(2000)
//        }
        receiver = BatteryState({ isFromWireless = true }) {
            if (isFromWireless) {
//                Log.d("!!!", "turn off")
                finish()
            }
        }
        registerReceiver(receiver, filter)





        setContent {
//            Test()
//            Test2()
//            Test3()
//            Clock()

            FocusTimeTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Incense()
//                    SystemBarTest()
//                    SystemBarMode()
                    SystemBarMode(isFullScreenMode = true)
                }
            }
//            CenteredVerticalWaveAnimation()
        }

//        setContent {
//            FocusTimeTheme {
//                // A surface container using the 'background' color from the theme
//
//                rememberSystemUiController().isSystemBarsVisible = false
//                LocalView.current.keepScreenOn = true
//
//
//                var clicked by remember {
//                    mutableStateOf(false)
//                }
//
//                val alpha by animateFloatAsState(
//                    targetValue = if (clicked) 1f else 0.25f,
//                    animationSpec = tween(2000)
//                )
//
//
//
//
//                LaunchedEffect(Unit) {
//                    while (true) {
//                        val time = getTime()
//                        delay(
//                            if (time < lastTouch) {
//                                // touched
//                                lastTouch - time
//                            } else {
//                                // no touch
//                                clicked = false
//                                3000
//                            }
//                        )
//                    }
//                }
//
//
//                Surface(
//                    modifier = Modifier
//                        .pointerInteropFilter {
//                            lastTouch = getTime() + 3000L
//                            clicked = true
//                            false
//                        }
//
//                        .alpha(alpha)
//
//                        .background(Color.Black)
//                        .fillMaxSize()
//                        .padding(48.dp)
//                        .clickable(
//                            interactionSource = remember { MutableInteractionSource() },
//                            indication = null
//                        ) { finish() },
//                    color = Color.Black,
//                    contentColor = Color.White
//                ) {
//                    Clock()
//                }
//            }
//        }
    }

    //    override fun onResume() {
//        super.onResume()
//
//        WindowCompat.setDecorFitsSystemWindows(window, false)
//        WindowCompat.getInsetsController(window, window.decorView)?.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_BARS_BY_TOUCH
////        WindowCompat.getInsetsController(window, window.decorView)?.hide(WindowInsetsCompat.Type.navigationBars())
////        WindowCompat.getInsetsController(window, window.decorView)?.hide(WindowInsetsCompat.Type.statusBars())
//
//    }
    override fun onDestroy() {
        super.onDestroy()

        unregisterReceiver(receiver)
    }

//    override fun onTouchEvent(event: MotionEvent?): Boolean {
//
//        lastTouch = getTime() + 3000L
//
//        return super.onTouchEvent(event)
//    }

    class BatteryState(private val plugging: () -> Unit, private val finish: () -> Unit) :
        BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {

            val state = intent?.getIntExtra(BatteryManager.EXTRA_STATUS, -1)
            val plug = intent?.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1)

            val isCharging = state == BatteryManager.BATTERY_STATUS_CHARGING
//            Log.d("!!!", "state = $state")
//            Log.d("!!!", "plug = $plug")
//            val isCharging = state == BatteryManager.BATTERY_STATUS_CHARGING || state == BatteryManager.BATTERY_STATUS_FULL

//            if (state == BatteryManager.BATTERY_STATUS_DISCHARGING) finish()
            if (plug != BatteryManager.BATTERY_PLUGGED_WIRELESS) finish()
            else plugging()
//            if (!isCharging) finish()

        }
    }

    val getTime = { Calendar.getInstance().timeInMillis }
}
