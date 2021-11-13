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



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onPause() {
        super.onPause()

    }

    override fun onResume() {
        super.onResume()
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

    }

}