package com.example.algorithms.viewmodels.simulation_search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class BinarySearchSimulationViewModel : ViewModel(), SearchSimulationViewModel {

    private val _state = MutableStateFlow(SimulationState())
    override val state: StateFlow<SimulationState> = _state.asStateFlow()

    private var searchJob: Job? = null

    init {
        reset()
    }

    override fun setInputText(value: String) {
        _state.update { it.copy(inputText = value, eliminatedIndices = emptySet()) }
    }

    override fun setTarget(value: String) {
        searchJob?.cancel()
        searchJob = null
        _state.update {
            it.copy(
                targetInput = value,
                eliminatedIndices = emptySet(),
                isRunning = false,
                isPaused = false,
                currentIndex = -1,
                progress = 0f,
                isSorting = false,
            )
        }
    }

    override fun togglePause() {
        _state.update { it.copy(isPaused = !it.isPaused) }
    }

    override fun applyInput() {
        searchJob?.cancel()
        searchJob = null

        val newList = _state.value.inputText
            .split(",")
            .mapNotNull { it.toIntOrNull()?.takeIf { n -> n in 1..1000 } }
            .take(49)
            .sorted()

        val newTarget = _state.value.targetInput.toIntOrNull()
        val actualTarget = newTarget?.toString() ?: newList.lastOrNull()?.toString().orEmpty()

        if (newList.isNotEmpty()) {
            _state.update {
                it.copy(
                    list = newList,
                    inputText = newList.joinToString(","),
                    arraySize = newList.size,
                    targetInput = actualTarget,
                    isRunning = false,
                    isPaused = false,
                    currentIndex = -1,
                    progress = 0f,
                    isSorting = false,
                )
            }
        }
    }

    override fun reset() {
        searchJob?.cancel()
        searchJob = null

        val newList = (1..49).map { (1..100).random() }         //.sorted()
        val defaultTarget = newList.last().toString()
        _state.update {
            it.copy(
                list = newList,
                eliminatedIndices = emptySet(),
                inputText = newList.joinToString(","),
                targetInput = defaultTarget,
                currentIndex = -1,
                isRunning = false,
                isPaused = false,
                progress = 0f,
                isSorting = false,
            )
        }
    }

    override fun startSearch() {
        if (_state.value.isRunning) return
        val currentList = _state.value.list
        _state.update {
            it.copy(
                isRunning = false,
                isSorting = true,
                eliminatedIndices = emptySet(),
            )
        }
        viewModelScope.launch {
            delay(2000) // Задержка для отображения надписи "Сортировка..."
            val sortedList = currentList.sorted()
            _state.update {
                it.copy(
                    list = sortedList,
                    isSorting = false,
                    isRunning = true
                )
            }
            searchJob = viewModelScope.launch {
                runSearch()
            }
        }
        //_state.update { it.copy(isRunning = true, eliminatedIndices = emptySet(),) }

//        searchJob = viewModelScope.launch {
//            runSearch()
//        }
    }

    private suspend fun runSearch() {
        //Сортировка
        val originalList = _state.value.list.sorted()
        _state.update { it.copy(list = originalList) }


        val target = _state.value.targetInput.toIntOrNull() ?: return
        val list = _state.value.list

        val eliminated = mutableSetOf<Int>()
        var left = 0
        var right = list.lastIndex

        while (left <= right) {
            while (_state.value.isPaused) delay(100)

            val mid = (left + right) / 2
            val current = list[mid]

            _state.update {
                it.copy(
                    currentIndex = mid,
                    progress = ((mid.toFloat() / list.size) * 100),
                    eliminatedIndices = eliminated.toSet()
                )
            }

            if (current == target) {
                _state.update { it.copy(isRunning = false) }
                return
            }

            delay(_state.value.delayTimeBinary)

            if (current < target) {
                // Отбрасываем левую часть включая mid
                eliminated.addAll(left..mid)
                left = mid + 1
            } else {
                // Отбрасываем правую часть включая mid
                eliminated.addAll(mid..right)
                right = mid - 1
            }
        }
    }
}
