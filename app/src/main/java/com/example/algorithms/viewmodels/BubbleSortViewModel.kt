package com.example.algorithms.viewmodels

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.tween
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class BubbleSortState(
    val list: List<Int> = (1..10).shuffled(),
    val i: Int = 0,
    val j: Int = 0,
    val isRunning: Boolean = false,
    val isPaused: Boolean = false,
    val delayTime: Long = 550L,
    val inputText: String = "",
    val originalList: List<Int> = list,
    val isEditing: Boolean = false,  // Показываем ли окно редактирования
    val arraySize: Int = list.size,  // Размер массива
    val progress: Float = 0f,
    val currentStep: Int = 0
)


class BubbleSortViewModel : ViewModel() {
    private val _state = MutableStateFlow(BubbleSortState())
    open val state: StateFlow<BubbleSortState> = _state.asStateFlow()

    // Список анимаций для каждого элемента
    val animatedOffsets = mutableStateListOf<Animatable<Float, AnimationVector1D>>()

    init {
       reset()
    }

    private fun updateProgress() {
        val listSize = _state.value.list.size
        val totalComparisons = if (listSize > 1) (listSize * (listSize - 1)) / 2 else 0

        // Вычисляем текущее количество выполненных сравнений
        val currentComparisons = if (_state.value.i > 0) {
            // Сумма сравнений на предыдущих шагах
            ((listSize - 1) downTo (listSize - _state.value.i)).sum() + _state.value.j
        } else {
            // Если это первый шаг, просто берем текущее значение j
            _state.value.j
        }

        // Вычисляем прогресс
        val progress = if (totalComparisons > 0) {
            (currentComparisons.toFloat() / totalComparisons) * 100
        } else {
            0f
        }

        // Обновляем состояние с новым значением прогресса
        _state.update { it.copy(progress = progress.coerceIn(0f, 100f)) }
    }


    fun setInputText(value: String) {
        _state.update { it.copy(inputText = value) }
    }

    fun setListFromInput() {
        val newList = _state.value.inputText
            .split(",")
            .mapNotNull { it.toIntOrNull()?.takeIf { num -> num in 1..1000 } }
            .take(25)
        if (newList.isNotEmpty()) {
            _state.update {
                it.copy(
                    list = newList,
                    originalList = newList.toList(),
                    arraySize = newList.size,
                    i = 0,
                    j = 0,
                    isRunning = false,
                    isPaused = false,
                    progress = 0f
                )
            }
            updateAnimatedOffsets(newList.size)
        }
    }

    fun reset() {
        val newList = (1..10).shuffled()
        // Обновляем состояние, не меняя список во время сортировки
        _state.update {
            it.copy(
                list = newList,
                originalList = newList.toList(),
                arraySize = newList.size,  // Используем размер нового списка
                inputText = newList.joinToString(","),
                i = 0,
                j = 0,
                isRunning = false,
                isPaused = false,
                progress = 0f
            )
        }
        updateAnimatedOffsets(newList.size)
    }

    fun togglePause() {
        _state.update { it.copy(isPaused = !it.isPaused) }
    }

    fun startSorting() {
        if (_state.value.originalList.isEmpty()) {
            _state.update { it.copy(originalList = it.list.toList()) }
        }
        _state.update { it.copy(isRunning = true, isPaused = false) }
    }

    fun repeatSorting() {
        _state.update {
            it.copy(
                list = it.originalList.toList(),
                i = 0,
                j = 0,
                isRunning = false,
                isPaused = true,
                progress = 0f
            )
        }
    }


    suspend fun runSorting() {
        while (_state.value.isRunning && !_state.value.isPaused) {
            val currentState = _state.value
            if (currentState.i < currentState.list.size - 1) {
                if (currentState.j < currentState.list.size - currentState.i - 1) {
                    if (currentState.list[currentState.j] > currentState.list[currentState.j + 1]) {
                        // Анимация смены блоков местами
                        coroutineScope {
                            val animation1 = async {
                                animatedOffsets[currentState.j].animateTo(
                                    targetValue = 1f,
                                    animationSpec = tween(durationMillis = (currentState.delayTime / 5).toInt())
                                )
                            }
                            val animation2 = async {
                                animatedOffsets[currentState.j + 1].animateTo(
                                    targetValue = -1f,
                                    animationSpec = tween(durationMillis = (currentState.delayTime / 5).toInt())
                                )
                            }
                            // Ожидаем завершения обеих анимаций
                            awaitAll(animation1, animation2)
                        }
                        // Обновляем список ПОСЛЕ завершения анимации
                        _state.update { current ->
                            current.copy(
                                list = current.list.toMutableList().apply {
                                    val temp = this[current.j]
                                    this[current.j] = this[current.j + 1]
                                    this[current.j + 1] = temp
                                },
                                j = current.j + 1
                            )
                        }

                        // Возвращаем анимацию в исходное положение
                        animatedOffsets[currentState.j].snapTo(0f)
                        animatedOffsets[currentState.j + 1].snapTo(0f)
                    } else {
                        // Если нет обмена, просто сбрасываем анимацию
                        animatedOffsets[currentState.j].snapTo(0f)
                        animatedOffsets[currentState.j + 1].snapTo(0f)
                        _state.update { it.copy(j = it.j + 1) }
                    }
                } else {
                    _state.update { it.copy(j = 0, i = it.i + 1) }
                }

                updateProgress()
                delay(currentState.delayTime)
            } else {
                _state.update { it.copy(isRunning = false) }
            }
        }
    }

    fun updateDelayTime(newDelayTime: Long) {
        _state.update { it.copy(delayTime = newDelayTime.coerceIn(50L, 1000L)) }
    }

    fun updateAnimatedOffsets(size: Int) {
        animatedOffsets.clear()
        animatedOffsets.addAll(List(size) { Animatable(0f) })
    }

    //  Управление isEditing
    fun toggleEditing() {
        _state.update { it.copy(isEditing = !it.isEditing) }
    }

    fun updateArraySize(newSize: Int) {
        if (newSize < 1 || newSize > 25) return  // Ограничение на размер массива

        val currentList = _state.value.list.toMutableList()

        when {
            newSize > currentList.size -> {
                // Увеличиваем массив, добавляя новые случайные элементы
                val additionalElements = List(newSize - currentList.size) { (1..25).random() }
                currentList.addAll(additionalElements)
            }
            newSize < currentList.size -> {
                // Уменьшаем массив, удаляя последние элементы
                currentList.subList(newSize, currentList.size).clear()
            }
        }

        _state.update {
            it.copy(
                list = currentList,
                originalList = currentList.toList(),
                arraySize = newSize,
                inputText = currentList.joinToString(","),
                i = 0,
                j = 0,
                isRunning = false,
                isPaused = false,
                progress = 0f
            )
        }
        updateAnimatedOffsets(newSize)
    }
}