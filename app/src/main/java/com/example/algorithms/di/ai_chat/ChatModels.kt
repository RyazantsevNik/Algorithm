package com.example.algorithms.di.ai_chat

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ChatRequest(
    @SerializedName("message") val message: String,
    @SerializedName("chat_history") val chatHistory: List<Message> = emptyList()
)

data class ChatResponse(
    @SerializedName("response") val response: String,
    @SerializedName("chat_history") val chatHistory: List<Message>
)

data class MessageDto(
    @SerializedName("role") val role: String,
    @SerializedName("content") val content: String,
    @SerializedName("id") val id: Int,
    @SerializedName("timestamp") val timestamp: String
)

data class ChatHistoryResponse(
    @SerializedName("chat_history") val chatHistory: List<Message>
)


data class Message(
    @SerializedName("role") val role: String,  // "user" или "assistant"
    @SerializedName("content") val content: String
): Serializable
