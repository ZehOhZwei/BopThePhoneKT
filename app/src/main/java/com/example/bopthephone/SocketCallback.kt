package com.example.bopthephone

interface SocketCallback<T> {
    fun onComplete(response: CallbackResponse<T>)
}

data class CallbackResponse<T> (val data: T)