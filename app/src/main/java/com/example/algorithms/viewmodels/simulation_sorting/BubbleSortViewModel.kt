package com.example.algorithms.viewmodels.simulation_sorting

import com.example.algorithms.viewmodels.simulation_sorting.base_class_for_simulation.SortingViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.update

class BubbleSortViewModel : SortingViewModel() {
    override suspend fun runSorting() {
        while (_state.value.isRunning && !_state.value.isPaused) {
            val currentState = _state.value
            if (currentState.i < currentState.list.size - 1) {
                if (currentState.j < currentState.list.size - currentState.i - 1) {
                    if (currentState.list[currentState.j] > currentState.list[currentState.j + 1]) {
                        coroutineScope {
                            val animation1 = async {
                                animatedOffsets[currentState.j].animateTo(1f)
                            }
                            val animation2 = async {
                                animatedOffsets[currentState.j + 1].animateTo(-1f)
                            }
                            awaitAll(animation1, animation2)
                        }

                        // Сбрасываем анимационные смещения сразу после завершения анимации
                        animatedOffsets[currentState.j].snapTo(0f)
                        animatedOffsets[currentState.j + 1].snapTo(0f)

                        _state.update {
                            it.copy(
                                list = it.list.toMutableList().apply {
                                    val temp = this[currentState.j]
                                    this[currentState.j] = this[currentState.j + 1]
                                    this[currentState.j + 1] = temp
                                },
                                j = currentState.j + 1
                            )
                        }

                        animatedOffsets[currentState.j].snapTo(0f)
                        animatedOffsets[currentState.j + 1].snapTo(0f)
                    } else {
                        _state.update { it.copy(j = it.j + 1) }
                    }
                } else {
                    _state.update { it.copy(j = 0, i = it.i + 1) }
                }
                delay(currentState.delayTime)
            } else {
                _state.update { it.copy(isRunning = false) }
            }
        }
    }
}