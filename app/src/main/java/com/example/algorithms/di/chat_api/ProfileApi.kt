package com.example.algorithms.di.chat_api

import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Query
import retrofit2.http.Body
import retrofit2.http.Header

data class UserProfile(
    val id: Int,
    val username: String,
    val email: String,
    val full_name: String?,
    val profile_picture: String?
)

interface ProfileApi {
    @GET("/profile/")
    suspend fun getProfile(
        @Header("Authorization") token: String
    ): UserResponse

    @PUT("/profile/")
    suspend fun updateProfile(
        @Header("Authorization") token: String,
        @Body profile: UserResponse
    ): UserResponse
}
