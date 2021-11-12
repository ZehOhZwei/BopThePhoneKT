package com.example.bopthephone

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button

class LobbyActivity : AppCompatActivity() {

    private var ready : Boolean = false
    private var players : Int = 0
    private lateinit var readyButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lobby)

        readyButton = findViewById(R.id.ReadyButton)
    }

    fun readyClick(view: View) {
        if (ready){
            readyButton.setBackgroundColor(Color.RED)
            readyButton.text = "Not Ready"
            ready = !ready
        }
        else{
            readyButton.setBackgroundColor(Color.BLUE)
            readyButton.text = "Ready"
            ready = !ready
        }
    }
}