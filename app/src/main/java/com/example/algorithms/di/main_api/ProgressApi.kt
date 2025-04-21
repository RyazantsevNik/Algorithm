package com.example.algorithms.di.main_api

import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Body
import retrofit2.http.Header

data class ProgressRequest(val algorithmId: String)
data class ProgressResponse(val totalCompleted: Int)

interface ProgressApi {
    @POST("/progress")
    suspend fun updateProgress(@Header("Authorization") token: String, @Body request: ProgressRequest): ProgressResponse

    @GET("/progress")
    suspend fun getProgress(@Header("Authorization") token: String): ProgressResponse
}
