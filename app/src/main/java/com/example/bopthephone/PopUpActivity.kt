package com.example.bopthephone

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

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
        if (b != null) {
            toLobbyActivity.putExtras(b)
                lobbyCodeEditText.text.toString()
            startActivity(toLobbyActivity)
        }
    }
}
