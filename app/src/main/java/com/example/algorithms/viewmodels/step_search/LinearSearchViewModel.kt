package com.example.algorithms.viewmodels.step_search

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.algorithms.viewmodels.profile.ProgressViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class LinearSearchViewModel : ViewModel(), KoinComponent {
    private val progressViewModel: ProgressViewModel by inject()
    private var isCompleted = false
    private var userToken: String? = null

    fun setToken(token: String) {
        userToken = token
    }

    val data = listOf(5, 3, 9, 1, 4, 7)
    val target = 9

    private val _stepIndex = mutableIntStateOf(0)
    val stepIndex: State<Int> = _stepIndex

    private val _currentIndex = mutableIntStateOf(-1)
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
        _stepIndex.intValue = 0
        _currentIndex.intValue = -1
        updateExplanation()
    }

    fun goToEnd() {
        stopAuto()

        if (foundIndex == null) {
            for ((index, value) in data.withIndex()) {
                if (value == target) {
                    foundIndex = index
                    break
                }
            }
        }

        _stepIndex.intValue = if (foundIndex != null) {
            foundIndex!! + 1
        } else {
            data.size + 1
        }

        _currentIndex.intValue = foundIndex ?: -1
        updateExplanation()
    }

    fun goToNextStep() {
        val totalSteps = if (foundIndex != null) foundIndex!! + 2 else data.size + 2
        if (_stepIndex.intValue >= totalSteps - 1) return

        _stepIndex.intValue++

        when (_stepIndex.intValue) {
            0 -> _currentIndex.intValue = -1
            in 1..data.size -> {
                val index = _stepIndex.intValue - 1
                _currentIndex.intValue = index
                if (data[index] == target && foundIndex == null) {
                    foundIndex = index
                    if (!isCompleted) {
                        isCompleted = true
                        userToken?.let { token ->
                            Log.d("LinearSearch", "Saving progress for linear_search")
                            progressViewModel.updateProgress(
                                token = token,
                                algorithm = "linear_search",
                                completed = true
                            )
                        }
                    }
                }
            }
            foundIndex?.plus(1) -> _currentIndex.intValue = foundIndex!!
        }

        updateExplanation()
    }

    fun goToPreviousStep() {
        if (_stepIndex.intValue <= 0) return
        stopAuto()

        _stepIndex.intValue--

        if (_stepIndex.intValue == 0) {
            _currentIndex.intValue = -1
        } else {
            val idx = _stepIndex.intValue - 1
            _currentIndex.intValue = if (foundIndex != null && idx > foundIndex!!) {
                foundIndex!!
            } else {
                idx
            }
        }

        if (_stepIndex.intValue <= (foundIndex ?: Int.MAX_VALUE)) {
            foundIndex = null
        }

        updateExplanation()
    }

    private fun updateExplanation() {
        val step = _stepIndex.intValue
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
                while (_stepIndex.intValue < (foundIndex?.plus(1) ?: (data.size + 1)) && isAuto) {
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
