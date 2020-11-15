package com.dariobrux.minesweeper.other

import java.text.SimpleDateFormat
import java.util.*

/**
 * Convert millisecond to a dateTime format in m:ss format.
 * @return the converted dateTime in string
 */
fun Long.toRemainingTime() : String {
    return SimpleDateFormat("m:ss", Locale.getDefault()).format(Date(this))
}