package com.dariobrux.minesweeper.logic

import android.util.Log
import com.dariobrux.minesweeper.data.Tile
import com.dariobrux.minesweeper.data.Type

class GameFactory {

    private lateinit var matrix: Array<Tile>
    private val n = 8

    fun getField() {

        val totalBombs = 15

        // Get all the position of the bombs. This means that I'll put
        // the bombs at these positions.
        val bombPositions = Array(n * n) {
            it
        }.apply {
            this.shuffle()
        }.take(totalBombs)

        var rowIndex = 0
        var colIndex = 0

        matrix = Array(n * n) { Tile() }

        repeat(n * n) { index ->

            // Get the position of the bomb and put the bomb Tile
            // at that position.
            bombPositions.getOrNull(index)?.let { bombPosition ->
                matrix[bombPosition] = Tile(Type.BOMB, -1)
            }

            when (matrix[index].type) {
                Type.BOMB -> {
                    incrementAdjacent(index)
                }
            }
        }

        for (i in 0 until (n * n) step n) {
            Log.d(
                "PRINT",
                matrix.sliceArray(i until i + n).map { it.flags }
                    .joinToString(separator = "\t\t", prefix = "\t\t")
            )
        }
    }

    /**
     * Order: west, north-west, north, north-east, east, south-east, south, south-west
     */
    private fun incrementAdjacent(index: Int) {

        // Get the adjacent tiles
        val tileWest = matrix.getOrNull(getWestIndex(index))
        val tileNorthWest = matrix.getOrNull(getNorthWestIndex(index))
        val tileNorth = matrix.getOrNull(getNorthIndex(index))
        val tileNorthEast = matrix.getOrNull(getNorthEastIndex(index))
        val tileEast = matrix.getOrNull(getEastIndex(index))
        val tileSouthEast = matrix.getOrNull(getSouthEastIndex(index))
        val tileSouth = matrix.getOrNull(getSouthIndex(index))
        val tileSouthWest = matrix.getOrNull(getSouthWestIndex(index))

        // Get the random tile between the adjacent.
        val tile = listOfNotNull(
            tileWest,
            tileNorthWest,
            tileNorth,
            tileNorthEast,
            tileEast,
            tileSouthEast,
            tileSouth,
            tileSouthWest
        ).filterNot {
            it.type == Type.BOMB
        }.shuffled().first()

        // Increment the flag.
        tile.type = Type.FLAG
        tile.flags++
    }

    /**
     * Get the west index.
     * @return -1 if the west index is at the column 0.
     */
    fun getWestIndex(index: Int): Int {
        return if (index % n == 0) {
            -1
        } else {
            index - 1
        }
    }

    /**
     * Get the north-west index.
     * @return -1 if the north-west index is at the column 0.
     */
    fun getNorthWestIndex(index: Int): Int {
        return if (index < n || index % n == 0) {
            -1
        } else {
            index - (n + 1)
        }
    }

    /**
     * Get the north index.
     * @return the north index.
     */
    fun getNorthIndex(index: Int): Int {
        return index - n
    }

    /**
     * Get the north-east index.
     * @return -1 if the north-east index is at the column 0.
     */
    fun getNorthEastIndex(index: Int): Int {
        val result = index - (n - 1)
        return if (index < n || result % n == 0) {
            -1
        } else {
            result
        }
    }

    /**
     * Get the east index.
     * @return -1 if the east index is at the column c % n = n.
     */
    fun getEastIndex(index: Int): Int {
        val result = index + 1
        return if (result % n == 0) {
            -1
        } else {
            result
        }
    }

    /**
     * Get the south-east index.
     * @return -1 if the south-east index is at the first column.
     */
    fun getSouthEastIndex(index: Int): Int {
        val result = index + (n + 1)
        return if (index >= (n * n) - n || result % n == 0) {
            -1
        } else {
            result
        }
    }

    /**
     * Get the south index.
     * @return the south index.
     */
    fun getSouthIndex(index: Int): Int {
        return if (index >= (n * n) - n) {
            -1
        } else {
            index + n
        }
    }

    /**
     * Get the south-west index.
     * @return -1 if the south-west index is at the column 0.
     */
    fun getSouthWestIndex(index: Int): Int {
        return if (index >= (n * n) - n || index % n == 0) {
            -1
        } else {
            index + (n - 1)
        }
    }

}