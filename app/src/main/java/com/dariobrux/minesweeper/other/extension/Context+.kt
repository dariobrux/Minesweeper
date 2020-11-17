package com.dariobrux.minesweeper.other.extension

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import java.util.concurrent.TimeUnit

/**
 *
 * Created by Dario Bruzzese on 17/11/2020.
 *
 * This class contains all the extended method for the Context
 *
 */

/**
 * Perform a vibration
 * @param duration the vibration duration
 */
fun Context.vibrate(duration: Long) {
    val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val vibrationEffect = VibrationEffect.createOneShot(duration, VibrationEffect.DEFAULT_AMPLITUDE)
        vibrator.vibrate(vibrationEffect)
    } else {
        @Suppress("DEPRECATION")
        vibrator.vibrate(TimeUnit.MILLISECONDS.toMillis(duration))
    }
}