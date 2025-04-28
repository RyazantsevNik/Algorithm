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
    val currentComparisonIndex: Int = -1,
    val keyValue: Int? = null,
    val keyIndex: Int? = null,
    val isSortingComplete: Boolean = false,
    val savedValue: Int? = null,
)
