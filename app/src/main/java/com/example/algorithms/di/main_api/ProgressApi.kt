package com.example.algorithms.di.main_api

import com.google.gson.annotations.SerializedName
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Header
import retrofit2.http.PUT

data class AlgorithmProgress(
    val algorithm: String,
    val completed: Boolean,
    @SerializedName("user_id") val userId: Int
)

data class AlgorithmProgressList(
    val progress: List<AlgorithmProgress>
)

data class AlgorithmProgressUpdate(
    val algorithm: String,
    val completed: Boolean
)

interface ProgressApi {
    @GET("/algorithms/progress")
    suspend fun getAlgorithmProgress(
        @Header("Authorization") token: String
    ): AlgorithmProgressList

    @PUT("/algorithms/progress")
    suspend fun updateAlgorithmProgress(
        @Header("Authorization") token: String,
        @Body update: AlgorithmProgressUpdate
    ): AlgorithmProgress

    @DELETE("/algorithms/progress/reset")
    suspend fun resetAlgorithmProgress(
        @Header("Authorization") token: String
    ): AlgorithmProgressList
}