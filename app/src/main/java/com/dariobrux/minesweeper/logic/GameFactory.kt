package com.dariobrux.minesweeper.logic

import com.dariobrux.minesweeper.data.State
import com.dariobrux.minesweeper.data.Tile
import com.dariobrux.minesweeper.data.Type
import com.dariobrux.minesweeper.other.extension.shuffled
import timber.log.Timber

/**
 *
 * Created by Dario Bruzzese on 14/11/2020.
 *
 * This class is the core. Creates the logic of the game.
 * Creates the board [n] x [n] with empty tiles.
 * Create a list with the bomb positions, shuffled, checking if the
 * adjacent positions are valid. Generally, a bomb position is valid when around
 * it, there aren't only bombs.
 * - When a tile is selected and it's a [Type.FLAG] tile, it changes its state to [State.DISCOVERED] and [Tile.isTouched] true.
 * - When a tile is selected and it's a [Type.EMPTY] tile, it changes its state to [State.DISCOVERED] and [Tile.isTouched] true, but also the
 * tiles around it will be discovered, until a flag is reached. It will be discovered too.
 * - When a tile is selected and it's a [Type.BOMB] flag, it changes its state to [State.DISCOVERED]  and [Tile.isTouched] true, but also the
 * other bombs will be discovered, without change their [Tile.isTouched] value.
 * When the user discovers [n] * [n] - [totalBombs] tiles, he wins.
 * When the user discovers a bomb, he loses.
 *
 */
class GameFactory {

    /**
     * The board
     */
    private lateinit var matrix: Array<Tile>

    /**
     * How many valid tiles remains. The valid tiles are the empty and flag tiles.
     */
    private var remainingNotBombTiles = 0

    /**
     * The length of the board side.
     */
    private val n = 8

    /**
     * How many bombs in the grid
     */
    private val totalBombs = 15

    /**
     * This list contains all bomb indexes in matrix.
     */
    private var bombPositions = emptyList<Int>()

    /**
     * Create the board.
     * @return the list with all tiles.
     */
    fun getBoard(): List<Tile> {

        remainingNotBombTiles = (n * n) - totalBombs

        // Create the matrix with all empty tiles.
        matrix = Array(n * n) { Tile() }

        // Create a list with all indexes
        bombPositions = getBombValidPositions()

        repeat(n * n) { index ->

            // Iterate all bomb positions and put the bomb Tile
            // at that position.
            bombPositions.getOrNull(index)?.let { bombPosition ->
                matrix[bombPosition] = Tile(Type.BOMB, -1)
                val adjacentTiles = getAdjacentNoBombTiles(bombPosition)
                incrementAllAdjacent(adjacentTiles)
            }
        }

        // Print the matrix in LogCat
        for (i in 0 until (n * n) step n) {
            Timber.tag(TAG).d(matrix.sliceArray(i until i + n).map { it.flags }.joinToString(separator = "\t\t", prefix = "\t\t").replace("-1", "B"))
        }

        return matrix.toList()
    }

    /**
     * Get the list with all the bomb positions that will be put in the matrix.
     * It also check the validity of the index.
     * @return the list with all bombs positions.
     */
    private fun getBombValidPositions(): MutableList<Int> {

        // All the possible positions shuffled.
        val positions = getNxNShuffledList()

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
     * @return a list of 0..(n x n) shuffled items
     */
    fun getNxNShuffledList() = Array(n * n) { it }.shuffled().toMutableList()

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
     * Get all the adjacent tiles around [position]. These tiles aren't bombs.
     * Order: west, north-west, north, north-east, east, south-east, south, south-west
     */
    private fun getAdjacentNoBombTiles(position: Int): List<Tile> = getAdjacentPositions(position).mapNotNull {
        matrix.getOrNull(it)
    }.filterNot {
        it.type == Type.BOMB
    }

    /**
     * Get the list with all adjacent positions.
     * This positions are the matrix indexes.
     * @param position start from this position.
     * @return the list with the adjacent positions.
     */
    fun getAdjacentPositions(position: Int): List<Int> {
        // Get the adjacent positions excluding the outside indexes (-1).
        return listOf(
            getWestIndex(position),
            getNorthWestIndex(position),
            getNorthIndex(position),
            getNorthEastIndex(position),
            getEastIndex(position),
            getSouthEastIndex(position),
            getSouthIndex(position),
            getSouthWestIndex(position)
        ).filterNot {
            it == -1
        }
    }

    /**
     * Increment the flags of the adjacent tiles.
     * @param tiles the list of all the adjacent tiles.
     */
    private fun incrementAllAdjacent(tiles: List<Tile>) {
        tiles.forEach {
            incrementFlag(it)
        }
    }

    /**
     * Increment the value of flags of a tile
     * @param tile the [Tile] on which increment flags.
     */
    private fun incrementFlag(tile: Tile) {
        tile.type = Type.FLAG
        tile.flags++
    }

    /**
     * Discover the tile.
     * @param tile the [Tile] to discover.
     * @return the remaining not bomb tiles.
     */
    fun discoverTile(tile: Tile, isTouched: Boolean): Int {
        tile.discover()
        if (isTouched) {
            tile.touched()
        }
        remainingNotBombTiles--
        return remainingNotBombTiles
    }

    /**
     * Iterate the bomb positions list discovering the correspondent tile.
     * @param touchedIndex the index of the touched tile in board.
     * @param onDiscovered the function to invoke when the bombs are discovered.
     */
    fun discoverAllBombs(touchedIndex: Int, onDiscovered: (Int, Int) -> Unit) {
        bombPositions.forEach {
            val tile = matrix[it]
            val isTouched = it == touchedIndex
            onDiscovered.invoke(it, discoverTile(tile, isTouched))
        }
    }

    /**
     * Clean the adjacent tiles until the flag is reached
     */
    fun cleanAdjacent(index: Int, onClean: (Tile, Int) -> Unit) {

        // Get the tile at index to check if the logic must continue or stop.
        val tile = matrix.getOrNull(index)
        tile ?: return

        // If is a flag clean it but stops to iterate.
        if (tile.type == Type.FLAG || tile.state == State.DISCOVERED) {
            onClean.invoke(tile, index)
            return
        }

        // Continue iteration
        onClean.invoke(tile, index)
        cleanAdjacent(getWestIndex(index), onClean)
        cleanAdjacent(getNorthWestIndex(index), onClean)
        cleanAdjacent(getNorthIndex(index), onClean)
        cleanAdjacent(getNorthEastIndex(index), onClean)
        cleanAdjacent(getEastIndex(index), onClean)
        cleanAdjacent(getSouthEastIndex(index), onClean)
        cleanAdjacent(getSouthIndex(index), onClean)
        cleanAdjacent(getSouthWestIndex(index), onClean)
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

    companion object {
        private const val TAG = "GameFactory"
    }
}