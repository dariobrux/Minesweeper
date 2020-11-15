package com.dariobrux.minesweeper.other

fun Int.sqrt(): Int {
    return kotlin.math.sqrt(this.toDouble()).toInt()
}