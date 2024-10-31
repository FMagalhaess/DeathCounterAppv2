package com.example.death_counter.com.death_counter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.primeiroapp.R
import com.google.android.material.card.MaterialCardView

class GameAdapter(private val games:List<Game>,
                  private val onGameClick: (Game) -> Unit,
                  private val onEditClick: (Game) -> Unit,
                  private val onFavoriteClick: (Game) -> Unit) : Adapter<GameAdapter.GameViewHolder>() {
    class GameViewHolder(view:View):ViewHolder(view){
        val name:TextView = view.findViewById(R.id.gameCard)
        val counter:TextView = view.findViewById(R.id.cardDeathCounter)
        val cardView:MaterialCardView = view.findViewById(R.id.game_card_view)
        val editButton: Button = view.findViewById(R.id.edit_game)
        val favoriteBtn: Button = view.findViewById(R.id.favorite_game)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.game_item, parent, false)
        return GameViewHolder(view)
    }

    override fun getItemCount(): Int = games.size


    override fun onBindViewHolder(holder: GameViewHolder, position: Int) {
        val game = games[position]
        holder.name.text = game.nome
        holder.counter.text = game.deaths.toString()
        val favoriteBtn = if (game.favorite) R.drawable.favorited else R.drawable.non_favorited
        holder.favoriteBtn.setCompoundDrawablesWithIntrinsicBounds(0, favoriteBtn, 0, 0)
        holder.cardView.setOnClickListener{onGameClick(game)}
        holder.editButton.setOnClickListener{onEditClick(game)}
        holder.favoriteBtn.setOnClickListener{
            game.favorite = !game.favorite
            onFavoriteClick(game)
            notifyItemChanged(position)
        }
    }
}