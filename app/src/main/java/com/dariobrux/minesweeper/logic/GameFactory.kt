package com.dariobrux.minesweeper.logic

import android.util.Log
import com.dariobrux.minesweeper.data.Tile
import com.dariobrux.minesweeper.data.Type
import com.dariobrux.minesweeper.other.shuffled

class GameFactory {

    private lateinit var matrix: Array<Tile>
    private val n = 8
    private val totalBombs = 15

    fun getField() {

        // Get all the position of the bombs. This means that I'll put
        // the bombs at these positions.

        matrix = Array(n * n) { Tile() }

        // Create a list with all indexes
        val bombPositions = getBombPositions()

        repeat(n * n) { index ->

            // Iterate all bomb positions and put the bomb Tile
            // at that position.
            bombPositions.getOrNull(index)?.let { bombPosition ->
                matrix[bombPosition] = Tile(Type.BOMB, -1)
                incrementAdjacent(bombPosition)
            }
        }

        for (i in 0 until (n * n) step n) {
            Log.d(
                "PRINT",
                matrix.sliceArray(i until i + n).map { it.flags }
                    .joinToString(separator = "\t\t", prefix = "\t\t").replace("-1", "B")
            )
        }
    }

    /**
     * Get the list with all the bomb positions that will be put in the matrix.
     * It also check the validity of the index.
     * @return the list with all bombs positions.
     */
    private fun getBombPositions(): MutableList<Int> {

        // All the possible positions shuffled.
        val positions = Array(n * n) { it }.shuffled().toMutableList()

        // The future bombs positions without no check.
        val bombPositions = positions.take(totalBombs).toMutableList()

        // Save all the unused bombs positions.
        val unusedBombPositions = positions.subList(totalBombs, positions.size).toMutableList()

        // Check valid bombs positions.
        bombPositions.forEachIndexed { index, bombPosition ->
            checkValidBombIndex(bombPosition, index, bombPositions, unusedBombPositions)
        }

        return bombPositions

    }

    /**
     * Check if the bomb position is valid. It means that around it, it must not be:
     * - for corner: 3 bombs around
     * - for side: 5 bombs around
     * - for inside: 8 bombs around
     */
    private fun checkValidBombIndex(bombPosition: Int, index: Int, bombPositions: MutableList<Int>, unusedBombPositions: MutableList<Int>) {

        // Get all indexes around the bomb
        val west = getWestIndex(bombPosition)
        val northWest = getNorthWestIndex(bombPosition)
        val north = getNorthIndex(bombPosition)
        val northEast = getNorthEastIndex(bombPosition)
        val east = getEastIndex(bombPosition)
        val southEast = getSouthEastIndex(bombPosition)
        val south = getSouthIndex(bombPosition)
        val southWest = getSouthWestIndex(bombPosition)

        // Filter all that are != -1, to stay inside the matrix and remove the outer bounds.
        val aroundBombIndexes = listOf(west, northWest, north, northEast, east, southEast, south, southWest).filter {
            it != -1
        }

        // If the list contains all the bomb's around indexes, it means that this bomb has an invalid index and I
        // must change it until it will be valid.
        if (bombPositions.containsAll(aroundBombIndexes)) {
            val newValue = unusedBombPositions.removeFirst()
            bombPositions[index] = newValue
            checkValidBombIndex(newValue, index, bombPositions, unusedBombPositions)
        }
    }

    /**
     * Check if the [position] is at the corner of the matrix
     * @param position an index in matrix
     * @return true if the position is a corner, false if is not a corner.
     */
    private fun isCorner(position: Int): Boolean {
        return position == 0 || position == n - 1 || position == ((n * n) - n) || position == (n * n) - 1
    }

    /**
     * Check if the [position] is at the side of the matrix
     * @param position an index in matrix
     * @return true if the position is at side, false if is not at side.
     */
    private fun isSide(position: Int): Boolean {
        return position < n || position % n == 0 || position % n == n - 1 || position >= (n * n) - n
    }

    /**
     * Check if the [position] is inside the matrix
     * @param position an index in matrix
     * @return true if the position is inside, false if is not inside.
     */
    private fun isInside(position: Int): Boolean {
        return !isCorner(position) && !isSide(position)
    }

    /**
     * Order: west, north-west, north, north-east, east, south-east, south, south-west
     */
    private fun incrementAdjacent(position: Int) {

        // Get the adjacent tiles
        val tileWest = matrix.getOrNull(getWestIndex(position))
        val tileNorthWest = matrix.getOrNull(getNorthWestIndex(position))
        val tileNorth = matrix.getOrNull(getNorthIndex(position))
        val tileNorthEast = matrix.getOrNull(getNorthEastIndex(position))
        val tileEast = matrix.getOrNull(getEastIndex(position))
        val tileSouthEast = matrix.getOrNull(getSouthEastIndex(position))
        val tileSouth = matrix.getOrNull(getSouthIndex(position))
        val tileSouthWest = matrix.getOrNull(getSouthWestIndex(position))

        // Get the random tile between the adjacent.
        val adjacentTilesNotBomb = listOfNotNull(
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
        }.forEach {
            it.type = Type.FLAG
            it.flags++
        }
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
        return if (index < n) {
            -1
        } else {
            index - n
        }
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