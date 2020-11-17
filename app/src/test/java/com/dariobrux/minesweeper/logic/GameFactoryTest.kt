package com.dariobrux.minesweeper.logic

import com.dariobrux.minesweeper.data.State
import com.dariobrux.minesweeper.data.Tile
import junit.framework.TestCase
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@Config(manifest = Config.NONE)
@RunWith(RobolectricTestRunner::class)
class GameFactoryTest : TestCase() {

    private val factory = GameFactory()

    @Test
    fun testGetBoard() {
        val n = 8
        val board = factory.getBoard()
        assertEquals(board.size, n * n)
    }

    @Test
    fun getNxNShuffledList() {
        val n = 8
        val list = Array(n * n) { it }
        val factoryList = factory.getNxNShuffledList()
        assertEquals(factoryList.size, list.size)
    }

    @Test
    fun testWestInvalidIndex() {
        assertEquals(factory.getWestIndex(0), -1)
        assertEquals(factory.getWestIndex(8), -1)
        assertEquals(factory.getWestIndex(16), -1)
        assertEquals(factory.getWestIndex(24), -1)
        assertEquals(factory.getWestIndex(32), -1)
        assertEquals(factory.getWestIndex(40), -1)
        assertEquals(factory.getWestIndex(48), -1)
        assertEquals(factory.getWestIndex(56), -1)
    }

    @Test
    fun testWestIndex() {
        assertEquals(factory.getWestIndex(1), 0)
        assertEquals(factory.getWestIndex(4), 3)
        assertEquals(factory.getWestIndex(7), 6)
        assertEquals(factory.getWestIndex(9), 8)
        assertEquals(factory.getWestIndex(12), 11)
        assertEquals(factory.getWestIndex(15), 14)
        assertEquals(factory.getWestIndex(17), 16)
        assertEquals(factory.getWestIndex(31), 30)
        assertEquals(factory.getWestIndex(41), 40)
        assertEquals(factory.getWestIndex(47), 46)
    }

    @Test
    fun testNorthWestInvalidIndex() {
        assertEquals(factory.getNorthWestIndex(0), -1)
        assertEquals(factory.getNorthWestIndex(4), -1)
        assertEquals(factory.getNorthWestIndex(7), -1)
        assertEquals(factory.getNorthWestIndex(8), -1)
        assertEquals(factory.getNorthWestIndex(16), -1)
        assertEquals(factory.getNorthWestIndex(24), -1)
        assertEquals(factory.getNorthWestIndex(32), -1)
        assertEquals(factory.getNorthWestIndex(40), -1)
        assertEquals(factory.getNorthWestIndex(48), -1)
        assertEquals(factory.getNorthWestIndex(56), -1)
    }

    @Test
    fun testNorthWestIndex() {
        assertEquals(factory.getNorthWestIndex(9), 0)
        assertEquals(factory.getNorthWestIndex(12), 3)
        assertEquals(factory.getNorthWestIndex(15), 6)
        assertEquals(factory.getNorthWestIndex(25), 16)
        assertEquals(factory.getNorthWestIndex(39), 30)
        assertEquals(factory.getNorthWestIndex(44), 35)
        assertEquals(factory.getNorthWestIndex(55), 46)
        assertEquals(factory.getNorthWestIndex(57), 48)
        assertEquals(factory.getNorthWestIndex(60), 51)
        assertEquals(factory.getNorthWestIndex(63), 54)
    }

    @Test
    fun testNorthInvalidIndex() {
        assertEquals(factory.getNorthIndex(0), -1)
        assertEquals(factory.getNorthIndex(1), -1)
        assertEquals(factory.getNorthIndex(2), -1)
        assertEquals(factory.getNorthIndex(3), -1)
        assertEquals(factory.getNorthIndex(4), -1)
        assertEquals(factory.getNorthIndex(5), -1)
        assertEquals(factory.getNorthIndex(6), -1)
        assertEquals(factory.getNorthIndex(7), -1)
    }

    @Test
    fun testNorthIndex() {
        assertEquals(factory.getNorthIndex(8), 0)
        assertEquals(factory.getNorthIndex(9), 1)
        assertEquals(factory.getNorthIndex(15), 7)
        assertEquals(factory.getNorthIndex(17), 9)
        assertEquals(factory.getNorthIndex(31), 23)
        assertEquals(factory.getNorthIndex(41), 33)
        assertEquals(factory.getNorthIndex(47), 39)
        assertEquals(factory.getNorthIndex(56), 48)
        assertEquals(factory.getNorthIndex(60), 52)
        assertEquals(factory.getNorthIndex(63), 55)
    }

    @Test
    fun testNorthEastInvalidIndex() {
        assertEquals(factory.getNorthEastIndex(0), -1)
        assertEquals(factory.getNorthEastIndex(4), -1)
        assertEquals(factory.getNorthEastIndex(7), -1)
        assertEquals(factory.getNorthEastIndex(15), -1)
        assertEquals(factory.getNorthEastIndex(23), -1)
        assertEquals(factory.getNorthEastIndex(31), -1)
        assertEquals(factory.getNorthEastIndex(39), -1)
        assertEquals(factory.getNorthEastIndex(47), -1)
        assertEquals(factory.getNorthEastIndex(55), -1)
        assertEquals(factory.getNorthEastIndex(63), -1)
    }

    @Test
    fun testNorthEastIndex() {
        assertEquals(factory.getNorthEastIndex(8), 1)
        assertEquals(factory.getNorthEastIndex(9), 2)
        assertEquals(factory.getNorthEastIndex(12), 5)
        assertEquals(factory.getNorthEastIndex(16), 9)
        assertEquals(factory.getNorthEastIndex(32), 25)
        assertEquals(factory.getNorthEastIndex(41), 34)
        assertEquals(factory.getNorthEastIndex(62), 55)
    }

