package com.example.primeiroapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.death_counter.com.death_counter.common.GamesDatabase
import com.example.death_counter.game_selector

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private val returnButton: Button by lazy { findViewById(R.id.return_to_games) }
    private val nome:TextView by lazy { findViewById(R.id.death_counter) }
    private val addDeath:Button by lazy { findViewById(R.id.add_death) }
    private val subDeath: Button by lazy { findViewById(R.id.undo_deaths) }
    private val restartDeath: Button by lazy { findViewById(R.id.restart_deaths) }
    private var deathNumber: Int = 0
    private val actualGame:TextView by lazy { findViewById(R.id.actual_game) }
    private val games by lazy { GamesDatabase.getAll() }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.back_to_previous)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setGame()
        returnButton.setOnClickListener(this)
        addDeath.setOnClickListener(this)
        subDeath.setOnClickListener(this)
        restartDeath.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.add_death -> {addToCounter()}
            R.id.undo_deaths -> {subToCounter()}
            R.id.restart_deaths -> {restartCounter()}
            R.id.return_to_games -> {returnToGamesList()}
        }
    }
    private fun addToCounter() {
        deathNumber += 1
        nome.text = deathNumber.toString()
    }
    private fun subToCounter() {
        if(deathNumber > 0){
            deathNumber -= 1
            nome.text = deathNumber.toString()
        }
    }
    private fun restartCounter() {
        deathNumber = 0
        nome.text = deathNumber.toString()
    }
    private fun returnToGamesList() {
        val intent = Intent(this, game_selector::class.java)
        startActivity(intent)
    }
    private fun setGame() {
        val gameName = intent.getStringExtra("gameName")
        val deathCounter = intent.getIntExtra("gameDeaths", -1)

        if (gameName != null && deathCounter != -1)
        {
            actualGame.text = gameName
            deathNumber = deathCounter
        }
        else{
            actualGame.text = games[0].nome
            deathNumber = games[0].deaths
        }
        nome.text = deathNumber.toString()
    }
}