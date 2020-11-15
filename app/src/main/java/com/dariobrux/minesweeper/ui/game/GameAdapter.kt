package com.dariobrux.minesweeper.ui.game

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dariobrux.minesweeper.R
import com.dariobrux.minesweeper.data.State
import com.dariobrux.minesweeper.data.Tile
import com.dariobrux.minesweeper.data.Type
import com.dariobrux.minesweeper.other.sqrt
import com.dariobrux.minesweeper.other.toVisible
import com.dariobrux.minesweeper.ui.game.GameAdapter.GameViewHolder
import com.google.android.material.card.MaterialCardView

class GameAdapter(private val context: Context, private val items: List<Tile>, private val listener: OnItemSelectedListener?) : RecyclerView.Adapter<GameViewHolder>() {

    interface OnItemSelectedListener {
        fun onItemSelected(item: Tile, position: Int)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): GameViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.item_tile, null)
        val params = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, viewGroup.width / items.size.sqrt())
        view.layoutParams = params
        return GameViewHolder(view)
    }

    override fun onBindViewHolder(holder: GameViewHolder, position: Int) {
        val item: Tile = items[position]

        // Write on the tile only if the tile is discovered
        if (item.state == State.DISCOVERED) {
            if (item.type == Type.BOMB) {
                holder.img.toVisible()
            } else {
                holder.txt.text = item.flags.toString()
            }
        }

        // Add the listener on click
        holder.card.setOnClickListener {
            listener?.onItemSelected(item, position)
        }
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