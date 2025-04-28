package com.example.algorithms.viewmodels.simulation_sorting

import androidx.lifecycle.viewModelScope
import com.example.algorithms.viewmodels.simulation_sorting.base_class_for_simulation.SortingViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
















































































































































class InsertionSortViewModel : SortingViewModel() {
        override suspend fun runSorting() {
            _state.update { it.copy(isSortingComplete = false) }

            val list = _state.value.list.toMutableList()
            val n = list.size

            var i = _state.value.i.coerceAtMost(n - 1)
            var j = _state.value.j
            var key = _state.value.keyValue ?: if (i < n) list[i] else return
            var keyIndex = _state.value.keyIndex ?: i

            while (i < n) {
                if (!_state.value.isRunning) break

                if (j == i) {
                    key = list[i]
                    keyIndex = i
                    _state.update {
                        it.copy(
                            keyIndex = i,
                            keyValue = key,
                            currentComparisonIndex = if (i > 0) i - 1 else -1,
                            savedValue = key
                        )
                    }
                }

                delayWithPause()

                while (j > 0 && list[j - 1] > key) {
                    pauseIfNeeded()


                    _state.update {
                        it.copy(
                            currentComparisonIndex = j - 1,
                            list = list.toList(),
                            savedValue = list[j - 1]
                        )
                    }

                    if (list[j - 1] > key) {
                        swapWithAnimation(j, j - 1)
                        list[j] = list[j - 1]
                        j--

                        _state.update {
                            it.copy(
                                list = list.toList(),
                                j = j,
                                keyIndex = j,
                                keyValue = key,
                                currentComparisonIndex = if (j > 0) j - 1 else -1
                            )
                        }

                        updateProgress()
                        delayWithPause()
                    } else {
                        break
                    }
                }


                applySavedValue(list, j)


                list[j] = key
                _state.update {
                    it.copy(
                        list = list.toList(),
                        keyIndex = null,
                        keyValue = null,
                        currentComparisonIndex = -1
                    )
                }


                i++
                j = i

                _state.update {
                    it.copy(
                        i = i.coerceAtMost(n - 1),
                        j = j,
                        keyIndex = null,
                        keyValue = null,
                        currentComparisonIndex = -1,
                        savedValue = null
                    )
                }

                if (i >= n) {
                    _state.update {
                        it.copy(
                            isRunning = false,
                            isPaused = false,
                            isSortingComplete = true
                        )
                    }
                    return
                }

                updateProgress()
                delayWithPause()
            }
        }

    private fun applySavedValue(list: MutableList<Int>, j: Int) {
        _state.value.savedValue?.let { savedValue ->
            list[j] = savedValue
            _state.update {
                it.copy(
                    list = list.toList(),
                    savedValue = null
                )
            }
        }
    }

    private suspend fun pauseIfNeeded() {
        while (_state.value.isPaused) {

            val currentState = _state.value
            _state.update {
                it.copy(
                    list = currentState.list.toList(),
                    keyIndex = currentState.keyIndex,
                    keyValue = currentState.keyValue,
                    savedValue = currentState.savedValue,
                    currentComparisonIndex = -1
                )
            }
            delay(50L)
            if (!_state.value.isRunning) return
        }
    }

    private suspend fun delayWithPause() {
        var elapsed = 0L
        val step = 50L
        while (elapsed < _state.value.delayTime) {
            pauseIfNeeded()
            delay(step)
            elapsed += step
        }
    }
}












































































































































































