package com.example.focustime.clocktool

fun Int.timeText(): String =
    if (this < 10) "0$this"
    else this.toString()

fun Int.setTimeText( t1: (String) -> Unit, t2: (String) -> Unit  ) {
        t1((this/10).toString())
        t2((this % 10).toString())
}