package com.example.bopthephone

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
    fun gameClick(view: View) {
        val showGameView: Intent = Intent(this, GameActivity::class.java)
        startActivity(showGameView)
    }
}