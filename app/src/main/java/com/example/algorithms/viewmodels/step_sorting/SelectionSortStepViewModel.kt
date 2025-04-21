package com.example.algorithms.viewmodels.step_sorting

import com.example.algorithms.data.SelectionSortStep
import com.example.algorithms.data.SortStep
import com.example.algorithms.viewmodels.step_sorting.base_class_for_step.BaseSortStepViewModel

class SelectionSortStepViewModel : BaseSortStepViewModel() {


    private val initialArray = listOf(5, 2, 4, 10, 6, 1, 3, 8, 7, 9)
    override val steps: List<SortStep> = generateSelectionSortSteps(initialArray)

    private fun generateSelectionSortSteps(arr: List<Int>): List<SelectionSortStep> {
        val steps = mutableListOf<SelectionSortStep>()
        val a = arr.toMutableList()
        steps.add(SelectionSortStep(a.toList(), selectedIndices = null, sortedBoundary = 0))

        for (i in 0 until a.size - 1) {
            var minIndex = i
            for (j in i + 1 until a.size) {
                steps.add(SelectionSortStep(a.toList(), selectedIndices = Pair(i, j), sortedBoundary = i))
                if (a[j] < a[minIndex]) {
                    minIndex = j
                }
            }
            if (minIndex != i) {
                a[i] = a[minIndex].also { a[minIndex] = a[i] }
                steps.add(SelectionSortStep(a.toList(), selectedIndices = Pair(i, minIndex), sortedBoundary = i))
            }
            steps.add(SelectionSortStep(a.toList(), selectedIndices = null, sortedBoundary = i + 1))
        }
        steps.add(SelectionSortStep(a.toList(), selectedIndices = null, sortedBoundary = a.size))
        return steps
    }
}