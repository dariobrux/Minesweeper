package com.dariobrux.minesweeper.other.extension

import java.text.SimpleDateFormat
import java.util.*

/**
 *
 * Created by Dario Bruzzese on 17/11/2020.
 *
 * This class contains all the extended method for the Numbers
 *
 */

/**
 * Convert millisecond to a dateTime format in m:ss format.
 * @param format the format of the final dateTime like "m:ss"
 * @return the converted dateTime in string
 */
fun Long.toFormattedTime(format: String) : String {
    return SimpleDateFormat(format, Locale.getDefault()).format(Date(this))
}

fun Int.sqrt(): Int {
    return kotlin.math.sqrt(this.toDouble()).toInt()
}