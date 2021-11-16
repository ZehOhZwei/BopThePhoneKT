package com.example.bopthephone

/**
 * Interface, das dazu dient bei Kommunikation mit dem Serber einen Callback
 * euszuführen.
 */
fun interface SocketCallback {
    fun onComplete(response: CallbackResponse)
}

/**
 * Datenklasse die eine [Message] enthält, die vom Callback verarbeitet werden kann.
 */
data class CallbackResponse (val data: Message)