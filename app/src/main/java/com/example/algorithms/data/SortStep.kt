package com.example.algorithms.data

sealed class SortStep(
    open val array: List<Int>,
    open val sortedBoundary: Int
)

data class BubbleSortStep(
    override val array: List<Int>,
    val comparedIndices: Pair<Int, Int>?,
    override val sortedBoundary: Int
) : SortStep(array, sortedBoundary)

data class SelectionSortStep(
    override val array: List<Int>,
    val selectedIndices: Pair<Int, Int>?,
    override val sortedBoundary: Int
) : SortStep(array, sortedBoundary)

data class InsertionSortStep(
    override val array: List<Int>,
    val selectedIndices: Pair<Int, Int>?,
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