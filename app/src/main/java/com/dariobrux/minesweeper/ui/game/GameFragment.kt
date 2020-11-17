package com.dariobrux.minesweeper.ui.game

import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.dariobrux.minesweeper.R
import com.dariobrux.minesweeper.data.Tile
import com.dariobrux.minesweeper.data.Type
import com.dariobrux.minesweeper.other.Constants
import com.dariobrux.minesweeper.other.extension.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_game.*
import org.aviran.cookiebar2.CookieBar
import timber.log.Timber

/**
 *
 * Created by Dario Bruzzese on 13/11/2020.
 *
 * This is the game fragment, where you can play.
 *
 */
@AndroidEntryPoint
class GameFragment : Fragment(), GameAdapter.OnItemSelectedListener {

    /**
     * The ViewModel
     */
    private val viewModel: GameViewModel by viewModels()

    /**
     * This is the timer that appears when the app launches, showing a countdown
     * in the middle of the screen.
     */
    private val timerPreLaunch: CountDownTimer = object : CountDownTimer(Constants.TIMER_COUNTDOWN, 1000L) {

        /**
         * Invoked when the timer ends.
         * When this timer ends, start the game timer.
         */
        override fun onFinish() {
            txtPreTimer?.toGone()
            adapter.isEnabled = true
            timerGame.start()
        }

        /**
         * Invoked every seconds
         * @param millisUntilFinished the current milliseconds elapsed.
         */
        override fun onTick(millisUntilFinished: Long) {
            Timber.tag(TAG).d("Game starts in $millisUntilFinished")
            txtPreTimer?.text = if (millisUntilFinished != 0L) {
                millisUntilFinished.toFormattedTime("s")
            } else {
                getString(R.string.start)
            }
        }
    }

    /**
     * This is the timer game.
     */
    private val timerGame = object : CountDownTimer(Constants.TIMER_GAME, 1000L) {

        /**
         * Invoked when the timer ends
         */
        override fun onFinish() {
            endGame(EndCause.TIMER)
        }

        /**
         * Invoked every seconds
         * @param millisUntilFinished the current milliseconds elapsed.
         */
        override fun onTick(millisUntilFinished: Long) {
            setTimerTextFormat(millisUntilFinished)
        }
    }

    private fun setTimerTextFormat(milliseconds: Long) {
        txtTimer?.text = milliseconds.toFormattedTime("m:ss")
    }

    /**
     * The grid adapter
     */
    private lateinit var adapter: GameAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_game, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        startGame()

        // Observe the score and update the TextView when the score changes
        viewModel.score.observe(this.viewLifecycleOwner) {
            txtScore?.text = getString(R.string.score_format, it)
        }

        txtNewGame?.setOnClickListener {
            it.toGone()
            startGame()
            CookieBar.dismiss(requireActivity())
        }
    }

    /**
     * Start the game creating the board, populating the grid and starting the timer.
     */
    private fun startGame() {
        // Initialize the board
        val tiles = viewModel.getBoard()

        grid?.post {
            // Initialize the adapter and populate the grid
            adapter = GameAdapter(requireContext(), tiles, this)
            grid?.let {
                it.layoutManager = GridLayoutManager(requireContext(), tiles.size.sqrt())
                it.adapter = adapter
            }
        }

        setTimerTextFormat(Constants.TIMER_GAME)

        // Start the timer
        txtPreTimer?.toVisible()
        timerPreLaunch.start()
    }

    override fun onDetach() {
        super.onDetach()
        timerPreLaunch.cancel()
        timerGame.cancel()
    }

    /**
     * Invoked when a tile is clicked.
     * If the tile is a bomb, lose game.
     * If the remaining tiles are 0, win game
     * @param item the tile clicked
     * @param position the position of the tile in the grid
     */
    override fun onItemSelected(item: Tile, position: Int) {

        viewModel.selectTile(item, position) { index, remainingNotBombTiles ->
            adapter.notifyItemChanged(index)

            // There are not other valid tiles to discover. You win
            if (remainingNotBombTiles == 0) {
                endGame(EndCause.WIN)
                return@selectTile
            }
        }

        // You have discover a bomb. You lose.
        when (item.type) {
            Type.BOMB -> {
                // Vibrate the phone
                requireContext().vibrate(300)
                endGame(EndCause.BOMB)
            }
            else -> {
                // Vibrate the phone
                requireContext().vibrate(50)
            }
        }
    }

    /**
     * Call this method when the game ends. It handles all the end causes,
     * stopping the timer and letting appear the new game label.
     * @param endResult the [EndCause]
     */
    private fun endGame(endResult: EndCause) {
        timerGame.cancel()
        adapter.isEnabled = false
        txtNewGame?.toVisible()

        // Create a CookieBar to display a message at the bottom
        val builder = CookieBar.build(requireActivity())
            .setDuration(10_000)
            .setCookiePosition(CookieBar.BOTTOM)

        when (endResult) {
            EndCause.BOMB -> {
                builder.setTitle(getString(R.string.oh_no))
                builder.setMessage(getString(R.string.message_lost_game_bomb))
                builder.setBackgroundColor(R.color.red_600)
                txtPreTimer?.let {
                    it.text = getString(R.string.you_lost)
                    it.toVisible()
                }
            }
            EndCause.TIMER -> {
                builder.setTitle(getString(R.string.oh_no))
                builder.setMessage(getString(R.string.message_lost_game_timer))
                builder.setBackgroundColor(R.color.red_600)
                txtPreTimer?.let {
                    it.text = getString(R.string.you_lost)
                    it.toVisible()
                }
            }
            EndCause.WIN -> {
                builder.setTitle(getString(R.string.great))
                builder.setMessage(getString(R.string.message_win_game))
                builder.setBackgroundColor(R.color.green_400)
                txtPreTimer?.let {
                    it.text = getString(R.string.you_win)
                    it.toVisible()
                }
            }
        }

        builder.show().view
    }

    /**
     * Enum used to indicate the ending cause of the game.
     */
    private enum class EndCause {
        BOMB,
        TIMER,
        WIN
    }

    companion object {
        private const val TAG = "GameFragment"
    }
}