package com.example.focustime.ui.page

import android.app.Activity
import android.content.res.Configuration
import android.util.Log
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.focustime.R
import com.example.focustime.clocktool.setTimeText
import com.example.focustime.clocktool.timeText
import kotlinx.coroutines.delay
import java.util.*

@OptIn(ExperimentalAnimationApi::class)
@Preview
@Composable
fun Clock() {

    val window = (LocalContext.current as Activity).window

    var hour1 by remember {
        mutableStateOf("")
    }
    var hour2 by remember {
        mutableStateOf("")
    }
    var minute1 by remember {
        mutableStateOf("")
    }
    var minute2 by remember {
        mutableStateOf("")
    }
    var second1 by remember {
        mutableStateOf("")
    }
    var second2 by remember {
        mutableStateOf("")
    }

    fun tick() {
        Calendar.getInstance().apply {
            get(Calendar.HOUR_OF_DAY).setTimeText(
                { hour1 = it },
                { hour2 = it }
            )
//            hour1 = get(Calendar.HOUR_OF_DAY).timeText()
            get(Calendar.MINUTE).setTimeText(
                { minute1 = it },
                { minute2 = it }
            )
            get(Calendar.SECOND).setTimeText(
                { second1 = it },
                { second2 = it }
            )
        }
    }


    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
//        Calendar.getInstance().get(Calendar.MILLISECOND).also {
//            delay((1000 - it).toLong())
//        }

        while (true) {
//            Log.d("!!!", "tick")
            tick()
//            delay(1000)
            delay(3000)

            Log.d("!!!", window.attributes.screenBrightness.toString())

            window.attributes.screenBrightness = Random().nextFloat()
        }
    }

    tick()

//    CompositionLocalProvider(values = ) {
//
//    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.weight(1.5f))
        Row(modifier = Modifier.wrapContentSize()) {
            CustomAnimation(targetState = hour1) {
                StartText(text = it)
            }
            CustomAnimation(targetState = hour2) {
                EndText(text = it)
            }
        }
//        AnimatedContent(hour1 + hour2) {
//            ClockText(text1 = hour1, text2 = hour2)
//        }
        Spacer(modifier = Modifier.weight(0.3f))
        Row(modifier = Modifier.wrapContentSize()) {
            CustomAnimation(targetState = minute1) {
                StartText(text = it)
            }
            CustomAnimation(targetState = minute2) {
                EndText(text = it)
            }
        }


        // second
//        Row(modifier = Modifier.wrapContentSize()) {
//            CustomAnimation(targetState = second1) {
//                StartText(text = it)
//            }
//            CustomAnimation(targetState = second2) {
//                EndText(text = it)
//            }
//        }

        Spacer(modifier = Modifier.weight(1.5f))
    }
}

@Composable
fun ColumnScope.ClockText(text: String) {
    Box(
        contentAlignment = Alignment.Center, modifier = Modifier
            .fillMaxWidth()
//        .padding(32.dp)
    ) {
        Text(
            text = text,
            fontSize = 200.sp,
            textAlign = TextAlign.Center,
            fontFamily = ClockFont.obj
        )
    }
}

@Composable
fun ColumnScope.ClockText(text1: String, text2: String) {
    Row(modifier = Modifier.wrapContentSize()) {
        Box(
            contentAlignment = Alignment.CenterEnd,
            modifier = Modifier.weight(1f)
        ) {
            Box(
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = "9",
                    fontSize = ClockFont.size,
                    textAlign = TextAlign.End,
                    fontFamily = ClockFont.obj,
                    color = Color.Black.copy(alpha = 0f)
                )
                Text(
                    text = text1,
                    fontSize = ClockFont.size,
                    textAlign = TextAlign.End,
                    fontFamily = ClockFont.obj
                )
            }
        }
        Box(
            contentAlignment = Alignment.CenterStart,
            modifier = Modifier.weight(1f)
        ) {
            Box(
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = "9",
                    fontSize = ClockFont.size,
                    textAlign = TextAlign.End,
                    fontFamily = ClockFont.obj,
                    color = Color.Black.copy(alpha = 0f)
                )
                Text(
                    text = text2,
                    fontSize = ClockFont.size,
                    textAlign = TextAlign.End,
                    fontFamily = ClockFont.obj
                )
            }
        }
    }

}

@Composable
fun RowScope.StartText(text: String) {
    Box(
        contentAlignment = Alignment.CenterEnd,
        modifier = Modifier.weight(1f)
    ) {
        Box(
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = "9",
                fontSize = ClockFont.size,
                textAlign = TextAlign.End,
                fontFamily = ClockFont.obj,
                color = Color.Black.copy(alpha = 0f)
            )
            Text(
                text = text,
                fontSize = ClockFont.size,
                textAlign = TextAlign.End,
                fontFamily = ClockFont.obj
            )
        }
    }
}

@Composable
fun RowScope.EndText(text: String) {
    Box(
        contentAlignment = Alignment.CenterStart,
        modifier = Modifier.weight(1f)
    ) {
        Box(
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = "9",
                fontSize = ClockFont.size,
                textAlign = TextAlign.End,
                fontFamily = ClockFont.obj,
                color = Color.Black.copy(alpha = 0f)
            )
            Text(
                text = text,
                fontSize = ClockFont.size,
                textAlign = TextAlign.End,
                fontFamily = ClockFont.obj
            )
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun CustomAnimation(targetState: String, content: @Composable (String) -> Unit) {
    AnimatedContent(targetState, transitionSpec = {
        fadeIn(animationSpec = tween(4000)) with fadeOut(animationSpec = tween(1000))
    }) {
        content(it)
    }
}

object ClockFont {
    val obj = FontFamily(
//        Font(R.font.organo),
        Font(R.font.typo_round_regular_demo)
    )

    //    val size = 100.sp
    val size = 230.sp
}


@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = false)
@Composable
fun Test() {
    Row(Modifier.padding(32.dp)) {
        Box(
            contentAlignment = Alignment.CenterEnd,
            modifier = Modifier.weight(1f)
        ) {
            Box(
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = "9",
                    fontSize = 96.sp,
                    textAlign = TextAlign.End,
                    fontFamily = ClockFont.obj,
                    color = Color.Black.copy(alpha = 0.5f)
                )
                Text(
                    text = "1",
                    fontSize = 96.sp,
                    textAlign = TextAlign.End,
                    fontFamily = ClockFont.obj
                )
            }
        }
        Box(
            contentAlignment = Alignment.CenterStart,
            modifier = Modifier.weight(1f)
        ) {
            Box(
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = "9",
                    fontSize = 96.sp,
                    textAlign = TextAlign.End,
                    fontFamily = ClockFont.obj,
                    color = Color.Black.copy(alpha = 0f)
                )
                Text(
                    text = "1",
                    fontSize = 96.sp,
                    textAlign = TextAlign.End,
                    fontFamily = ClockFont.obj
                )
            }
        }
    }
}