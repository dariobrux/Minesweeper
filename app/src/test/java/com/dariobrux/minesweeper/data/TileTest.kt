package com.dariobrux.minesweeper.data

import junit.framework.TestCase
import org.junit.Test

class TileTest : TestCase() {

    @Test
    fun testDiscover() {
        val tile = Tile(state = State.COVERED)
        assertEquals(State.DISCOVERED, tile.apply {
            discover()
        }.state)
    }
}