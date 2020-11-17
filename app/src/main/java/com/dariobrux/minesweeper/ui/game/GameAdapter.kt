package com.dariobrux.minesweeper.ui.game

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.dariobrux.minesweeper.R
import com.dariobrux.minesweeper.data.State
import com.dariobrux.minesweeper.data.Tile
import com.dariobrux.minesweeper.data.Type
import com.dariobrux.minesweeper.other.extension.sqrt
import com.dariobrux.minesweeper.other.extension.toVisible
import com.dariobrux.minesweeper.ui.game.GameAdapter.GameViewHolder
import com.google.android.material.card.MaterialCardView

/**
 *
 * Created by Dario Bruzzese on 15/11/2020.
 *
 * This class is the adapter for the grid that shows each tile on the board in the RecyclerView.
 *
 */
class GameAdapter(private val context: Context, private val items: List<Tile>, private val listener: OnItemSelectedListener?) : RecyclerView.Adapter<GameViewHolder>() {

    /**
     * Used by other classes to enable or disable the click on items.
     */
    var isEnabled = false

    interface OnItemSelectedListener {

        /**
         * Invoked when a tile is clicked.
         * @param item the tile clicked
         * @param position the position of the tile in the grid
         */
        fun onItemSelected(item: Tile, position: Int)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): GameViewHolder {
        val view = View.inflate(context, R.layout.item_tile, null)
        val params = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, viewGroup.width / items.size.sqrt())
        view.layoutParams = params
        return GameViewHolder(view)
    }

    override fun onBindViewHolder(holder: GameViewHolder, position: Int) {
        val item: Tile = items[position]

        // Write on the tile only if the tile is discovered
        if (item.state == State.DISCOVERED) {
            when (item.type) {
                Type.BOMB -> {
                    configureDiscoveredBombs(holder, item)
                }
                Type.EMPTY -> {
                    configureDiscoveredEmpty(holder)
                }
                else -> {
                    configureDiscoveredFlags(holder, item)
                }
            }
        }

        // Add the listener on click
        holder.card.setOnClickListener {
            if (!isEnabled) return@setOnClickListener
            listener?.onItemSelected(item, position)
        }
    }

    /**
     * Configure the flag tile when it has been discovered.
     * @param holder the [GameViewHolder].
     * @param item the [Tile] selected.
     */
    private fun configureDiscoveredFlags(holder: GameViewHolder, item: Tile) {
        holder.txt.text = item.flags.toString()
        holder.card.setCardBackgroundColor(ContextCompat.getColor(context, R.color.orange_200))
    }

    /**
     * Configure the empty tile when it has been discovered.
     * @param holder the [GameViewHolder].
     */
    private fun configureDiscoveredEmpty(holder: GameViewHolder) {
        holder.card.setCardBackgroundColor(ContextCompat.getColor(context, R.color.blue_grey_900))
    }

    /**
     * Configure the bomb in the adapter when it has been discovered.
     * If the tile has been touched, set the explosion image.
     * Other bomb tiles uses the bomb image.
     * @param holder the [GameViewHolder].
     * @param item the [Tile] selected.
     */
    private fun configureDiscoveredBombs(holder: GameViewHolder, item: Tile) {
        if (item.isTouched) {
            holder.img.setImageResource(R.drawable.explosion)
        } else {
            holder.img.setImageResource(R.drawable.bomb)
        }
        holder.img.toVisible()
        holder.card.setCardBackgroundColor(ContextCompat.getColor(context, R.color.red_600))
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class GameViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var card = view.findViewById<View>(R.id.card) as MaterialCardView
        var txt = view.findViewById<View>(R.id.txt) as TextView
        var img = view.findViewById<View>(R.id.img) as ImageView
    }
}