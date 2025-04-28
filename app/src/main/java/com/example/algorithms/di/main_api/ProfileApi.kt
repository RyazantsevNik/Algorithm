package com.example.algorithms.di.main_api

import com.google.gson.annotations.SerializedName
import okhttp3.MultipartBody
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

data class ProfilePhotoResponse(
    val message: String,
    @SerializedName("profile_picture") val profilePicture: String?
)

data class UserUpdateRequest(
    @SerializedName("username")
    val username: String? = null,
    @SerializedName("email")
    val email: String? = null,
    @SerializedName("current_password")
    val currentPassword: String? = null,
    @SerializedName("new_password")
    val newPassword: String? = null
)

interface ProfileApi {
    @GET("/profile")
    suspend fun getProfile(
        @Header("Authorization") token: String
    ): UserResponse

    @Multipart
    @POST("/profile/upload-photo")
    suspend fun uploadProfilePhoto(
        @Header("Authorization") token: String,
        @Part photo: MultipartBody.Part
    ): ProfilePhotoResponse

    @PUT("/profile")
    suspend fun updateProfile(
        @Header("Authorization") token: String,
        @Body updateRequest: UserUpdateRequest
    ): UserResponse

    @DELETE("/profile/delete-photo")
    suspend fun deleteProfilePhoto(
        @Header("Authorization") token: String
    ): ProfilePhotoResponse
}
