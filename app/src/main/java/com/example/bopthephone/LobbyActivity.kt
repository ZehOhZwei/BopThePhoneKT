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

class LobbyActivity : AppCompatActivity() {

    private var ready : Boolean = false
    private var players : Int = 0
    private lateinit var readyButton: Button
    private lateinit var lobbyCode: String

    private lateinit var socketService: SocketService
    private var bound: Boolean = false

    private val mConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            println("super123")
            val binder = service as SocketService.SocketBinder
            socketService = binder.getService()
            bound = true
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            println("nichtsuper123")
            bound = false
        }
    }

    override fun onResume() {
        super.onResume()
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE)

    }

    override fun onPause() {
        super.onPause()
        unbindService(mConnection)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lobby)

        readyButton = findViewById(R.id.ReadyButton)
        lobbyCode = intent.getStringExtra("LobbyCode")!!
    }

    fun onMessage(response: CallbackResponse<Message<String>>){
        when(response.data.type){
            "start-game"->startGame()
        }
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
