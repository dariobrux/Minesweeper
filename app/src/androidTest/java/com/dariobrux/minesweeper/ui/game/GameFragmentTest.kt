package com.dariobrux.minesweeper.ui.game

import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.dariobrux.minesweeper.R
import com.dariobrux.minesweeper.ui.MainActivity
import junit.framework.TestCase
import org.junit.After
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class GameFragmentTest : TestCase() {

    private lateinit var scenario: ActivityScenario<MainActivity>

    @After
    fun cleanup() {
        scenario.close()
    }

    @Test
    fun testFragmentVisible() {
        scenario = launchActivity()
        Espresso.onView(withId(R.id.containerGameRoot)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun testScoreVisible() {
        scenario = launchActivity()
        Espresso.onView(withId(R.id.txtScore)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun testScoreText() {
        scenario = launchActivity()
        Espresso.onView(withText("Score: 0")).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun testBoardVisible() {
        scenario = launchActivity()
        Espresso.onView(withId(R.id.grid)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun testCountdownTimerVisible() {
        scenario = launchActivity()
        Thread.sleep(1000)
        Espresso.onView(withId(R.id.txtPreTimer)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun testTimerVisible() {
        scenario = launchActivity()
        Thread.sleep(6000)
        Espresso.onView(withId(R.id.txtTimer)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun testBoardClicked() {
        scenario = launchActivity()
        Thread.sleep(6000)
        Espresso.onView(withId(R.id.grid)).perform(
            RecyclerViewActions.actionOnItemAtPosition<GameAdapter.GameViewHolder>(0, MyViewAction.clickChildViewWithId(R.id.card))
        )
    }

    @Test
    fun testBoardNewGameVisible() {
        scenario = launchActivity()
        Thread.sleep(6000)
        repeat(49) {
            Espresso.onView(withId(R.id.grid)).perform(
                RecyclerViewActions.actionOnItemAtPosition<GameAdapter.GameViewHolder>(it, MyViewAction.clickChildViewWithId(R.id.card))
            )
        }
        Espresso.onView(withId(R.id.txtNewGame)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }
}