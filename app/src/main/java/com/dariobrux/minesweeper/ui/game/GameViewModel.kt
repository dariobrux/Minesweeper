package com.dariobrux.minesweeper.ui.game

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dariobrux.minesweeper.data.State
import com.dariobrux.minesweeper.data.Tile
import com.dariobrux.minesweeper.data.Type
import com.dariobrux.minesweeper.logic.GameFactory

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
        return gameFactory.getBoard()
    }

    /**
     * Call to change the state of a tile selected. The tile from state COVERED passes
     * to state DISCOVERED. If a tile is EMPTY, the adjacent tiles must be cleaned.
     * @param tile the selected [Tile]
     * @param position the index of the tile in board.
     * @param onDiscovered function to invoke when the tile is processed.
     */
    fun selectTile(tile: Tile, position: Int, onDiscovered: (Int) -> Unit) {

        // Return if a tile has already been discovered.
        if (tile.state == State.DISCOVERED) {
            return
        }

        // Check the state.
        // If Bomb, the tile discovers but the game ends.
        // If Empty, clean and process the adjacent tiles and increment score.
        // If Flag, discover the tile, process the adjacent and increment score.
        when (tile.type) {
            Type.BOMB -> {
                processTile(tile, position, onDiscovered)
            }
            Type.EMPTY -> {

                // clean the adjacent tiles
                gameFactory.cleanAdjacent(position) { adjacentTile, index ->
                    if (adjacentTile.state == State.DISCOVERED) {
                        return@cleanAdjacent
                    }

                    // Discover the processed tiles incrementing score.
                    processTileScore(adjacentTile, index, onDiscovered)
                }
            }
            else -> {

                // Discover the processed tiles incrementing score.
                processTileScore(tile, position, onDiscovered)
            }
        }
    }

    /**
     * Discover the tile, launch callback to come back to the UI.
     * @param tile the tile to discover.
     * @param index the index of the tile in board.
     * @param onDiscovered callback to come back to the UI.
     */
    private fun processTile(tile: Tile, index: Int, onDiscovered: (Int) -> Unit) {
        tile.discover()
        onDiscovered.invoke(index)
    }

    /**
     * Discover the tile, launch callback to come back to the UI and increment the score.
     * @param tile the tile to discover.
     * @param index the index of the tile in board.
     * @param onDiscovered callback to come back to the UI.
     */
    private fun processTileScore(tile: Tile, index: Int, onDiscovered: (Int) -> Unit) {
        processTile(tile, index, onDiscovered)
        score.value = score.value?.plus(1)
    }
}