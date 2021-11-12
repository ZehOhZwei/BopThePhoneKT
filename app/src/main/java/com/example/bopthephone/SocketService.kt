package com.example.bopthephone

import android.app.Service
import android.content.Intent
import android.os.IBinder

class SocketService : Service() {

    var connected: Boolean = false

    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }

    override fun onUnbind(intent: Intent?): Boolean {
        return super.onUnbind(intent)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }
}