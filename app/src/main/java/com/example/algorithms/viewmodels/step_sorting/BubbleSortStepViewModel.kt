package com.example.algorithms.viewmodels.step_sorting

import com.example.algorithms.data.BubbleSortStep
import com.example.algorithms.data.SortStep
import com.example.algorithms.viewmodels.step_sorting.base_class_for_step.BaseSortStepViewModel

class BubbleSortStepViewModel : BaseSortStepViewModel() {

    private val initialArray = listOf(5, 2, 4, 10, 6, 1, 3, 8, 7, 9)

    override val steps: List<SortStep> = generateBubbleSortSteps(initialArray)

    private fun generateBubbleSortSteps(arr: List<Int>): List<BubbleSortStep> {
        val steps = mutableListOf<BubbleSortStep>()
        val a = arr.toMutableList()
        steps.add(BubbleSortStep(a.toList(), comparedIndices = null, sortedBoundary = a.size))
        val n = a.size

        for (i in 0 until n - 1) {
            for (j in 0 until n - i - 1) {
                steps.add(BubbleSortStep(a.toList(), comparedIndices = Pair(j, j + 1), sortedBoundary = n - i))
                if (a[j] > a[j + 1]) {
                    a[j] = a[j + 1].also { a[j + 1] = a[j] }
                    steps.add(BubbleSortStep(a.toList(), comparedIndices = Pair(j, j + 1), sortedBoundary = n - i))
                }
            }
            steps.add(BubbleSortStep(a.toList(), comparedIndices = null, sortedBoundary = n - i - 1))
        }
        steps.add(BubbleSortStep(a.toList(), comparedIndices = null, sortedBoundary = 0))
        return steps
    }
}