package com.example.algorithms.viewmodels.menu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.algorithms.data.AlgorithmItem
import com.example.algorithms.data.repositories.FavoritesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FavoritesViewModel(
    private val favoritesRepository: FavoritesRepository
) : ViewModel() {
    private val _favoriteAlgorithms = MutableStateFlow<List<AlgorithmItem>>(emptyList())
    val favoriteAlgorithms: StateFlow<List<AlgorithmItem>> = _favoriteAlgorithms.asStateFlow()

    private val _favoriteIds = MutableStateFlow<Set<String>>(emptySet())
    val favoriteIds: StateFlow<Set<String>> = _favoriteIds.asStateFlow()

    init {
        viewModelScope.launch {
            favoritesRepository.favorites.collect { favorites ->
                _favoriteIds.value = favorites
                updateFavoriteAlgorithms()
            }
        }
    }

    fun toggleFavorite(algorithm: AlgorithmItem) {
        viewModelScope.launch {
            val isFavorite = favoritesRepository.toggleFavorite(algorithm.title)
            if (isFavorite) {
                _favoriteAlgorithms.update { currentList ->
                    currentList + algorithm
                }
            } else {
                _favoriteAlgorithms.update { currentList ->
                    currentList.filter { it.title != algorithm.title }
                }
            }
        }
    }

    fun isFavorite(algorithmTitle: String): Boolean {
        return algorithmTitle in _favoriteIds.value
    }

    private fun updateFavoriteAlgorithms() {
        val allAlgorithms = listOf(
            AlgorithmItem("Факториал", isCompleted = false, starRating = 0),
            AlgorithmItem("Числа Фибоначчи", isCompleted = false, starRating = 0),
            AlgorithmItem("Бинарный поиск", isCompleted = false, starRating = 0),
            AlgorithmItem("Линейный поиск", isCompleted = false, starRating = 0),
            AlgorithmItem("Сортировка пузырьком", isCompleted = false, starRating = 0),
            AlgorithmItem("Сортировка вставками", isCompleted = false, starRating = 0),
            AlgorithmItem("Сортировка выбором", isCompleted = false, starRating = 0),
            AlgorithmItem("Быстрая сортировка", isCompleted = false, starRating = 0),
            AlgorithmItem("Обход в ширину", isCompleted = false, starRating = 0),
            AlgorithmItem("Обход в глубину", isCompleted = false, starRating = 0),
            AlgorithmItem("Алгоритм Дейкстры", isCompleted = false, starRating = 0),
            AlgorithmItem("Алгоритм Крускала", isCompleted = false, starRating = 0)
        )
        
        _favoriteAlgorithms.value = allAlgorithms.filter { it.title in _favoriteIds.value }
    }
} 