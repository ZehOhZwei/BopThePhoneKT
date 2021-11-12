package com.example.bopthephone

import kotlinx.serialization.Serializable

@Serializable
data class Message<T>(
    val type: String,
    val content: T
)