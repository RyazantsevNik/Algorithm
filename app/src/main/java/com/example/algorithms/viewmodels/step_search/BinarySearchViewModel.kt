package com.example.algorithms.viewmodels.step_search

import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
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
import kotlin.math.log2

class BinarySearchViewModel : ViewModel(), KoinComponent {
    private val progressViewModel: ProgressViewModel by inject()
    private var isCompleted = false
    private var userToken: String? = null

    fun setToken(token: String) {
        userToken = token
    }

    sealed class SearchStep {
        object Initial : SearchStep()
        data class CalculateMid(val mid: Int) : SearchStep()
        data class CompareValues(val mid: Int, val comparison: String) : SearchStep()
        data class AdjustBounds(val newLeft: Int, val newRight: Int) : SearchStep()
        data class Found(val index: Int) : SearchStep()
        object NotFound : SearchStep()
    }

    val data = listOf(1, 3, 4, 5, 7, 9)
    val target = 5

    private val _currentStep = mutableStateOf<SearchStep>(SearchStep.Initial)
    val currentStep: State<SearchStep> = _currentStep

    private val _stepIndex = mutableIntStateOf(0)
    val stepIndex: State<Int> = _stepIndex

    private val _left = mutableIntStateOf(0)
    val left: State<Int> = _left

    private val _right = mutableIntStateOf(data.size - 1)
    val right: State<Int> = _right

    private val _mid = mutableIntStateOf(-1)
    val mid: State<Int> = _mid

    private val _disabledIndices = mutableStateListOf<Int>()
    val disabledIndices: List<Int> = _disabledIndices

    private fun getInitialExplanation(): String {
        return "Бинарный поиск работает путём многократного деления списка пополам. " +
                "Начнём поиск элемента $target в диапазоне [${_left.intValue}, ${_right.intValue}]."
    }

    private val _explanation = mutableStateOf(getInitialExplanation())
    val explanation: State<String> = _explanation

    var isAuto by mutableStateOf(false)
        private set
    private var autoJob: Job? = null

    private val _stepHistory = mutableStateListOf<SearchStep>()
    val stepHistory: List<SearchStep> = _stepHistory

    fun goToNextStep() {
        when (val step = _currentStep.value) {
            is SearchStep.Initial -> calculateMid()
            is SearchStep.CalculateMid -> compareValues(step.mid)
            is SearchStep.CompareValues -> adjustBounds(step.mid, step.comparison)
            is SearchStep.AdjustBounds -> {
                if (_left.intValue <= _right.intValue) {
                    calculateMid()
                } else {
                    _currentStep.value = SearchStep.NotFound
                    _explanation.value = "Элемент $target не найден"
                }
            }
            else -> {}
        }
        _stepIndex.intValue++
        _stepHistory.add(_currentStep.value)

        if (_currentStep.value is SearchStep.Found || _currentStep.value is SearchStep.NotFound) {
            if (!isCompleted) {
                isCompleted = true
                userToken?.let { token ->
                    progressViewModel.updateProgress(
                        token = token,
                        algorithm = "binary_search",
                        completed = true
                    )
                }
            }
        }
    }

    private fun calculateMid() {
        val midValue = (_left.intValue + _right.intValue) / 2
        _mid.intValue = midValue
        _currentStep.value = SearchStep.CalculateMid(midValue)
        _explanation.value = "Вычисляем средний индекс: $midValue (значение ${data[midValue]})"
    }

    private fun compareValues(mid: Int) {
        val comparison = when {
            data[mid] == target -> "equal"
            data[mid] < target -> "less"
            else -> "greater"
        }
        _currentStep.value = SearchStep.CompareValues(mid, comparison)
        _explanation.value = when (comparison) {
            "equal" -> "Нашли! ${data[mid]} == $target"
            "less" -> "Сравниваем: ${data[mid]} < $target → идём вправо"
            else -> "Сравниваем: ${data[mid]} > $target → идём влево"
        }
    }

