package com.dariobrux.minesweeper.ui.game

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.dariobrux.kotimer.Timer
import com.dariobrux.kotimer.interfaces.OnTimerListenerAdapter
import com.dariobrux.minesweeper.R
import com.dariobrux.minesweeper.data.Tile
import com.dariobrux.minesweeper.data.Type
import com.dariobrux.minesweeper.other.sqrt
import com.dariobrux.minesweeper.other.toGone
import com.dariobrux.minesweeper.other.toRemainingTime
import com.dariobrux.minesweeper.other.toVisible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_game.*
import org.aviran.cookiebar2.CookieBar


@AndroidEntryPoint
class GameFragment : Fragment(), GameAdapter.OnItemSelectedListener {

    /**
     * The ViewModel
     */
    private val viewModel: GameViewModel by viewModels()

    /**
     * This is the timer game.
     */
    private val timer: Timer = Timer().apply {
        setDuration(180_000)
        setOnTimerListener(object : OnTimerListenerAdapter() {

            /**
             * Invoked every seconds
             * @param milliseconds the current milliseconds elapsed.
             */
            override fun onTimerRun(milliseconds: Long) {
                txtTimer?.text = milliseconds.toRemainingTime()
            }

            /**
             * Invoked when the timer ends
             */
            override fun onTimerEnded() {
                endGame(EndCause.TIMER)
            }
        }, true)
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

        // Initialize the adapter and populate the grid
        adapter = GameAdapter(requireContext(), tiles, this)
        grid?.let {
            it.layoutManager = GridLayoutManager(requireContext(), tiles.size.sqrt())
            it.adapter = adapter
        }

        // Start the timer
        timer.start()
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
            }
        }

        // You have discover a bomb. You lose.
        if (item.type == Type.BOMB) {
            endGame(EndCause.BOMB)
            return
        }
    }

    /**
     * Call this method when the game ends. It handles all the end causes,
     * stopping the timer and letting appear the new game label.
     * @param endResult the [EndCause]
     */
    private fun endGame(endResult: EndCause) {
        timer.stop()
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
            }
            EndCause.TIMER -> {
                builder.setTitle(getString(R.string.oh_no))
                builder.setMessage(getString(R.string.message_lost_game_timer))
                builder.setBackgroundColor(R.color.red_600)
            }
            EndCause.WIN -> {
                builder.setTitle(getString(R.string.great))
                builder.setMessage(getString(R.string.message_win_game))
                builder.setBackgroundColor(R.color.green_400)
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
}