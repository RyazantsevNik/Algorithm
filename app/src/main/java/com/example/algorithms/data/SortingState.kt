package com.example.algorithms.data

data class SortingState(
    val list: List<Int> = (1..10).shuffled(),
    val i: Int = 0,
    val j: Int = 0,
    val isRunning: Boolean = false,
    val isPaused: Boolean = false,
    val delayTime: Long = 550L,
    val inputText: String = "",
    val originalList: List<Int> = list,
    val isEditing: Boolean = false,
    val arraySize: Int = list.size,
    val progress: Float = 0f,
    val minIndex: Int = -1,
    val currentComparisonIndex: Int = -1, // Текущий сравниваемый элемент
)
