package com.example.algorithms.viewmodels

import com.example.algorithms.viewmodels.base_class_for_simulation.SortingViewModel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SelectionSortViewModel : SortingViewModel() {
    override suspend fun runSorting() {
        while (_state.value.isRunning) {
            val currentState = _state.value
            if (currentState.i < currentState.list.size - 1) {
                var minIndex = currentState.minIndex.takeIf { it >= currentState.i } ?: currentState.i
                var startJ = (currentState.currentComparisonIndex + 1).coerceAtLeast(currentState.i + 1)

                for (j in startJ until currentState.list.size) {
                    // Ждем, если поставлена пауза
                    while (_state.value.isPaused) {
                        delay(100)
                    }

                    // Обновляем текущий сравниваемый элемент
                    _state.update { it.copy(currentComparisonIndex = j) }

                    val innerState = _state.value
                    if (innerState.list[j] < innerState.list[minIndex]) {
                        minIndex = j
                        _state.update { it.copy(minIndex = minIndex) }
                    }

                    delay(innerState.delayTime)
                }

                if (minIndex != currentState.i) {
                    swapWithAnimation(currentState.i, minIndex)

                    _state.update {
                        it.copy(
                            list = it.list.toMutableList().apply {
                                val temp = this[currentState.i]
                                this[currentState.i] = this[minIndex]
                                this[minIndex] = temp
                            },
                            minIndex = -1
                        )
                    }
                }

                // Обновляем `i`, но НЕ сбрасываем `currentComparisonIndex`
                _state.update { it.copy(i = currentState.i + 1, currentComparisonIndex = currentState.i + 1) }
                delay(currentState.delayTime)
            } else {
                _state.update { it.copy(isRunning = false, currentComparisonIndex = -1, minIndex = -1) }
            }
        }
    }
}