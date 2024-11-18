package com.example.death_counter

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.death_counter.com.death_counter.Game
import com.example.death_counter.com.death_counter.GameInputDTO
import com.example.death_counter.com.death_counter.data.database.GameDatabaseHelper
import com.example.primeiroapp.R
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class CreateOrEditGame : AppCompatActivity(), View.OnClickListener {

    private val returnButton: Button by lazy { findViewById(R.id.back_screen) }
    private lateinit var gameNameLayout: TextInputLayout
    private lateinit var deathsLayout: TextInputLayout
    private lateinit var gameNameEditText: TextInputEditText
    private lateinit var deathsEditText: TextInputEditText
    private lateinit var saveButton: Button
    private lateinit var deleteButton: Button
    private lateinit var gameDatabaseHelper: GameDatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_create_or_edit_game)

        gameNameLayout = findViewById(R.id.game_name_input)
        deathsLayout = findViewById(R.id.deaths_input)
        gameNameEditText = gameNameLayout.editText as TextInputEditText
        deathsEditText = deathsLayout.editText as TextInputEditText
        saveButton = findViewById(R.id.button2)
        deleteButton = findViewById(R.id.button3)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.back_to_previous)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        returnButton.setOnClickListener(this)
        saveButton.setOnClickListener { saveGame() }
        deleteButton.setOnClickListener { deleteGame() }
        gameDatabaseHelper = GameDatabaseHelper(this)

        setNameAndDeaths()
    }
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.back_screen -> finish()
        }
    }

    private fun setNameAndDeaths() {
        val actualGameName = intent.getStringExtra("gameName") ?: ""
        val deathsOnGame = intent.getIntExtra("gameDeaths", 0)

        gameNameEditText.setText(actualGameName)
        deathsEditText.setText(deathsOnGame.toString())
    }

    private fun saveGame() {

        val gameName = gameNameEditText.text.toString()
        val deaths = deathsEditText.text.toString().toIntOrNull() ?: 0
        val it = Intent(baseContext, GameSelector::class.java)
        val actualGameId = intent.getIntExtra("gameId", -1)
        if (!hasItemToEdit()) {
            createGame(gameName, deaths)
            startActivity(it)
        }
        if (hasItemToEdit()) {
            val gameToEdit = gameDatabaseHelper.getById(actualGameId)
            if (gameToEdit != null) {
                editGame(gameToEdit, gameName, deaths)
                startActivity(it)
            }
        }
    }
    private fun editGame (game: Game, newGameName: String, newDeaths: Int) {
        game.nome = newGameName
        game.deaths = newDeaths
        gameDatabaseHelper.editGame(game)
    }
    private fun createGame(gameName: String, deaths:Int) {
        val toCreate = GameInputDTO(gameName, deaths)
        gameDatabaseHelper.createGame(toCreate)
    }
    private fun hasItemToEdit() : Boolean {
        val actualGameName = intent.getStringExtra("gameName")
        val deathsOnGame = intent.getIntExtra("gameDeaths", -1)
        return actualGameName != null && deathsOnGame != -1
        //se retornar FALSE é para criar, se TRUE é para editar
    }
    private fun deleteGame() {
        gameNameEditText.text?.clear()
        deathsEditText.text?.clear()
    }
}
