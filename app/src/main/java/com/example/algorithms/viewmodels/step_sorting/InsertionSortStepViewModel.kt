package com.example.algorithms.viewmodels.step_sorting

import com.example.algorithms.data.InsertionSortStep
import com.example.algorithms.data.SortStep
import com.example.algorithms.viewmodels.step_sorting.base_class_for_step.BaseSortStepViewModel
import com.example.algorithms.viewmodels.profile.ProgressViewModel
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class InsertionSortStepViewModel : BaseSortStepViewModel(), KoinComponent {
    private val progressViewModel: ProgressViewModel by inject()
    private val initialArray = listOf(5, 2, 4, 10, 6, 1, 3, 8, 7, 9)
    private var isCompleted = false
    private var userToken: String? = null

    override val steps: List<SortStep> = generateInsertionSortSteps(initialArray)

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
                        algorithm = "insertion_sort",
                        completed = true
                    )
                }
            }
        }
    }

    private fun generateInsertionSortSteps(arr: List<Int>): List<InsertionSortStep> {
        val steps = mutableListOf<InsertionSortStep>()
        val a = arr.toMutableList()
        steps.add(InsertionSortStep(a.toList(), selectedIndices = null, sortedBoundary = 1))

        for (i in 1 until a.size) {
            var j = i
            steps.add(InsertionSortStep(a.toList(), selectedIndices = Pair(i, i), sortedBoundary = i))

            while (j > 0 && a[j - 1] > a[j]) {

                a[j] = a[j - 1].also { a[j - 1] = a[j] }


                steps.add(InsertionSortStep(a.toList(), selectedIndices = Pair(j - 1, j), sortedBoundary = i))
                j--
            }

            steps.add(InsertionSortStep(a.toList(), selectedIndices = null, sortedBoundary = i + 1))

        }

        steps.add(InsertionSortStep(a.toList(), selectedIndices = null, sortedBoundary = a.size))
        return steps
    }
}