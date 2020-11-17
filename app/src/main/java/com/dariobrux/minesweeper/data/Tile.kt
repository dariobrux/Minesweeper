package com.dariobrux.minesweeper.data

/**
 *
 * Created by Dario Bruzzese on 17/11/2020.
 *
 * This is the tile model. Represent every item of the board.
 * [type] could be [Type.EMPTY], [Type.FLAG] and [Type.BOMB],
 * [flags] is the number of bombs around it.
 * [state] could be [State.COVERED] and [State.DISCOVERED],
 * [isTouched] is true when user touches the tile, false if the tile has never been touched.
 *
 */
data class Tile(var type: Type = Type.EMPTY, var flags: Int = 0, var state: State = State.COVERED, var isTouched: Boolean = false) {

    fun discover() {
        state = State.DISCOVERED
    }

    fun touched() {
        isTouched = true
    }
}

enum class Type {
    EMPTY,
    FLAG,
    BOMB
}

enum class State {
    COVERED,
    DISCOVERED;
}