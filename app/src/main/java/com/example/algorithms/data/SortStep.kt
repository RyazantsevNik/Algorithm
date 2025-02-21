package com.example.algorithms.data

sealed class SortStep(
    open val array: List<Int>,
    open val sortedBoundary: Int // Элементы с индексом >= sortedBoundary уже отсортированы
)

data class BubbleSortStep(
    override val array: List<Int>,
    val comparedIndices: Pair<Int, Int>?, // Если null, значит это шаг-маркер (например, конец прохода)
    override val sortedBoundary: Int
) : SortStep(array, sortedBoundary)

data class SelectionSortStep(
    override val array: List<Int>,
    val selectedIndices: Pair<Int, Int>?, // Индексы текущего сравнения или обмена
    override val sortedBoundary: Int
) : SortStep(array, sortedBoundary)

data class InsertionSortStep(
    override val array: List<Int>,
    val selectedIndices: Pair<Int, Int>?, // Индексы текущего сравнения или обмена
    override val sortedBoundary: Int
) : SortStep(array, sortedBoundary)

data class QuickSortStep(
    override val array: List<Int>,
    val pivotIndex: Int? = null,
    val leftPointer: Int? = null,
    val rightPointer: Int? = null,
    val partitionRange: Pair<Int, Int>? = null,
    val sortedIndices: Set<Int> = emptySet(),
    override val sortedBoundary: Int
) : SortStep(array, sortedBoundary)