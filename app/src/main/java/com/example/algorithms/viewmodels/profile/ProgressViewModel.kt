package com.example.algorithms.viewmodels.profile

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.algorithms.di.main_api.AlgorithmProgress
import com.example.algorithms.di.main_api.ProgressRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProgressViewModel(
    private val progressRepository: ProgressRepository
) : ViewModel() {

    private val _progress = MutableStateFlow<List<AlgorithmProgress>>(emptyList())
    val progress: StateFlow<List<AlgorithmProgress>> = _progress.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    fun loadProgress(token: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            Log.d("ProgressViewModel", "Loading progress with token: ${token.take(10)}...")
            try {
                val result = progressRepository.getAlgorithmProgress(token)
                result.onSuccess { response -> 
                    Log.d("ProgressViewModel", "Successfully loaded progress: ${response.progress}")
                    _progress.value = response.progress 
                }.onFailure { e -> 
                    Log.e("ProgressViewModel", "Error loading progress: ${e.message}")
                    _error.value = e.message ?: "Ошибка загрузки прогресса" 
                }
            } catch (e: Exception) {
                Log.e("ProgressViewModel", "Exception during progress loading: ${e.message}")
                _error.value = e.message ?: "Ошибка загрузки прогресса"
            }
            _isLoading.value = false
        }
    }

    fun updateProgress(token: String, algorithm: String, completed: Boolean) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            Log.d("ProgressViewModel", "Updating progress for $algorithm: $completed")
            progressRepository.updateAlgorithmProgress(token, algorithm, completed)
                .onSuccess { updated ->
                    Log.d("ProgressViewModel", "Progress updated successfully for $algorithm")
                    _progress.update { list ->
                        list.map {
                            if (it.algorithm == updated.algorithm) updated else it
                        }
                    }
                }
                .onFailure { e -> 
                    Log.e("ProgressViewModel", "Error updating progress for $algorithm: ${e.message}")
                    _error.value = e.message ?: "Ошибка обновления прогресса" 
                }
            _isLoading.value = false
        }
    }

    fun resetProgress(token: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            progressRepository.resetAlgorithmProgress(token)
                .onSuccess { response -> _progress.value = response.progress }
                .onFailure { e -> _error.value = e.message ?: "Ошибка сброса прогресса" }
            _isLoading.value = false
        }
    }
}
