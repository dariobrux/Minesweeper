package com.dariobrux.minesweeper.data

data class Tile(var type: Type = Type.EMPTY, var flags: Int = 0, var state: State = State.COVERED) {

    fun discover() {
        state = State.DISCOVERED
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