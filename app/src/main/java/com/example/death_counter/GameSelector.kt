package com.example.death_counter

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.death_counter.com.death_counter.GameAdapter
import com.example.death_counter.com.death_counter.data.database.GameDatabaseHelper
import com.example.primeiroapp.MainActivity
import com.example.primeiroapp.R

class GameSelector : AppCompatActivity(), View.OnClickListener {

    private val gamesList: RecyclerView by lazy { findViewById(R.id.selectGame) }
    private val addGameToList: Button by lazy { findViewById(R.id.add_game_to_list) }
    private val closeActivity: Button by lazy { findViewById(R.id.return_button_game_selector) }
    private lateinit var gameAdapter: GameAdapter
    private lateinit var gameDatabaseHelper: GameDatabaseHelper

    private lateinit var mainActivityLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_game_selector)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.back_to_previous)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        addGameToList.setOnClickListener(this)
        closeActivity.setOnClickListener(this)

        gamesList.setPadding(0, 140, 0, 640)
        gamesList.clipToPadding = false

        gameDatabaseHelper = GameDatabaseHelper(this)
        val games = gameDatabaseHelper.getAllGames().toMutableList()

        gameAdapter = GameAdapter(
            context = this,  // Passando o contexto para o adapter
            games = games,
            gameDatabaseHelper = gameDatabaseHelper,  // Passando o gameDatabaseHelper
            onGameClick = { game ->
                val intent = Intent(this, MainActivity::class.java).apply {
                    putExtra("gameName", game.nome)
                    putExtra("gameDeaths", game.deaths)
                    putExtra("gameId", game.id)
                }
                startActivity(intent)
            },
            onDeleteClick = { game ->
                // Passando a função para deletar com a confirmação
                gameAdapter.showDeleteConfirmationDialog(game)
            },
            onEditClick = { game ->
                val intent = Intent(this, CreateOrEditGame::class.java).apply {
                    putExtra("gameName", game.nome)
                    putExtra("gameDeaths", game.deaths)
                    putExtra("gameId", game.id)
                }
                startActivity(intent)
            },
            onFavoriteClick = { game ->
                gameDatabaseHelper.updateGameFavoriteStatus(game.id, game.favorite)
                val updatedGames = gameDatabaseHelper.getAllGames().toMutableList()
                gameAdapter.updateGames(updatedGames)
            }
        )

        gameDatabaseHelper.setAdapter(gameAdapter)

        gamesList.layoutManager = LinearLayoutManager(this)
        gamesList.adapter = gameAdapter

        mainActivityLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val updatedGameId = result.data?.getIntExtra("updatedGameId", -1)
                if (updatedGameId != null && updatedGameId != -1) {
                    gameAdapter.notifyDataSetChanged()
                }
            }
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.add_game_to_list -> {
                val intent = Intent(this, CreateOrEditGame::class.java)
                startActivity(intent)
            }
            R.id.return_button_game_selector -> finish()
        }
    }
}

