package com.example.bopthephone

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.graphics.Color
import android.os.Bundle
import android.os.IBinder
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class LobbyActivity : AppCompatActivity() {

    private var ready : Boolean = false
    private var players : Int = 0
    private lateinit var readyButton: Button
    private lateinit var lobbyCode: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lobby)

        readyButton = findViewById(R.id.ReadyButton)
        lobbyCode = intent.getStringExtra("LobbyCode")!!
    }

    private fun onMessage(response: CallbackResponse){
        when(response.data.type){
            "start-game"->startGame()

            "player-joined"->players++
        }
    }

    fun readyClick(view: View) {
        if (ready){
            readyButton.setBackgroundColor(Color.RED)
            readyButton.text = "Not Ready"
            ready = !ready
            //socketService.sendMessage(Json.encodeToString(Message("unready", lobbyCode)))
        }
        else{
            readyButton.setBackgroundColor(Color.GREEN)
            readyButton.text = "Ready"
            ready = !ready
            //socketService.sendMessage(Json.encodeToString(Message("ready", lobbyCode)))
        }
    }

    fun startGame(){
        val toMultiplayerActivity: Intent = Intent(this, MultiplayerActivity()::class.java)
        val b: Bundle = Bundle()
        b.putString("LobbyCode", lobbyCode)
        if(b != null){
            toMultiplayerActivity.putExtras(b)
            startActivity(toMultiplayerActivity)
        }
    }
}
