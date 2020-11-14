package com.dariobrux.minesweeper

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.dariobrux.minesweeper.logic.GameFactory

class GameViewModel @ViewModelInject constructor(private val gameFactory: GameFactory) : ViewModel() {

    fun getBoard() {
        gameFactory.getField()
    }
}