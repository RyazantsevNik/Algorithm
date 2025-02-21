package com.example.algorithms.viewmodels

import com.example.algorithms.data.QuickSortStep
import com.example.algorithms.data.SortStep

class QuickSortStepViewModel : BaseSortStepViewModel() {
    private val initialArray = listOf(5, 2, 8, 10, 6, 1, 3, 4)
    override val steps: List<SortStep> = generateQuickSortSteps(initialArray)


    private fun generateQuickSortSteps(arr: List<Int>): List<QuickSortStep> {
        val steps = mutableListOf<QuickSortStep>()
        val sortedIndices = mutableSetOf<Int>()
        var sortedBoundary = 0 // Начальное значение sortedBoundary

        fun MutableList<Int>.swap(i: Int, j: Int) {
            val temp = this[i]
            this[i] = this[j]
            this[j] = temp
        }

        fun quickSort(array: MutableList<Int>, low: Int, high: Int) {
            if (low < high) {
                val pivotIndex = high
                val pivot = array[pivotIndex]

                // Шаг: выбор опорного элемента
                steps.add(QuickSortStep(
                    array = array.toList(),
                    pivotIndex = pivotIndex,
                    partitionRange = Pair(low, high),
                    sortedIndices = sortedIndices.toSet(),
                    sortedBoundary = sortedBoundary
                ))

                var wall = low



                for (i in low until high) {
                    if (array[i] < pivot) {
                        // Шаг 1: фиксируем, что элемент меньше опорного
                        steps.add(QuickSortStep(
                            array = array.toList(),
                            pivotIndex = pivotIndex,
                            leftPointer = wall,
                            rightPointer = i,
                            partitionRange = Pair(low, high),
                            sortedIndices = sortedIndices.toSet(),
                            sortedBoundary = sortedBoundary
                        ))

                        // Шаг 2: выполняем обмен (если необходимо)
                        if (i != wall) {
                            array.swap(i, wall)
                        }

                        // Шаг 3: фиксируем, что обмен произошел и стена сдвинулась
                        wall++
                        steps.add(QuickSortStep(
                            array = array.toList(),
                            pivotIndex = pivotIndex,
                            leftPointer = wall,
                            rightPointer = i,
                            partitionRange = Pair(low, high),
                            sortedIndices = sortedIndices.toSet(),
                            sortedBoundary = sortedBoundary
                        ))
                    } else {
                        steps.add(QuickSortStep(
                            array = array.toList(),
                            pivotIndex = pivotIndex,
                            leftPointer = wall,
                            rightPointer = i,
                            partitionRange = Pair(low, high),
                            sortedIndices = sortedIndices.toSet(),
                            sortedBoundary = sortedBoundary
                        ))
                    }
                }


                // Устанавливаем опорный элемент на окончательную позицию
                array.swap(wall, pivotIndex)
                sortedIndices.add(wall)

                sortedBoundary++

                steps.add(QuickSortStep(
                    array = array.toList(),
                    pivotIndex = wall,
                    partitionRange = Pair(low, high),
                    sortedIndices = sortedIndices.toSet(),
                    sortedBoundary = sortedBoundary
                ))

                quickSort(array, low, wall - 1)
                quickSort(array, wall + 1, high)
            } else if (low == high) {
                sortedIndices.add(low)
                sortedBoundary++
                steps.add(QuickSortStep(
                    array = array.toList(),
                    sortedIndices = sortedIndices.toSet(),
                    sortedBoundary =sortedBoundary
                ))
            }
        }

        val mutableArray = arr.toMutableList()
        steps.add(QuickSortStep(
            array = mutableArray.toList(),
            sortedIndices = sortedIndices.toSet(),
            sortedBoundary = sortedBoundary
        ))
        quickSort(mutableArray, 0, mutableArray.size - 1)
        steps.add(QuickSortStep(
            array = mutableArray.toList(),
            sortedIndices = sortedIndices.toSet(),
            sortedBoundary = sortedBoundary
        ))
        return steps
    }
}
