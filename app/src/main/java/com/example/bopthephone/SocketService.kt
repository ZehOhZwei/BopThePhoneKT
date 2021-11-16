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
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.http.cio.websocket.*
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.UNLIMITED
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.util.*

/**
 * Service der die Kommunikation zwischen Client und Server verwaltet und Nachrichten senden
 * und empfangen kann.
 */
class SocketService : Service() {

    private val binders: MutableSet<SocketCallback> = mutableSetOf()
    private val messageChannel = Channel<String>(UNLIMITED)
    private val mainThreadHandler: Handler = Handler.createAsync(Looper.getMainLooper())
    private val job = SupervisorJob()

    /**
     * Coroutine Scope für das asynschrone Verwalten von Nachrichten.
     */
    private val socketScope = CoroutineScope(Dispatchers.IO)

    private var connected: Boolean = false

    lateinit var session: DefaultClientWebSocketSession

    private val client = HttpClient(OkHttp) {
        install(WebSockets)
    }

    private val binder = SocketBinder()


    /**
     * Fügt einen Observer hinzu, der bei einer eingehenden Nachricht,
     * benachrichtigt werden soll.
     */
    fun registerCallback(callback: SocketCallback) {
        binders.add(callback)
    }


    /**
     * Entfernt einen Beobachter.
     */
    fun deregisterCallback(callback: SocketCallback) {
        binders.remove(callback)
    }

    /**
     * Schreibt eine Nachricht in den [messageChannel]. Die Nachricht wird in der nächsten
     * Iteration von [outputMessages] an den Server gesendet.
     */
    fun sendMessage(message: String) {
        socketScope.launch {
        messageChannel.send(message)
        }
    }

    /**
     * Benachrichtigt alle registrierten Beobachter über die empfangene Nachricht.
     */
    private fun notify(response: CallbackResponse) {
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
        socketScope.launch {
            client.webSocket(
                method = HttpMethod.Get,
                host = "",
                port = 8080,
                path = "/chat"
            ) {
                session = this

                var output = launch { outputMessages() }
                var input = launch { inputMessages() }

                output.join()
                input.cancelAndJoin()
            }
        }
        return START_STICKY
    }

    /**
     * Coroutine Handler für ausgehende Nachrichtnen.
     */
    private suspend fun DefaultClientWebSocketSession.inputMessages() {
        for (message in messageChannel) {
            println("sending: $message")
            try {
                send(message)
            } catch (e: Exception) {
                println("Error while sending: " + e.localizedMessage)
                return
            }
        }
    }

    /**
     * Coroutine Handler für eingehende Nachrichten.
     */
    private suspend fun DefaultClientWebSocketSession.outputMessages() {
        try {
            for (message in incoming) {
                message as? Frame.Text ?: continue
                val content = message.readText()
                println(content)
                notify(CallbackResponse(Json.decodeFromString(content)))
            }
        } catch (e: Exception) {
            println("Error while receiving: " + e.localizedMessage)
        }
    }

    inner class SocketBinder : Binder() {
        fun getService(): SocketService = this@SocketService
    }
}