    @Test
    fun testEastInvalidIndex() {
        assertEquals(factory.getEastIndex(7), -1)
        assertEquals(factory.getEastIndex(15), -1)
        assertEquals(factory.getEastIndex(23), -1)
        assertEquals(factory.getEastIndex(31), -1)
        assertEquals(factory.getEastIndex(39), -1)
        assertEquals(factory.getEastIndex(47), -1)
        assertEquals(factory.getEastIndex(55), -1)
        assertEquals(factory.getEastIndex(63), -1)
    }

    @Test
    fun testEastIndex() {
        assertEquals(factory.getEastIndex(8), 9)
        assertEquals(factory.getEastIndex(9), 10)
        assertEquals(factory.getEastIndex(12), 13)
        assertEquals(factory.getEastIndex(16), 17)
        assertEquals(factory.getEastIndex(32), 33)
        assertEquals(factory.getEastIndex(41), 42)
        assertEquals(factory.getEastIndex(62), 63)
    }

    @Test
    fun testSouthEastInvalidIndex() {
        assertEquals(factory.getSouthEastIndex(7), -1)
        assertEquals(factory.getSouthEastIndex(15), -1)
        assertEquals(factory.getSouthEastIndex(23), -1)
        assertEquals(factory.getSouthEastIndex(31), -1)
        assertEquals(factory.getSouthEastIndex(39), -1)
        assertEquals(factory.getSouthEastIndex(47), -1)
        assertEquals(factory.getSouthEastIndex(55), -1)
        assertEquals(factory.getSouthEastIndex(56), -1)
        assertEquals(factory.getSouthEastIndex(61), -1)
        assertEquals(factory.getSouthEastIndex(62), -1)
        assertEquals(factory.getSouthEastIndex(63), -1)
    }

    @Test
    fun testSouthEastIndex() {
        assertEquals(factory.getSouthEastIndex(0), 9)
        assertEquals(factory.getSouthEastIndex(4), 13)
        assertEquals(factory.getSouthEastIndex(6), 15)
        assertEquals(factory.getSouthEastIndex(8), 17)
        assertEquals(factory.getSouthEastIndex(9), 18)
        assertEquals(factory.getSouthEastIndex(12), 21)
        assertEquals(factory.getSouthEastIndex(16), 25)
        assertEquals(factory.getSouthEastIndex(32), 41)
        assertEquals(factory.getSouthEastIndex(41), 50)
        assertEquals(factory.getSouthEastIndex(54), 63)
    }

    @Test
    fun testSouthInvalidIndex() {
        assertEquals(factory.getSouthIndex(56), -1)
        assertEquals(factory.getSouthIndex(57), -1)
        assertEquals(factory.getSouthIndex(58), -1)
        assertEquals(factory.getSouthIndex(59), -1)
        assertEquals(factory.getSouthIndex(60), -1)
        assertEquals(factory.getSouthIndex(61), -1)
        assertEquals(factory.getSouthIndex(62), -1)
        assertEquals(factory.getSouthIndex(63), -1)
    }

    @Test
    fun testSouthIndex() {
        assertEquals(factory.getSouthIndex(0), 8)
        assertEquals(factory.getSouthIndex(4), 12)
        assertEquals(factory.getSouthIndex(6), 14)
        assertEquals(factory.getSouthIndex(8), 16)
        assertEquals(factory.getSouthIndex(9), 17)
        assertEquals(factory.getSouthIndex(12), 20)
        assertEquals(factory.getSouthIndex(16), 24)
        assertEquals(factory.getSouthIndex(32), 40)
        assertEquals(factory.getSouthIndex(41), 49)
        assertEquals(factory.getSouthIndex(54), 62)
        assertEquals(factory.getSouthIndex(55), 63)
    }

    @Test
    fun testSouthWestInvalidIndex() {
        assertEquals(factory.getSouthWestIndex(0), -1)
        assertEquals(factory.getSouthWestIndex(8), -1)
        assertEquals(factory.getSouthWestIndex(16), -1)
        assertEquals(factory.getSouthWestIndex(24), -1)
        assertEquals(factory.getSouthWestIndex(32), -1)
        assertEquals(factory.getSouthWestIndex(40), -1)
        assertEquals(factory.getSouthWestIndex(48), -1)
        assertEquals(factory.getSouthWestIndex(56), -1)
        assertEquals(factory.getSouthWestIndex(57), -1)
        assertEquals(factory.getSouthWestIndex(58), -1)
        assertEquals(factory.getSouthWestIndex(59), -1)
        assertEquals(factory.getSouthWestIndex(60), -1)
        assertEquals(factory.getSouthWestIndex(61), -1)
        assertEquals(factory.getSouthWestIndex(62), -1)
        assertEquals(factory.getSouthWestIndex(63), -1)
    }

    @Test
    fun testSouthWestIndex() {
        assertEquals(factory.getSouthWestIndex(1), 8)
        assertEquals(factory.getSouthWestIndex(4), 11)
        assertEquals(factory.getSouthWestIndex(6), 13)
        assertEquals(factory.getSouthWestIndex(7), 14)
        assertEquals(factory.getSouthWestIndex(9), 16)
        assertEquals(factory.getSouthWestIndex(12), 19)
        assertEquals(factory.getSouthWestIndex(15), 22)
        assertEquals(factory.getSouthWestIndex(33), 40)
        assertEquals(factory.getSouthWestIndex(41), 48)
        assertEquals(factory.getSouthWestIndex(54), 61)
        assertEquals(factory.getSouthWestIndex(55), 62)
    }
}