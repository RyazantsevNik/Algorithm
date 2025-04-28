package com.example.algorithms.viewmodels.simulation_search

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class SimulationState(
    val list: List<Int> = (1..10).map { (1..100).random() },
    val targetInput: String = "",
    val inputText: String = "",
    val currentIndex: Int = -1,
    val isRunning: Boolean = false,
    val isPaused: Boolean = false,
    val delayTime: Long = 500L,
    val delayTimeBinary: Long = 1000L,
    val arraySize: Int = 10,
    val progress: Float = 0f,
    val eliminatedIndices: Set<Int> = emptySet(),
    val isSorting: Boolean = false
)


class LinearSearchSimulationViewModel : ViewModel(), SearchSimulationViewModel {


    private val _state = MutableStateFlow(SimulationState())
    override val state: StateFlow<SimulationState> = _state.asStateFlow()

    private var searchJob: Job? = null


    private val animatedOffsets = mutableStateListOf<Animatable<Float, AnimationVector1D>>()

    init {
        reset()
    }

    override fun setInputText(value: String) {
        _state.update { it.copy(inputText = value) }
    }

    override fun setTarget(value: String) {
        searchJob?.cancel()
        searchJob = null
        _state.update {
            it.copy(
                targetInput = value,
                isRunning = false,
                isPaused = false,
                currentIndex = -1,
                progress = 0f
            )
        }
    }

    override fun togglePause() {
        _state.update { it.copy(isPaused = !it.isPaused) }
    }

    override fun applyInput() {
        searchJob?.cancel()
        searchJob = null

        _state.update {
            it.copy(
                targetInput = "",
                inputText = it.inputText,
                isRunning = false,
                isPaused = false,
                currentIndex = -1,
                progress = 0f
            )
        }

        val newList = _state.value.inputText
            .split(",")
            .mapNotNull { it.toIntOrNull()?.takeIf { num -> num in 1..1000 } }
            .take(49)

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
                    progress = 0f
                )
            }
            updateAnimatedOffsets(newList.size)
        }
    }


    override fun reset() {
        searchJob?.cancel()
        searchJob = null

        val newList = (1..49).map { (1..100).random() }
        val defaultTarget = newList.last().toString()
        _state.update {
            it.copy(
                list = newList,
                inputText = newList.joinToString(","),
                targetInput = defaultTarget,
                currentIndex = -1,
                isRunning = false,
                isPaused = false,
                progress = 0f
            )
        }
        updateAnimatedOffsets(_state.value.arraySize)
    }

    private fun updateAnimatedOffsets(size: Int) {
        animatedOffsets.clear()
        animatedOffsets.addAll(List(size) { Animatable(0f) })
    }

    override fun startSearch() {
        if (_state.value.isRunning) return

        _state.update { it.copy(isRunning = true) }

        searchJob = viewModelScope.launch {
            runSearch()
        }
    }

    private suspend fun runSearch() {
        val target = _state.value.targetInput.toIntOrNull() ?: return
        val list = _state.value.list

        for (i in list.indices) {
            while (_state.value.isPaused) delay(100)

            _state.update { it.copy(currentIndex = i, progress = (i.toFloat() / list.size) * 100) }

            if (list[i] == target) {
                _state.update { it.copy(isRunning = false) }
                return
            }

            delay(_state.value.delayTime)
        }

        _state.update { it.copy(isRunning = false) }
    }
}