package com.example.algorithms.di.main_api

import android.util.Log

class ProgressRepository(private val progressApi: ProgressApi) {

    suspend fun getAlgorithmProgress(token: String): Result<AlgorithmProgressList> {
        return try {
            Log.d("ProgressRepository", "Sending GET request for progress with token: ${token.take(10)}...")
            val response = progressApi.getAlgorithmProgress("Bearer $token")
            Log.d("ProgressRepository", "Received response: $response")
            if (response.progress.isEmpty()) {
                Log.w("ProgressRepository", "Received empty progress list")
            }
            Result.success(response)
        } catch (e: Exception) {
            Log.e("ProgressRepository", "Error getting progress: ${e.message}")
            Log.e("ProgressRepository", "Stack trace: ${e.stackTraceToString()}")
            Result.failure(e)
        }
    }

    suspend fun updateAlgorithmProgress(
        token: String,
        algorithm: String,
        completed: Boolean
    ): Result<AlgorithmProgress> {
        return try {
            Log.d("ProgressRepository", "Sending PUT request for $algorithm with token: ${token.take(10)}...")
            val update = AlgorithmProgressUpdate(algorithm, completed)
            val response = progressApi.updateAlgorithmProgress("Bearer $token", update)
            Log.d("ProgressRepository", "Received response: $response")
            Result.success(response)
        } catch (e: Exception) {
            Log.e("ProgressRepository", "Error updating progress: ${e.message}")
            Result.failure(e)
        }
    }

    suspend fun resetAlgorithmProgress(token: String): Result<AlgorithmProgressList> {
        return try {
            Log.d("ProgressRepository", "Sending DELETE request with token: ${token.take(10)}...")
            val response = progressApi.resetAlgorithmProgress("Bearer $token")
            Log.d("ProgressRepository", "Received response: $response")
            Result.success(response)
        } catch (e: Exception) {
            Log.e("ProgressRepository", "Error resetting progress: ${e.message}")
            Result.failure(e)
        }
    }
}