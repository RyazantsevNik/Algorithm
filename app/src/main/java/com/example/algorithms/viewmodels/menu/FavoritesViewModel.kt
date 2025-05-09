package com.example.algorithms.viewmodels.menu

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.algorithms.data.AlgorithmItem
import com.example.algorithms.data.algorithmCategories
import com.example.algorithms.data.repositories.FavoritesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FavoritesViewModel(context: Context) : ViewModel() {
    private val repository = FavoritesRepository(context)
    private val _favoriteAlgorithms = MutableStateFlow<List<AlgorithmItem>>(emptyList())
    val favoriteAlgorithms: StateFlow<List<AlgorithmItem>> = _favoriteAlgorithms.asStateFlow()

    fun toggleFavorite(algorithm: AlgorithmItem) {
        viewModelScope.launch {
            repository.toggleFavorite(algorithm.title)
            updateFavorites()
        }
    }

    fun isFavorite(algorithmTitle: String): Boolean {
        return _favoriteAlgorithms.value.any { it.title == algorithmTitle }
    }

    private fun updateFavorites() {
        viewModelScope.launch {
            repository.favorites.collect { favoriteTitles ->
                _favoriteAlgorithms.value = algorithmCategories
                    .flatMap { it.items }
                    .filter { it.title in favoriteTitles }
            }
        }
    }

    init {
        updateFavorites()
    }
} 