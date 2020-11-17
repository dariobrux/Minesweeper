package com.dariobrux.minesweeper.other.extension

import android.view.View

/**
 *
 * Created by Dario Bruzzese on 15/11/2020.
 *
 * This class contains all the extended method for the View
 *
 */

/**
 * Set the visibility to VISIBLE.
 */
fun View.toVisible() {
    this.visibility = View.VISIBLE
}

/**
 * Set the visibility to GONE.
 */
fun View.toGone() {
    this.visibility = View.GONE
}