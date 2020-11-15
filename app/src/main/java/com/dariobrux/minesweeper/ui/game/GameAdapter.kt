package com.dariobrux.minesweeper.ui.game

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dariobrux.minesweeper.R
import com.dariobrux.minesweeper.data.State
import com.dariobrux.minesweeper.data.Tile
import com.dariobrux.minesweeper.other.sqrt
import com.dariobrux.minesweeper.ui.game.GameAdapter.ViewHolder
import com.google.android.material.card.MaterialCardView

class GameAdapter(private val context: Context, private val items: List<Tile>, private val listener: OnItemSelectedListener?) : RecyclerView.Adapter<ViewHolder>() {

    interface OnItemSelectedListener {
        fun onItemSelected(item: Tile, position: Int)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.item_tile, null)
        val params = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, viewGroup.width / items.size.sqrt())
        view.layoutParams = params
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item: Tile = items[position]

        // Write on the tile only if the tile is discovered
        if (item.state == State.DISCOVERED) {
            holder.txt.text = item.flags.toString()
        }

        // Add the listener on click
        holder.card.setOnClickListener {
            listener?.onItemSelected(item, position)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var card = view.findViewById<View>(R.id.card) as MaterialCardView
        var txt = view.findViewById<View>(R.id.txt) as TextView
    }
}