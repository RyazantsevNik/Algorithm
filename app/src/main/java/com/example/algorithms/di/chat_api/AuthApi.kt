package com.example.algorithms.di.chat_api

import com.google.gson.annotations.SerializedName
import retrofit2.http.Body
import retrofit2.http.POST

data class LoginResponse(
    @SerializedName("access_token")val accessToken: String,
    @SerializedName("token_type")val tokenType: String
)

interface AuthApi {
    @POST("/register/")
    suspend fun register(@Body request: RegisterRequest): AuthResponse

    @POST("/login/")
    suspend fun login(@Body request: LoginRequest): LoginResponse


}