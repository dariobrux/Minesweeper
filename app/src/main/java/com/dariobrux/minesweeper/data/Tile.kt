package com.dariobrux.minesweeper.data

data class Tile(var type: Type = Type.EMPTY, var flags: Int = 0)

enum class Type {
    EMPTY,
    FLAG,
    BOMB
}