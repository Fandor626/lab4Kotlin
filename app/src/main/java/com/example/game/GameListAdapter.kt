package com.example.game

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.game.data.Game
import com.example.game.data.getFormattedPrice
import com.example.game.databinding.ItemListItemBinding


class GameListAdapter(private val onGameClicked: (Game) -> Unit) :
    ListAdapter<Game, GameListAdapter.GameViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameViewHolder {
        return GameViewHolder(
            ItemListItemBinding.inflate(
                LayoutInflater.from(
                    parent.context
                )
            )
        )
    }

    override fun onBindViewHolder(holder: GameViewHolder, position: Int) {
        val current = getItem(position)
        holder.itemView.setOnClickListener {
            onGameClicked(current)
        }
        holder.bind(current)
    }

    class GameViewHolder(private var binding: ItemListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(game: Game) {
            binding.gameName.text = game.gameName
            binding.gamePrice.text = game.getFormattedPrice()
            binding.gameQuantity.text = game.quantityInStock.toString()
        }
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<Game>() {
            override fun areItemsTheSame(oldGame: Game, newGame: Game): Boolean {
                return oldGame === newGame
            }

            override fun areContentsTheSame(oldGame: Game, newGame: Game): Boolean {
                return oldGame.gameName == newGame.gameName
            }
        }
    }
}
