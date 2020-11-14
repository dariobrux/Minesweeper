package com.dariobrux.minesweeper

import android.util.Log
import androidx.lifecycle.ViewModel
import com.dariobrux.minesweeper.data.Tile
import com.dariobrux.minesweeper.data.Type

class GameViewModel : ViewModel() {

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
            Log.d("PRINT", matrix.sliceArray(i.. (i + n)).map { it.flags }.joinToString(separator = "\t"))
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
        val tileBottom = matrix.getOrNull(index + n)

        // Get the random tile between the adjacent.
        val tile = listOfNotNull(tileWest, tileNorth, tileEast, tileBottom).filterNot {
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
    private fun getWestIndex(index: Int) : Int {
        val result = index - 1
        return if (result % n == 0) {
            -1
        } else {
            result
        }
    }

    /**
     * Get the north-west index.
     * @return -1 if the north-west index is at the column 0.
     */
    private fun getNorthWestIndex(index: Int) : Int {
        val result = index - (n + 1)
        return if (result % n == 0) {
            -1
        } else {
            result
        }
    }

    /**
     * Get the north index.
     * @return the north index.
     */
    private fun getNorthIndex(index: Int) : Int {
        return index - n
    }

    /**
     * Get the north-east index.
     * @return -1 if the north-east index is at the column 0.
     */
    private fun getNorthEastIndex(index: Int) : Int {
        val result = index - (n - 1)
        return if (result % n == 0) {
            -1
        } else {
            result
        }
    }

    /**
     * Get the east index.
     * @return -1 if the east index is at the column c % n = n.
     */
    private fun getEastIndex(index: Int) : Int {
        val result = index + 1
        return if (result % n == 0) {
            -1
        } else {
            result
        }
    }

}