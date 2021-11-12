package com.example.bopthephone

import android.app.AlertDialog
import android.content.*
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.view.View

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
        //val intent = Intent(this, PopUpActivity::class.java)
        //startActivity(intent)
        val showGameView: Intent = Intent(this, LobbyActivity()::class.java)
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

