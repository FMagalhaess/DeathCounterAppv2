package com.example.death_counter

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_create_or_edit_game)

        // Configurando as views
        gameNameLayout = findViewById(R.id.game_name_input)
        deathsLayout = findViewById(R.id.deaths_input)
        gameNameEditText = gameNameLayout.editText as TextInputEditText
        deathsEditText = deathsLayout.editText as TextInputEditText
        saveButton = findViewById(R.id.button2)
        deleteButton = findViewById(R.id.button3)

        // Configurando a visualização de recuo do sistema
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.back_to_previous)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        returnButton.setOnClickListener(this)
        saveButton.setOnClickListener { saveGame() }
        deleteButton.setOnClickListener { deleteGame() }

        // Chama o método para preencher os campos com dados recebidos, se houver
        setNameAndDeaths()
    }

    // Método para lidar com cliques
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.back_screen -> finish()
        }
    }

    // Preencher o nome do jogo e mortes com base nos dados recebidos da Intent
    private fun setNameAndDeaths() {
        val actualNameGame = intent.getStringExtra("gameName") ?: ""
        val deathsOnGame = intent.getIntExtra("gameDeaths", 0)

        gameNameEditText.setText(actualNameGame)
        deathsEditText.setText(deathsOnGame.toString())
    }

    // Método para salvar o jogo (ainda não implementado)
    private fun saveGame() {
        // Implementar a lógica para salvar o jogo
        val gameName = gameNameEditText.text.toString()
        val deaths = deathsEditText.text.toString().toIntOrNull() ?: 0

        // Aqui você pode adicionar lógica para salvar os dados
    }

    // Método para deletar o jogo (ainda não implementado)
    private fun deleteGame() {
        // Implementar a lógica para deletar o jogo
        gameNameEditText.text?.clear()
        deathsEditText.text?.clear()
    }
}
