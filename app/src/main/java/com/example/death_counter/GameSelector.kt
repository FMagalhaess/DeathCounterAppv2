package com.example.death_counter

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.death_counter.com.death_counter.GameAdapter
import com.example.death_counter.com.death_counter.common.GamesDatabase
import com.example.primeiroapp.MainActivity
import com.example.primeiroapp.R

class GameSelector : AppCompatActivity(), View.OnClickListener {

    private val gamesList: RecyclerView by lazy { findViewById(R.id.selectGame) }
    private val addGameToList: Button by lazy { findViewById(R.id.add_game_to_list) }
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

        gamesList.setPadding(0, 0, 0, 320)
        gamesList.clipToPadding = false
        val games = GamesDatabase.getAll()
        gamesList.layoutManager = LinearLayoutManager(baseContext)
        gamesList.adapter = GameAdapter(games, onGameClick = { game ->
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("gameName", game.nome)
            intent.putExtra("gameDeaths", game.deaths)
            startActivity(intent)
        }, onEditClick = { game ->
            val intent = Intent(this, CreateOrEditGame::class.java)
            intent.putExtra("gameName", game.nome)
            intent.putExtra("gameDeaths", game.deaths)
            startActivity(intent)
        })

    }

    override fun onClick(v: View?) {
        when(v?.id)
        {
            R.id.add_game_to_list -> {
                val intent = Intent(this, CreateOrEditGame::class.java)
                startActivity(intent)
            }
        }
    }
}