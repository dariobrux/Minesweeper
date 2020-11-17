package com.dariobrux.minesweeper.other.extension

fun Int.sqrt(): Int {
    return kotlin.math.sqrt(this.toDouble()).toInt()
}