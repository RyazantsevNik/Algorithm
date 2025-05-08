package com.example.algorithms.viewmodels.step_sorting

import com.example.algorithms.data.SelectionSortStep
import com.example.algorithms.data.SortStep
import com.example.algorithms.viewmodels.step_sorting.base_class_for_step.BaseSortStepViewModel
import com.example.algorithms.viewmodels.profile.ProgressViewModel
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class SelectionSortStepViewModel : BaseSortStepViewModel(), KoinComponent {
    private val progressViewModel: ProgressViewModel by inject()
    private val initialArray = listOf(5, 2, 4, 10, 6, 1, 3, 8, 7, 9)
    private var isCompleted = false
    private var userToken: String? = null

    override val steps: List<SortStep> = generateSelectionSortSteps(initialArray)

    fun setToken(token: String) {
        userToken = token
    }

    override fun handleNextStep() {
        if (currentStepIndex < steps.size - 1) {
            super.goToNextStep()
            
            if (currentStepIndex == steps.size - 1 && !isCompleted) {
                isCompleted = true
                userToken?.let { token ->
                    progressViewModel.updateProgress(
                        token = token,
                        algorithm = "selection_sort",
                        completed = true
                    )
                }
            }
        }
    }

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