package com.example.bopthephone

import android.app.AlertDialog
import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.VerifiedInputEvent
import android.view.View
import android.content.DialogInterface

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

    fun scoreboardClick(view: View) {
        val showScoreboardView: Intent = Intent(this, ScoreboardActivity::class.java)
        startActivity(showScoreboardView)
    }

    fun testClick(view: View) {

        val tapItPlayer: MediaPlayer = MediaPlayer.create(this, R.raw.tap_it)
        tapItPlayer.start()

    }

    override fun onBackPressed() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle(R.string.app_name);
        builder.setMessage("Do you want to exit?")
            .setCancelable(false)
            .setPositiveButton("Yes") { dialog, id -> finish() }
            .setNegativeButton("No") { dialog, id -> dialog.cancel() }

        val alert = builder.create()
        alert.show();
    }


}

