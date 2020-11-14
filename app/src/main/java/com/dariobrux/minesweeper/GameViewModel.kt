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
     * Order: west, north-west, north, north-est, est, south-est, south, south-west
     */
    private fun incrementAdjacent(index: Int) {

        // Get the adjacent tiles
        val tileLeft = matrix.getOrNull(index - 1)
        val tileTop = matrix.getOrNull(index - n)
        val tileRight = matrix.getOrNull(index + 1)
        val tileBottom = matrix.getOrNull(index + n)

        // Get the random tile between the adjacent.
        val tile = listOfNotNull(tileLeft, tileTop, tileRight, tileBottom).filterNot {
            it.type == Type.BOMB
        }.shuffled().first()

        // Increment the flag.
        tile.type = Type.FLAG
        tile.flags++
    }

}