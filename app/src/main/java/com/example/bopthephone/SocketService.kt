package com.example.bopthephone

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.features.websocket.*
import io.ktor.http.*
import io.ktor.http.cio.websocket.*
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.UNLIMITED
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.*

class SocketService : Service() {

    private val binders: MutableSet<SocketCallback<String>> = mutableSetOf()
    private val messageChannel = Channel<String>(UNLIMITED)
    private val mainThreadHandler: Handler = Handler.createAsync(Looper.getMainLooper())

    private var connected: Boolean = false

    private val client = HttpClient(OkHttp) {
        install(WebSockets)
    }

    private val binder = SocketBinder()

    fun registerCallback(callback: SocketCallback<String>) {
        binders.add(callback)
    }

    fun deregisterCallback(callback: SocketCallback<String>) {
        binders.remove(callback)
    }

    suspend fun sendMessage(message: String) {
        messageChannel.send(message)
    }

    private fun notify(
        response: CallbackResponse<String>,
    ) {
        binders.forEach { it.onComplete(response) }

    }

    override fun onBind(intent: Intent): IBinder {
        return binder
    }

    override fun onUnbind(intent: Intent?): Boolean {
        return super.onUnbind(intent)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        println("onstartcommand")
        runBlocking {
            client.webSocket(
                method = HttpMethod.Get,
                host = "192.168.2.101",
                port = 4666,
                path = "/bop"
            ) {
                connected = true
                val input = launch { inputMessages() }
                val output = launch { outputMessages() }
                input.join()
                output.cancelAndJoin()
            }
            connected = false
            client.close()
        }

        return START_STICKY
    }

    private suspend fun DefaultClientWebSocketSession.inputMessages() {
        while (true) {
            val message = messageChannel.receive()
            if (message.equals("disconnect", true)) return
            try {
                send(message)
            } catch (e: Exception) {
                println("Error while sending: " + e.localizedMessage)
                return
            }
        }
    }

    private suspend fun DefaultClientWebSocketSession.outputMessages() {
        try {
            for (message in incoming) {
                message as? Frame.Text ?: continue
                val content = message.readText()
                println(content)
                notify(CallbackResponse(content))
            }
        } catch (e: Exception) {
            println("Error while receiving: " + e.localizedMessage)
        }
    }

    inner class SocketBinder : Binder() {
        fun getService(): SocketService = this@SocketService
    }
}