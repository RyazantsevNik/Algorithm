package com.example.algorithms.viewmodels.step_search

import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class LinearSearchViewModel : ViewModel() {
    // входные данные и искомое
    val data = listOf(5, 3, 9, 1, 4, 7)
    val target = 9

    private val _stepIndex = mutableStateOf(0)
    val stepIndex: State<Int> = _stepIndex

    private val _currentIndex = mutableStateOf(-1)
    val currentIndex: State<Int> = _currentIndex

    private var foundIndex: Int? = null

    private val _explanation = mutableStateOf("")
    val explanation: State<String> = _explanation

    private var autoJob: Job? = null
    var isAuto by mutableStateOf(false)
        private set

    init {
        updateExplanation()
    }

    fun goToStart() {
        stopAuto()
        foundIndex = null
        _stepIndex.value = 0
        _currentIndex.value = -1
        updateExplanation()
    }

    fun goToEnd() {
        stopAuto()
            //Если сортировка не началась, ищет вручную
        if (foundIndex == null) {
            for ((index, value) in data.withIndex()) {
                if (value == target) {
                    foundIndex = index
                    break
                }
            }
        }

        _stepIndex.value = if (foundIndex != null) {
            foundIndex!! + 1 // Переход к шагу с "элемент найден"
        } else {
            data.size + 1 // Элемент не найден
        }

        _currentIndex.value = foundIndex ?: -1
        updateExplanation()
    }

    fun goToNextStep() {
        val totalSteps = if (foundIndex != null) foundIndex!! + 2 else data.size + 2
        if (_stepIndex.value >= totalSteps - 1) return

        _stepIndex.value++

        when (_stepIndex.value) {
            0 -> _currentIndex.value = -1
            in 1..data.size -> {
                val index = _stepIndex.value - 1
                _currentIndex.value = index
                if (data[index] == target && foundIndex == null) {
                    foundIndex = index
                }
            }
            foundIndex?.plus(1) -> _currentIndex.value = foundIndex!!
        }

        updateExplanation()
    }

    fun goToPreviousStep() {
        if (_stepIndex.value <= 0) return
        stopAuto()

        _stepIndex.value--

        if (_stepIndex.value == 0) {
            _currentIndex.value = -1
        } else {
            val idx = _stepIndex.value - 1
            _currentIndex.value = if (foundIndex != null && idx > foundIndex!!) {
                foundIndex!!
            } else {
                idx
            }
        }

        if (_stepIndex.value <= (foundIndex ?: Int.MAX_VALUE)) {
            foundIndex = null
        }

        updateExplanation()
    }

    private fun updateExplanation() {
        val step = _stepIndex.value
        _explanation.value = when (step) {
            0 -> "Наша цель — найти элемент $target. Мы будем последовательно сравнивать каждый элемент массива с $target."
            in 1..data.size -> {
                val idx = step - 1
                val current = data[idx]
                if (current == target) {
                    "Сравниваем элемент $current с $target — элемент найден!"
                } else {
                    "Сравниваем элемент $current с $target — не совпадает."
                }
            }
            foundIndex?.plus(1) -> "Элемент $target найден на позиции $foundIndex."
            else -> "Поиск завершён."
        }
    }

    fun toggleAutoMode() {
        isAuto = !isAuto
        if (isAuto) {
            autoJob = viewModelScope.launch {
                while (_stepIndex.value < (foundIndex?.plus(1) ?: (data.size + 1)) && isAuto) {
                    delay(1000)
                    goToNextStep()
                }
                isAuto = false
            }
        } else {
            stopAuto()
        }
    }

    private fun stopAuto() {
        autoJob?.cancel()
        isAuto = false
    }
}
