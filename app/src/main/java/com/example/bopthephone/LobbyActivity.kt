package com.example.bopthephone

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

class LobbyActivity : AppCompatActivity() {

    private var ready : Boolean = false
    private var playersNum : Int = 0
    private lateinit var readyButton: Button
    private lateinit var lobbyCodeText: TextView
    private lateinit var lobbyCode : String
    private val players = IntArray(4)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lobby)

        val bundle = intent.extras
        lobbyCode = bundle?.getString("LobbyCode","Lobby code") ?: ""
        readyButton = findViewById(R.id.ReadyButton)
        lobbyCodeText.text = lobbyCode
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