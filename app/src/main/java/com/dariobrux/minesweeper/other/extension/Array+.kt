package com.dariobrux.minesweeper.other.extension

/**
 *
 * Created by Dario Bruzzese on 15/11/2020.
 *
 * This class contains all the extended method for the Array
 *
 */

/**
 * Shuffle items in an array.
 * @return the same array with shuffled items.
 */
fun <T> Array<T>.shuffled(): Array<T> {
    return this.apply {
        shuffle()
    }
}