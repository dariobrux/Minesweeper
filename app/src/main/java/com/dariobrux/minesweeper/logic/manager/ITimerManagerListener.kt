package com.dariobrux.minesweeper.logic.manager

interface ITimerManagerListener {

    fun onTimerRun(millis: Long)
    fun onTimerFinish()
}