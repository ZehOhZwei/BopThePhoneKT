package com.example.bopthephone

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

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
            readyButton.setBackgroundColor(Color.GREEN)
            readyButton.text = "Ready"
            ready = !ready
        }
    }
}
