package com.example.algorithms.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MenuViewModel : ViewModel() {

    // Состояние раскрытых категорий
    private val _expandedCategories = MutableStateFlow(mapOf<String, Boolean>())
    val expandedCategories: StateFlow<Map<String, Boolean>> = _expandedCategories

    // Обновление состояния раскрытия категории
    fun toggleCategory(categoryTitle: String) {
        viewModelScope.launch {
            // Получаем текущее состояние категорий или используем пустую мапу
            val currentStates = _expandedCategories.value.toMutableMap()

            // Устанавливаем значение по умолчанию (false), если ключ отсутствует
            currentStates[categoryTitle] = !(currentStates[categoryTitle] ?: false)

            // Обновляем состояние
            _expandedCategories.value = currentStates
        }
    }

    // Состояние кликов на подпункты
    private val _clickedItems = MutableStateFlow(emptyMap<String, Boolean>())
    val clickedItems: StateFlow<Map<String, Boolean>> = _clickedItems

    // Обновление состояния клика на подпункт
    fun clickItem(itemTitle: String) {
        viewModelScope.launch {
            val currentStates = _clickedItems.value.toMutableMap()
            currentStates[itemTitle] = true
            _clickedItems.value = currentStates

            // Сброс состояния после короткой задержки
            delay(700) // Длительность анимации
            currentStates[itemTitle] = false
            _clickedItems.value = currentStates
        }
    }
}