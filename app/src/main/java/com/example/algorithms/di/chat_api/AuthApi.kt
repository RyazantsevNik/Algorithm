package com.example.algorithms.di.chat_api

import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.GET
import retrofit2.http.Header

data class LoginResponse(
    val access_token: String,
    val token_type: String
)

interface AuthApi {
    @POST("/register/")
    suspend fun register(@Body request: RegisterRequest): AuthResponse

    @POST("/login/")
    suspend fun login(@Body request: LoginRequest): LoginResponse

    @GET("/logout")
    suspend fun logout(@Header("Authorization") token: String)
}