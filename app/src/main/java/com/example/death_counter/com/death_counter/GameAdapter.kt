package com.example.death_counter.com.death_counter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.death_counter.com.death_counter.data.database.GameDatabaseHelper
import com.example.primeiroapp.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView

class GameAdapter(
    private val context: Context,
    private var games: MutableList<Game>,
    private val gameDatabaseHelper: GameDatabaseHelper,
    val onGameClick: (Game) -> Unit,
    private val onEditClick: (Game) -> Unit,
    private val onFavoriteClick: (Game) -> Unit,
    private val onDeleteClick: (Game) -> Unit
) : Adapter<GameAdapter.GameViewHolder>() {

    class GameViewHolder(view: View) : ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.gameCard)
        val counter: TextView = view.findViewById(R.id.cardDeathCounter)
        val cardView: MaterialCardView = view.findViewById(R.id.game_card_view)
        val editButton: Button = view.findViewById(R.id.edit_game)
        val favoriteBtn: MaterialButton = view.findViewById(R.id.favorite_game)
        val deleteBtn: Button = view.findViewById( R.id.delete_game )
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.game_item, parent, false)
        return GameViewHolder(view)
    }
    fun showDeleteConfirmationDialog(game: Game) {
        val alertDialog = AlertDialog.Builder(context)
            .setTitle("Excluir Jogo")
            .setMessage("Tem certeza que deseja excluir esse jogo?")
            .setPositiveButton("Sim") { _, _ ->
                deleteGame(game)  // Chama a função de exclusão, passando o jogo
            }
            .setNegativeButton("Não", null)
            .create()

        alertDialog.show()
    }

    override fun getItemCount(): Int = games.size

    override fun onBindViewHolder(holder: GameViewHolder, position: Int) {
        val game = games[position]
        holder.name.text = game.nome
        holder.counter.text = game.deaths.toString()

        // Define o ícone de favorito
        val favoriteIcon = if (game.favorite) R.drawable.favorited else R.drawable.non_favorited
        holder.favoriteBtn.setIconResource(favoriteIcon)
        holder.favoriteBtn.setIconSize(100)

        // Quando o card é clicado, chama a ação de exibir detalhes do jogo
        holder.cardView.setOnClickListener { onGameClick(game) }

        // Quando o botão de editar é clicado, chama a ação de editar o jogo
        holder.editButton.setOnClickListener { onEditClick(game) }

        holder.deleteBtn.setOnClickListener{ onDeleteClick(game) }

        // Quando o botão de favorito é clicado
        holder.favoriteBtn.setOnClickListener {
            // Impede que o clique no botão de favorito seja propagado para o cardView
            it.setOnClickListener {  }

            // Alterna o status de favorito
            game.favorite = !game.favorite

            // Atualiza o banco de dados
            onFavoriteClick(game)

            // Atualiza a lista de jogos no adapter
            val updatedGames = gameDatabaseHelper.getAllGames().toMutableList()
            updateGames(updatedGames)

            // Notifica a mudança para o item específico
            notifyItemChanged(position)
        }
    }
    private fun deleteGame(game: Game) {
        // Deleta o jogo no banco de dados
        gameDatabaseHelper.deleteGameById(game.id)

        // Busca a posição do item na lista
        val position = games.indexOf(game)

        // Se o jogo foi encontrado na lista, removemos da lista e atualizamos a RecyclerView
        if (position >= 0) {
            games.removeAt(position)
            notifyItemRemoved(position)  // Notifica que o item foi removido
        }
    }


    // Método para atualizar a lista de jogos
    fun updateGames(newGames: MutableList<Game>) {
        games = newGames  // Atualiza a lista com os jogos novos
        notifyDataSetChanged()  // Notifica o RecyclerView para refletir as mudanças
    }
}
