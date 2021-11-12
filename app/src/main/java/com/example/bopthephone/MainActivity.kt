package com.example.bopthephone

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.VerifiedInputEvent
import android.view.View
import android.widget.PopupWindow

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
        val intent = Intent(this, PopUpActivity::class.java)
        startActivity(intent)
       //val showGameView: Intent = Intent(this, LobbyActivity()::class.java)
       //startActivity(showGameView)
    }

    fun testClick(view: View) {

    }
}