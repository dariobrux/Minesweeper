package com.dariobrux.minesweeper.ui.game

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.dariobrux.minesweeper.data.Tile
import com.dariobrux.minesweeper.logic.GameFactory

class GameViewModel @ViewModelInject constructor(private val gameFactory: GameFactory) : ViewModel() {

    fun getBoard(): List<Tile> {
        return gameFactory.getBoard()
    }
}