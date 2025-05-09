package com.example.algorithms.data.repositories

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class FavoritesRepository(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    private val _favorites = MutableStateFlow<Set<String>>(emptySet())
    val favorites: Flow<Set<String>> = _favorites.asStateFlow()

    init {
        loadFavorites()
    }

    private fun loadFavorites() {
        val savedFavorites = prefs.getStringSet(KEY_FAVORITES, emptySet()) ?: emptySet()
        _favorites.value = savedFavorites
    }

    fun toggleFavorite(algorithmId: String): Boolean {
        val currentFavorites = _favorites.value.toMutableSet()
        val isFavorite = algorithmId in currentFavorites
        
        if (isFavorite) {
            currentFavorites.remove(algorithmId)
        } else {
            currentFavorites.add(algorithmId)
        }
        
        _favorites.value = currentFavorites
        prefs.edit().putStringSet(KEY_FAVORITES, currentFavorites).apply()
        
        return !isFavorite
    }

    companion object {
        private const val PREFS_NAME = "favorites_prefs"
        private const val KEY_FAVORITES = "favorites"
    }
} 