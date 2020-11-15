package com.dariobrux.minesweeper.ui.game

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.dariobrux.kotimer.Timer
import com.dariobrux.kotimer.interfaces.OnTimerListener
import com.dariobrux.kotimer.interfaces.OnTimerListenerAdapter
import com.dariobrux.minesweeper.R
import com.dariobrux.minesweeper.data.Tile
import com.dariobrux.minesweeper.other.sqrt
import com.dariobrux.minesweeper.other.toRemainingTime
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_game.*
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class GameFragment : Fragment(), GameAdapter.OnItemSelectedListener {

    private val viewModel: GameViewModel by viewModels()

    // The game timer
    private val timer: Timer = Timer().apply {
        setDuration(120_000L)
        setOnTimerListener(object : OnTimerListenerAdapter() {
            override fun onTimerRun(milliseconds: Long) {
                txtTimer?.text = milliseconds.toRemainingTime()
            }
        }, true)
    }

    private lateinit var adapter: GameAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_game, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tiles = viewModel.getBoard()

        adapter = GameAdapter(requireContext(), tiles, this)

        grid?.let {
            it.layoutManager = GridLayoutManager(requireContext(), tiles.size.sqrt())
            it.adapter = adapter
        }

        // Observe the score and update the TextView when the score changes
        viewModel.score.observe(this.viewLifecycleOwner) {
            txtScore?.text = getString(R.string.score_format, it)
        }

        timer.start()
    }

    override fun onItemSelected(item: Tile, position: Int) {
        viewModel.selectTile(item, position) {
            adapter.notifyItemChanged(it)
        }
    }
}