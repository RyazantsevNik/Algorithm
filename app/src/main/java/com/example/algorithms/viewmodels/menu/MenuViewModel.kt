package com.example.algorithms.viewmodels.menu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MenuViewModel : ViewModel() {
    private val _expandedCategories = MutableStateFlow(
        mapOf(
            "Сортировка" to true,
            "Математика" to true,
            "Поиск" to true,
            "Графы" to true
        )
    )
    val expandedCategories: StateFlow<Map<String, Boolean>> = _expandedCategories.asStateFlow()


    fun toggleCategory(categoryTitle: String) {
        viewModelScope.launch {
            val currentStates = _expandedCategories.value.toMutableMap()
            currentStates[categoryTitle] = !(currentStates[categoryTitle] ?: false)
            _expandedCategories.value = currentStates
        }
    }


    private val _clickedItems = MutableStateFlow(emptyMap<String, Boolean>())



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