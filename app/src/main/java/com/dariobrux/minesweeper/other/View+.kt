package com.dariobrux.minesweeper.other

import android.view.View

fun View.toVisible() {
    this.visibility = View.VISIBLE
}

fun View.toGone() {
    this.visibility = View.GONE
}