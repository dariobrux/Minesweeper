package com.dariobrux.minesweeper.ui.game

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.dariobrux.minesweeper.R
import com.dariobrux.minesweeper.other.sqrt
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_game.*

@AndroidEntryPoint
class GameFragment : Fragment() {

    private val viewModel: GameViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_game, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tiles = viewModel.getBoard()

        grid?.let {
            it.layoutManager = GridLayoutManager(requireContext(), tiles.size.sqrt())
            it.adapter = GameAdapter(requireContext(), tiles)
        }
    }

}