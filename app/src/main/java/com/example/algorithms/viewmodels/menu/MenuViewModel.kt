package com.example.algorithms.viewmodels.menu

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.algorithms.di.main_api.AlgorithmProgress
import com.example.algorithms.viewmodels.profile.ProgressViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class MenuViewModel : ViewModel(), KoinComponent {
    private val progressViewModel: ProgressViewModel by inject()
    
    private val _expandedCategories = MutableStateFlow(
        mapOf(
            "Сортировка" to true,
            "Математика" to true,
            "Поиск" to true,
            "Графы" to true
        )
    )
    val expandedCategories: StateFlow<Map<String, Boolean>> = _expandedCategories.asStateFlow()

    private val _algorithmProgress = MutableStateFlow<Map<String, Boolean>>(emptyMap())
    val algorithmProgress: StateFlow<Map<String, Boolean>> = _algorithmProgress.asStateFlow()

    init {
        viewModelScope.launch {
            progressViewModel.progress.collect { progressList ->
                val progressMap = progressList.associate { progress ->
                    progress.algorithm to progress.completed
                }
                Log.d("MenuViewModel", "Progress list: $progressList")
                Log.d("MenuViewModel", "Progress map: $progressMap")
                Log.d("MenuViewModel", "Algorithm_1 status: ${progressMap["algorithm_1"]}")
                Log.d("MenuViewModel", "Algorithm_2 status: ${progressMap["algorithm_2"]}")
                _algorithmProgress.value = progressMap
            }
        }
    }

    fun toggleCategory(categoryTitle: String) {
        viewModelScope.launch {
            val currentStates = _expandedCategories.value.toMutableMap()
            currentStates[categoryTitle] = !(currentStates[categoryTitle] ?: false)
            _expandedCategories.value = currentStates
        }
    }

    val _clickedItems = MutableStateFlow(emptyMap<String, Boolean>())

    fun clickItem(itemTitle: String) {
        viewModelScope.launch {
            val currentStates = _clickedItems.value.toMutableMap()
            currentStates[itemTitle] = true
            _clickedItems.value = currentStates

            delay(700)
            currentStates[itemTitle] = false
            _clickedItems.value = currentStates
        }
    }
}