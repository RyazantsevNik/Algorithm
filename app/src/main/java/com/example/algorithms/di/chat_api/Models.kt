package com.example.algorithms.di.chat_api

import com.google.gson.annotations.SerializedName

data class UserResponse(
    val id: Int,
    val username: String,
    val email: String,
    @SerializedName("full_name") val fullName: String? = null,
    @SerializedName("profile_picture") val profilePicture: String? = null
)

data class AuthResponse(
    val user: UserResponse,
    @SerializedName("access_token") val accessToken: String,
    @SerializedName("token_type") val tokenType: String
)

data class RegisterRequest(
    val username: String,
    val email: String,
    val password: String
)

data class LoginRequest(
    val username: String,
    val password: String
)
