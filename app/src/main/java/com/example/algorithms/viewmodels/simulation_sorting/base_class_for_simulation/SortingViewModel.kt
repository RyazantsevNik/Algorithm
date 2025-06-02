package com.example.algorithms.viewmodels.simulation_sorting.base_class_for_simulation
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.tween
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.algorithms.data.SortingState
import kotlinx.coroutines.Job
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


abstract class SortingViewModel : ViewModel() {

    protected val _state = MutableStateFlow(SortingState())
    val state: StateFlow<SortingState> = _state.asStateFlow()

    val animatedOffsets = mutableStateListOf<Animatable<Float, AnimationVector1D>>()


    init {
        reset()
    }

    fun updateProgress() {
        val listSize = _state.value.list.size
        val totalComparisons = if (listSize > 1) (listSize * (listSize - 1)) / 2 else 0
        val currentComparisons = if (_state.value.i > 0) {
            ((listSize - 1) downTo (listSize - _state.value.i)).sum() + _state.value.j
        } else {
            _state.value.j
        }
        val progress = (currentComparisons.toFloat() / totalComparisons) * 100
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
                    progress = 0f,
                    keyIndex = null,
                    keyValue = null
                )
            }
            updateAnimatedOffsets(newList.size)
        }
    }

    fun reset() {
        val newList = (1..10).shuffled()
        _state.update {
            it.copy(
                list = newList,
                originalList = newList.toList(),
                arraySize = newList.size,
                inputText = newList.joinToString(","),
                i = 0,
                j = 0,
                isRunning = false,
                isPaused = false,
                progress = 0f,
                minIndex = -1,
                currentComparisonIndex = -1,
                keyIndex = null,
                keyValue = null,
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
                progress = 0f,
                isSortingComplete = false,
                minIndex = -1,
                currentComparisonIndex = -1,
                keyIndex = null,
                keyValue = null
            )
        }
    }

    abstract suspend fun runSorting()

    fun updateDelayTime(newDelayTime: Long) {
        _state.update { it.copy(delayTime = newDelayTime.coerceIn(50L, 1000L)) }
    }

    fun updateAnimatedOffsets(size: Int) {
        animatedOffsets.clear()
        animatedOffsets.addAll(List(size) { Animatable(0f) })
    }

    fun toggleEditing() {
        _state.update { it.copy(isEditing = !it.isEditing,

        ) }
    }

    fun resetIndexes(){
        _state.update {
            it.copy(
                minIndex = -1,
                currentComparisonIndex = -1
            )
        }
    }

    fun updateArraySize(newSize: Int) {
        if (newSize < 1 || newSize > 25) return

        val currentList = _state.value.list.toMutableList()

        when {
            newSize > currentList.size -> {

                val additionalElements = List(newSize - currentList.size) { (1..25).random() }
                currentList.addAll(additionalElements)
            }
            newSize < currentList.size -> {

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
                progress = 0f,
                minIndex = -1,
                currentComparisonIndex = -1
            )
        }
        updateAnimatedOffsets(newSize)
    }



    suspend fun swapWithAnimation(index1: Int, index2: Int) {
        if (index1 == index2) return

        val barWidth = 1f
        val horizontalOffsetDistance = barWidth * (index2 - index1).toFloat()


        coroutineScope {
            launch {
                animatedOffsets[index1].animateTo(
                    horizontalOffsetDistance,
                    tween(_state.value.delayTime.toInt())
                )
            }
            launch {
                animatedOffsets[index2].animateTo(
                    -horizontalOffsetDistance,
                    tween(_state.value.delayTime.toInt())
                )
            }.join()

            val newList = _state.value.list.toMutableList()
            val temp = newList[index1]
            newList[index1] = newList[index2]
            newList[index2] = temp


            _state.update { it.copy(list = newList) }

            animatedOffsets[index1].snapTo(0f)
            animatedOffsets[index2].snapTo(0f)
        }
    }



































}
