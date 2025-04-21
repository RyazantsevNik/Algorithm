package com.example.algorithms.viewmodels.simulation_sorting

import androidx.lifecycle.viewModelScope
import com.example.algorithms.viewmodels.simulation_sorting.base_class_for_simulation.SortingViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


//При паузе неправильная анимация
//class InsertionSortViewModel : SortingViewModel() {
//    override suspend fun runSorting() {
//        _state.update { it.copy(isSortingComplete = false) }
//
//        val list = _state.value.list.toMutableList()
//        val n = list.size
//
//        var i = _state.value.i.coerceAtMost(n - 1)
//        var j = _state.value.j
//        var key = _state.value.keyValue ?: if (i < n) list[i] else return
//        var keyIndex = _state.value.keyIndex ?: i
//
//        while (i < n) {
//            if (!_state.value.isRunning) break
//
//            // Начало новой итерации внешнего цикла
//            if (j == i) {
//                key = list[i]
//                keyIndex = i
//                _state.update {
//                    it.copy(
//                        keyIndex = i,
//                        keyValue = key,
//                        currentComparisonIndex = if (i > 0) i - 1 else -1
//                    )
//                }
//            }
//
//            delayWithPause()
//
//            while (j > 0 && list[j - 1] > key) {
//                pauseIfNeeded()
//
//                // Перед перемещением элемента сохраняем состояние
//                _state.update {
//                    it.copy(
//                        currentComparisonIndex = j - 1,
//                        list = list.toList(), // Фиксируем текущее состояние
//                        keyIndex = keyIndex,
//                        keyValue = key
//                    )
//                }
//
//                // Анимация перемещения
//                swapWithAnimation(j, j - 1)
//
//                // Обновляем список и состояние
//                list[j] = list[j - 1]
//                j--
//
//                _state.update {
//                    it.copy(
//                        list = list.toList(),
//                        j = j,
//                        keyIndex = j,
//                        keyValue = key,
//                        currentComparisonIndex = if (j > 0) j - 1 else -1
//                    )
//                }
//
//                updateProgress()
//                delayWithPause()
//            }
//
//            // Вставка ключа на финальную позицию
//            list[j] = key
//            _state.update {
//                it.copy(
//                    list = list.toList(),
//                    keyIndex = null,
//                    keyValue = null,
//                    currentComparisonIndex = -1
//                )
//            }
//
//            // Подготовка к следующей итерации
//            i++
//            j = i
//
//            _state.update {
//                it.copy(
//                    i = i.coerceAtMost(n - 1),
//                    j = j,
//                    keyIndex = null,
//                    keyValue = null,
//                    currentComparisonIndex = -1
//                )
//            }
//
//            if (i >= n) {
//                _state.update {
//                    it.copy(
//                        isRunning = false,
//                        isPaused = false,
//                        isSortingComplete = true
//                    )
//                }
//                return
//            }
//
//            updateProgress()
//            delayWithPause()
//        }
//    }
//
//    private suspend fun pauseIfNeeded() {
//        while (_state.value.isPaused) {
//            // Сохраняем актуальное состояние перед паузой
//            val currentState = _state.value
//            val currentList = currentState.list.toMutableList()
//
//            // Сбрасываем keyIndex и keyValue только если они не нужны
//            val shouldResetKey = currentState.keyIndex == null || currentState.keyValue == null
//
//            _state.update {
//                it.copy(
//                    list = currentList.toList(), // Фиксируем текущий список
//                    keyIndex = if (shouldResetKey) null else currentState.keyIndex,
//                    keyValue = if (shouldResetKey) null else currentState.keyValue,
//                    currentComparisonIndex = -1
//                )
//            }
//
//            delay(50L)
//            if (!_state.value.isRunning) return
//        }
//    }
//
//
//    private suspend fun delayWithPause() {
//        var elapsed = 0L
//        val step = 50L
//        while (elapsed < _state.value.delayTime) {
//            pauseIfNeeded()
//            delay(step)
//            elapsed += step
//        }
//    }
//}


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
                            savedValue = key // Сохраняем начальное значение
                        )
                    }
                }

                delayWithPause()

                while (j > 0 && list[j - 1] > key) {
                    pauseIfNeeded()

                    // Сохраняем значение элемента, который будет сдвигаться
                    _state.update {
                        it.copy(
                            currentComparisonIndex = j - 1,
                            list = list.toList(),
                            savedValue = list[j - 1] // Сохраняем перемещаемое значение
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

                // Вызываем нашу функцию после внутреннего цикла
                applySavedValue(list, j)

                // Вставка ключа
                list[j] = key
                _state.update {
                    it.copy(
                        list = list.toList(),
                        keyIndex = null,
                        keyValue = null,
                        currentComparisonIndex = -1
                    )
                }

                // Подготовка к следующей итерации
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
                    savedValue = null // Сбрасываем после применения
                )
            }
        }
    }

    private suspend fun pauseIfNeeded() {
        while (_state.value.isPaused) {
            // Сохраняем все текущие значения, включая savedValue
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


////При паузе элементы приравниваются
//class InsertionSortViewModel : SortingViewModel() {
//    override suspend fun runSorting() {
//        _state.update { it.copy(isSortingComplete = false) }
//
//        val list = _state.value.list.toMutableList()
//        val n = list.size
//
//        var i = _state.value.i.coerceAtMost(n - 1)
//        var j = _state.value.j
//        var key = _state.value.keyValue ?: if (i < n) list[i] else return
//        var keyIndex = _state.value.keyIndex ?: i
//
//        while (i < n) {
//            if (!_state.value.isRunning) break
//
//            // Начало новой итерации внешнего цикла
//            if (j == i) {
//                key = list[i]
//                keyIndex = i
//                _state.update {
//                    it.copy(
//                        keyIndex = i,
//                        keyValue = key,
//                        currentComparisonIndex = if (i > 0) i - 1 else -1
//                    )
//                }
//            }
//
//            delayWithPause()
//
//            while (j > 0 && list[j - 1] > key) {
//                pauseIfNeeded()
//
//                // Перед перемещением элемента сохраняем состояние
//                _state.update {
//                    it.copy(
//                        currentComparisonIndex = j - 1,
//                        list = list.toList()  // Фиксируем текущее состояние
//                    )
//                }
//                if (list[j - 1] > key) {
//                    // Только если элементы нужно менять местами
//
//
//                // Анимация перемещения
//                swapWithAnimation(j, j - 1)
//
//                // Обновляем список и состояние
//                list[j] = list[j - 1]
//                j--
//
//                _state.update {
//                    it.copy(
//                        list = list.toList(),
//                        j = j,
//                        keyIndex = j,
//                        keyValue = key,
//                        currentComparisonIndex = if (j > 0) j - 1 else -1
//                    )
//                }
//
//                updateProgress()
//                delayWithPause()
//                } else {
//                break // Прерываем внутренний цикл, если элементы уже в порядке
//            }
//            }
//
//            // КРИТИЧЕСКИЙ УЧАСТОК: вставка ключа на финальную позицию
//            if (j != keyIndex) {
//                // Вставка ключа вручную без анимации свопа!
//                list[j] = key
//                _state.update {
//                    it.copy(
//                        list = list.toList(),
//                        keyIndex = null,
//                        keyValue = null,
//                        currentComparisonIndex = -1
//                    )
//                }
//            } else {
//                // Если key на своём месте, просто убираем подсветку без изменений
//                _state.update {
//                    it.copy(
//                        keyIndex = null,
//                        keyValue = null,
//                        currentComparisonIndex = -1
//                    )
//                }
//            }
//            // Подготовка к следующей итерации
//            i++
//            j = i
//
//            _state.update {
//                it.copy(
//                    i = i.coerceAtMost(n - 1),
//                    j = j,
//                    keyIndex = null,
//                    keyValue = null,
//                    currentComparisonIndex = -1
//                )
//            }
//
//            if (i >= n) {
//                _state.update {
//                    it.copy(
//                        isRunning = false,
//                        isPaused = false,
//                        isSortingComplete = true
//                    )
//                }
//                return
//            }
//
//            updateProgress()
//            delayWithPause()
//        }
//    }
//
//    private suspend fun pauseIfNeeded() {
//        while (_state.value.isPaused) {
//            // Сохраняем актуальное состояние перед паузой
//            val currentState = _state.value
//            val currentList = currentState.list.toMutableList()
//
//            // Сохраняем keyIndex и keyValue
//            val keyIndex = currentState.keyIndex
//            val keyValue = currentState.keyValue
//
//            _state.update {
//                it.copy(
//                    list = currentList.toList(), // Фиксируем текущий список
//                    keyIndex = keyIndex,
//                    keyValue = keyValue,
//                    savedValue = currentState.savedValue,
//                    currentComparisonIndex = -1
//                )
//            }
//
//            delay(50L)
//            if (!_state.value.isRunning) return
//        }
//    }
////    private suspend fun pauseIfNeeded() {
////        while (_state.value.isPaused) {
////            // Сохраняем актуальное состояние перед паузой
////            _state.update {
////                it.copy(
////                    list = it.list.toList(),  // Копируем текущий список
////                   // keyIndex = null,
////                    currentComparisonIndex = -1
////                )
////            }
////            delay(50L)
////            if (!_state.value.isRunning) return
////        }
////    }
//
//
//    private suspend fun delayWithPause() {
//        var elapsed = 0L
//        val step = 50L
//        while (elapsed < _state.value.delayTime) {
//            pauseIfNeeded()
//            delay(step)
//            elapsed += step
//        }
//    }
//}