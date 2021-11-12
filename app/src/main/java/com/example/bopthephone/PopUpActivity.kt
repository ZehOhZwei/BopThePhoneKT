package com.example.bopthephone

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView

class PopUpActivity : AppCompatActivity() {

    private lateinit var lobbyCodeEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(0, 0)
        setContentView(R.layout.activity_pop_up)

        lobbyCodeEditText = findViewById(R.id.lobbyCodeEditText)
    }

    fun enterLobbyClick(view: View) {
        val toLobbyActivity: Intent = Intent(this, LobbyActivity()::class.java)
        val b: Bundle = Bundle()
        b.putString("LobbyCode", lobbyCodeEditText.text.toString())
        if(b != null){
            toLobbyActivity.putExtras(b)
            startActivity(toLobbyActivity)
        }
    }
}