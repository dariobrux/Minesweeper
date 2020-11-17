package com.dariobrux.minesweeper.ui.game

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dariobrux.minesweeper.data.State
import com.dariobrux.minesweeper.data.Tile
import com.dariobrux.minesweeper.data.Type
import com.dariobrux.minesweeper.logic.GameFactory

/**
 *
 * Created by Dario Bruzzese on 17/11/2020.
 *
 * This is the ViewModel, with logic interaction between the game and the fragment.
 *
 */
class GameViewModel @ViewModelInject constructor(private val gameFactory: GameFactory) : ViewModel() {

    /**
     * This is the player score. It increments when a tile (!= bomb) is discovered.
     */
    val score = MutableLiveData(0)

    /**
     * Get the board created by [GameFactory]
     * @return the list with all [Tile] representing the board.
     */
    fun getBoard(): List<Tile> {
        score.value = 0
        return gameFactory.getBoard()
    }

    /**
     * Call to change the state of a tile selected. The tile from state COVERED passes
     * to state DISCOVERED. If a tile is EMPTY, the adjacent tiles must be cleaned.
     * @param tile the selected [Tile]
     * @param position the index of the tile in board.
     * @param onDiscovered function to invoke when the tile is processed.
     */
    fun selectTile(tile: Tile, position: Int, onDiscovered: (Int, Int) -> Unit) {

        // Return if a tile has already been discovered.
        if (tile.state == State.DISCOVERED) {
            return
        }

        // Check the state.
        // If Bomb, the discover all the bombs and the game ends.
        // If Empty, clean and process the adjacent tiles and increment score.
        // If Flag, discover the tile, process the adjacent and increment score.
        when (tile.type) {
            Type.BOMB -> {
                processAllBombs(position, onDiscovered)
            }
            Type.EMPTY -> {

                // clean the adjacent tiles
                gameFactory.cleanAdjacent(position) { adjacentTile, index ->
                    if (adjacentTile.state == State.DISCOVERED) {
                        return@cleanAdjacent
                    }

                    // Discover the processed tiles incrementing score.
                    processTileScore(adjacentTile, position, index, onDiscovered)
                }
            }
            else -> {

                // Discover the processed tile incrementing score.
                processTileScore(tile, position, onDiscovered)
            }
        }
    }

    /**
     * Discover all the bombs.
     * @param touchedIndex the index of the touched tile in board.
     * @param onDiscovered the function to invoke when the bombs have been discovered.
     */
    private fun processAllBombs(touchedIndex: Int, onDiscovered: (Int, Int) -> Unit) {
        gameFactory.discoverAllBombs(touchedIndex, onDiscovered)
    }

    /**
     * Discover the tile, launch callback to come back to the UI.
     * @param tile the tile to discover.
     * @param touchedIndex the index of the touched tile in board.
     * @param index the index of the tile in board.
     * @param onDiscovered callback to come back to the UI.
     */
    private fun processTile(tile: Tile, touchedIndex: Int, index: Int, onDiscovered: (Int, Int) -> Unit) {
        val isTouched = index == touchedIndex
        val remainingNotBombTiles = gameFactory.discoverTile(tile, isTouched)
        onDiscovered.invoke(index, remainingNotBombTiles)
    }

    /**
     * Discover the tile, launch callback to come back to the UI and increment the score.
     * @param tile the tile to discover.
     * @param touchedIndex the index of the touched tile in board.
     * @param index the index of the tile in board.
     * @param onDiscovered callback to come back to the UI.
     */
    private fun processTileScore(tile: Tile, touchedIndex: Int, index: Int, onDiscovered: (Int, Int) -> Unit) {
        processTile(tile, touchedIndex, index, onDiscovered)
        score.value = score.value?.plus(1)
    }

    /**
     * Discover the tile, launch callback to come back to the UI and increment the score.
     * @param tile the tile to discover.
     * @param touchedIndex the index of the touched tile in board.
     * @param onDiscovered callback to come back to the UI.
     */
    private fun processTileScore(tile: Tile, touchedIndex: Int, onDiscovered: (Int, Int) -> Unit) {
        processTile(tile, touchedIndex, touchedIndex, onDiscovered)
        score.value = score.value?.plus(1)
    }
}