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
import com.dariobrux.minesweeper.other.toRemainingTime
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
        setDuration(120_000L)
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

    private lateinit var adapter: GameAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_game, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize the board
        val tiles = viewModel.getBoard()

        // Initialize the adapter and populate the grid
        adapter = GameAdapter(requireContext(), tiles, this)
        grid?.let {
            it.layoutManager = GridLayoutManager(requireContext(), tiles.size.sqrt())
            it.adapter = adapter
        }

        // Observe the score and update the TextView when the score changes
        viewModel.score.observe(this.viewLifecycleOwner) {
            txtScore?.text = getString(R.string.score_format, it)
        }

        // Start the timer
        timer.start()
    }

    override fun onItemSelected(item: Tile, position: Int) {
        viewModel.selectTile(item, position) {
            adapter.notifyItemChanged(it)
        }
        if (item.type == Type.BOMB) {
            endGame(EndCause.BOMB)
            return
        }
    }

    private fun endGame(endResult: EndCause) {
        timer.stop()
        adapter.isEnabled = false

        // Create a CookieBar to display a message at the bottom
        val cookieBar = CookieBar.build(requireActivity())
            .setDuration(10_000)
            .setCookiePosition(CookieBar.BOTTOM)

        when (endResult) {
            EndCause.BOMB -> {
                cookieBar.setTitle(getString(R.string.oh_no))
                cookieBar.setMessage(getString(R.string.message_lost_game_bomb))
                cookieBar.setBackgroundColor(R.color.red_600)
            }
            EndCause.TIMER -> {
                cookieBar.setTitle(getString(R.string.oh_no))
                cookieBar.setMessage(getString(R.string.message_lost_game_timer))
                cookieBar.setBackgroundColor(R.color.red_600)
            }
        }

        cookieBar.show()
    }

    /**
     * Enum used to indicate the ending cause of the game.
     */
    private enum class EndCause {
        BOMB,
        TIMER
    }
}