package com.example.bopthephone

fun interface SocketCallback {
    fun onComplete(response: CallbackResponse)
}

data class CallbackResponse (val data: Message)