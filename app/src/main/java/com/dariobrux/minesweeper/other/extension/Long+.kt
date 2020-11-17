package com.dariobrux.minesweeper.other.extension

import java.text.SimpleDateFormat
import java.util.*

/**
 * Convert millisecond to a dateTime format in m:ss format.
 * @param format the format of the final dateTime like "m:ss"
 * @return the converted dateTime in string
 */
fun Long.toFormattedTime(format: String) : String {
    return SimpleDateFormat(format, Locale.getDefault()).format(Date(this))
}