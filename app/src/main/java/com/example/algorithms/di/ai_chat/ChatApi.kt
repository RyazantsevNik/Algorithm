package com.example.algorithms.di.ai_chat

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface ChatApi {
    @GET("/chat/history")
    suspend fun getChatHistory(
        @Header("Authorization") token: String
    ): Response<List<MessageDto>>


    @POST("/chat")
    suspend fun sendMessage(
        @Header("Authorization") token: String,
        @Body request: ChatRequest
    ): Response<ChatResponse>
}