    private fun adjustBounds(mid: Int, comparison: String) {
        when (comparison) {
            "equal" -> {
                _currentStep.value = SearchStep.Found(mid)
                _explanation.value = "Элемент $target найден на позиции $mid"
            }
            "less" -> {
                (_left.intValue..mid).forEach { _disabledIndices.add(it) }
                _left.intValue = mid + 1
                _currentStep.value = SearchStep.AdjustBounds(_left.intValue, _right.intValue)
                _explanation.value = "Обновляем границы поиска: [${_left.intValue}, ${_right.intValue}] → идём вправо"
            }
            else -> {
                (mid.._right.intValue).forEach { _disabledIndices.add(it) }
                _right.intValue = mid - 1
                _currentStep.value = SearchStep.AdjustBounds(_left.intValue, _right.intValue)
                _explanation.value = "Обновляем границы поиска: [${_left.intValue}, ${_right.intValue}] → идём влево"
            }
        }
    }

    fun goToStart() {
        stopAuto()
        _stepIndex.intValue = 0
        _left.intValue = 0
        _right.intValue = data.size - 1
        _mid.intValue = -1
        _disabledIndices.clear()
        _currentStep.value = SearchStep.Initial
        _explanation.value = getInitialExplanation()
        _stepHistory.clear()
        _stepHistory.add(SearchStep.Initial)
    }

    fun goToEnd() {
        stopAuto()
        goToStart()

        while (_currentStep.value !is SearchStep.Found && _currentStep.value !is SearchStep.NotFound) {
            goToNextStep()
        }
    }

    fun goToPreviousStep() {
        if (_stepHistory.size > 1) {
            val lastIndex = _stepHistory.size - 1
            _stepHistory.removeAt(lastIndex)
            val previousStep = _stepHistory.last()

            when (previousStep) {
                is SearchStep.Initial -> {
                    _left.intValue = 0
                    _right.intValue = data.size - 1
                    _mid.intValue = -1
                    _disabledIndices.clear()
                    _explanation.value = "Начнём поиск элемента $target"
                }
                is SearchStep.CalculateMid -> {
                    _mid.intValue = previousStep.mid
                    _explanation.value = "Вычисляем средний индекс: ${previousStep.mid} (значение ${data[previousStep.mid]})"
                }
                is SearchStep.CompareValues -> {
                    _mid.intValue = previousStep.mid
                    _explanation.value = when (previousStep.comparison) {
                        "equal" -> "Нашли! ${data[previousStep.mid]} == $target"
                        "less" -> "Сравниваем: ${data[previousStep.mid]} < $target → идём вправо"
                        else -> "Сравниваем: ${data[previousStep.mid]} > $target → идём влево"
                    }
                }
                is SearchStep.AdjustBounds -> {
                    _left.intValue = previousStep.newLeft
                    _right.intValue = previousStep.newRight
                    _disabledIndices.clear()
                    if (previousStep.newLeft > 0) {
                        (0 until previousStep.newLeft).forEach { _disabledIndices.add(it) }
                    }
                    if (previousStep.newRight < data.size - 1) {
                        ((previousStep.newRight + 1)..(data.size - 1)).forEach { _disabledIndices.add(it) }
                    }
                    _explanation.value = "Обновляем границы поиска: [${previousStep.newLeft}, ${previousStep.newRight}]"
                }
                is SearchStep.Found -> {
                    _currentStep.value = SearchStep.Found(previousStep.index)
                    _explanation.value = "Элемент $target найден на позиции ${previousStep.index}"
                }
                is SearchStep.NotFound -> {
                    _currentStep.value = SearchStep.NotFound
                    _explanation.value = "Элемент $target не найден"
                }
            }

            _currentStep.value = previousStep
            _stepIndex.intValue = maxOf(0, _stepIndex.intValue - 1)
        }
    }

    fun toggleAutoMode() {
        isAuto = !isAuto
        if (isAuto) {
            autoJob = viewModelScope.launch {
                while (isAuto && _currentStep.value !is SearchStep.Found
                    && _currentStep.value !is SearchStep.NotFound
                ) {
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