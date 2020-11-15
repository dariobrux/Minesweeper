package com.dariobrux.minesweeper.ui.game

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dariobrux.minesweeper.R
import com.dariobrux.minesweeper.data.Tile
import com.dariobrux.minesweeper.other.sqrt
import com.dariobrux.minesweeper.ui.game.GameAdapter.ViewHolder

class GameAdapter(private val context: Context, private val items: List<Tile>) : RecyclerView.Adapter<ViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.item_tile, null)
        val params = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, viewGroup.width / items.size.sqrt())
        view.layoutParams = params
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, i: Int) {
        val item: Tile = items[i]

        holder.txt.text = item.flags.toString()
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var txt: TextView = view.findViewById<View>(R.id.txt) as TextView
    }
}