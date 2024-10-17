package com.example.death_counter

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.primeiroapp.R

class CreateOrEditGame : AppCompatActivity(), View.OnClickListener {
    private val returnButton: Button by lazy { findViewById(R.id.back_screen) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_create_or_edit_game)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.back_to_previous)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        returnButton.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.back_screen -> {finish()}
        }
    }
}