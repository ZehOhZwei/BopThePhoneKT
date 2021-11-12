package com.example.bopthephone

import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.VerifiedInputEvent
import android.view.View

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
    fun singleplayerClick(view: View) {
        val showGameView: Intent = Intent(this, GameActivity::class.java)
        startActivity(showGameView)
    }

    fun multiplayerClick(view: View) {
        val showGameView: Intent = Intent(this, LobbyActivity::class.java)
        startActivity(showGameView)
    }

    fun testClick(view: View) {

        val tapItPlayer : MediaPlayer =  MediaPlayer.create(this, R.raw.tap_it)
        tapItPlayer.start()

    }
}