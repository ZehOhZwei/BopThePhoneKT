package com.example.bopthephone

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.runBlocking

class MainActivity : AppCompatActivity() {

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onStart() {
        super.onStart()
        Intent(this, SocketService::class.java).also { intent ->
            startService(intent)
            bindService(intent, mConnection, Context.BIND_AUTO_CREATE)
        }
    }

    override fun onPause() {
        super.onPause()
        unbindService(mConnection)
        bound = false

    }

    fun singleplayerClick(view: View) {
        val showGameView: Intent = Intent(this, GameActivity::class.java)
        startActivity(showGameView)
    }

    fun multiplayerClick(view: View) {
        val intent = Intent(this, PopUpActivity::class.java)
        startActivity(intent)
        //val showGameView: Intent = Intent(this, LobbyActivity()::class.java)
        //startActivity(showGameView)
    }

    fun scoreboardClick(view: View) {
        val showScoreboardView: Intent = Intent(this, ScoreboardActivity::class.java)
        startActivity(showScoreboardView)
    }

    fun testClick(view: View) {

            socketService.sendMessage("abc")

    }

}