package com.example.primeiroapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.death_counter.GameSelector
import com.example.death_counter.com.death_counter.Game
import com.example.death_counter.com.death_counter.data.database.GameDatabaseHelper

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var gameDatabaseHelper: GameDatabaseHelper
    private lateinit var gamesFromDb: List<Game>

    private val changeGameBtn: Button by lazy { findViewById(R.id.change_game) }
    private val returnBtn: Button by lazy { findViewById(R.id.return_button_main_activity) }
    private val nome: TextView by lazy { findViewById(R.id.death_counter) }
    private val addDeath: Button by lazy { findViewById(R.id.add_death) }
    private val subDeath: Button by lazy { findViewById(R.id.undo_deaths) }
    private val restartDeath: Button by lazy { findViewById(R.id.restart_deaths) }
    private val actualGame: TextView by lazy { findViewById(R.id.actual_game) }
    private lateinit var adViewContainer: FrameLayout

    private var deathNumber: Int = 0
    private var actualGameId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // Configurações de janela
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.back_to_previous)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Inicialize o banco
        gameDatabaseHelper = GameDatabaseHelper(this)
        gamesFromDb = gameDatabaseHelper.getAllGames()

        // Configura elementos da UI
        adViewContainer = findViewById(R.id.admob_placeholder)
        prepareAdPlaceholder()
        setGame()

        // Configura cliques
        changeGameBtn.setOnClickListener(this)
        addDeath.setOnClickListener(this)
        subDeath.setOnClickListener(this)
        restartDeath.setOnClickListener(this)
        returnBtn.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.add_death -> addToCounter()
            R.id.undo_deaths -> subToCounter()
            R.id.restart_deaths -> restartCounter()
            R.id.change_game -> returnToGamesList()
            R.id.return_button_main_activity -> finishWithResult()
        }
    }

    private fun setGame() {
        val gameName = intent.getStringExtra("gameName")
        val deathCounter = intent.getIntExtra("gameDeaths", -1)
        val gameIdFromTable = intent.getIntExtra("gameId", -1)

        // Verifica se há um favorito no banco
        val favoriteGame = gamesFromDb.find { it.favorite }
        if (gameName != null && deathCounter != -1 && gameIdFromTable != -1) {
            actualGameId = gameIdFromTable
            actualGame.text = gameName
            deathNumber = deathCounter
        } else if (favoriteGame != null) {
            actualGameId = favoriteGame.id
            actualGame.text = favoriteGame.nome
            deathNumber = favoriteGame.deaths
        } else {
            // Caso padrão
            actualGameId = gamesFromDb[0].id
            actualGame.text = gamesFromDb[0].nome
            deathNumber = gamesFromDb[0].deaths
        }
        nome.text = deathNumber.toString()
    }

    private fun prepareAdPlaceholder() {
        adViewContainer.setBackgroundColor(resources.getColor(android.R.color.holo_red_light))
    }

    private fun addToCounter() {
        deathNumber += 1
        nome.text = deathNumber.toString()
        gameDatabaseHelper.updateDeaths(actualGameId, deathNumber)
    }

    private fun subToCounter() {
        if (deathNumber > 0) {
            deathNumber -= 1
            nome.text = deathNumber.toString()
            gameDatabaseHelper.updateDeaths(actualGameId, deathNumber)
        }
    }

    private fun restartCounter() {
        deathNumber = 0
        nome.text = deathNumber.toString()
        gameDatabaseHelper.updateDeaths(actualGameId, deathNumber)
    }

    private fun returnToGamesList() {
        val intent = Intent(this, GameSelector::class.java)
        startActivity(intent)
    }

    private fun finishWithResult() {
        val resultIntent = Intent().apply {
            putExtra("updatedGameId", actualGameId)
        }
        setResult(RESULT_OK, resultIntent)
        finish()
    }
}
