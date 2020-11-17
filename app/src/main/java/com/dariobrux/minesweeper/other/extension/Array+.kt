package com.dariobrux.minesweeper.other.extension

/**
 * Shuffle items in an array.
 * @return the same array with shuffled items.
 */
fun <T> Array<T>.shuffled() : Array<T> {
    return this.apply {
        shuffle()
    }
}