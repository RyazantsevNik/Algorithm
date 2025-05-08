package com.example.algorithms.viewmodels.step_sorting

import com.example.algorithms.data.QuickSortStep
import com.example.algorithms.data.SortStep
import com.example.algorithms.viewmodels.step_sorting.base_class_for_step.BaseSortStepViewModel
import com.example.algorithms.viewmodels.profile.ProgressViewModel
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class QuickSortStepViewModel : BaseSortStepViewModel(), KoinComponent {
    private val progressViewModel: ProgressViewModel by inject()
    private val initialArray = listOf(5, 2, 7, 6, 1, 3, 4)
    private var isCompleted = false
    private var userToken: String? = null

    override val steps: List<SortStep> = generateQuickSortSteps(initialArray)

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
                        algorithm = "quick_sort",
                        completed = true
                    )
                }
            }
        }
    }

    private fun generateQuickSortSteps(arr: List<Int>): List<QuickSortStep> {
        val steps = mutableListOf<QuickSortStep>()
        val sortedIndices = mutableSetOf<Int>()
        var sortedBoundary = 0

        fun MutableList<Int>.swap(i: Int, j: Int) {
            val temp = this[i]
            this[i] = this[j]
            this[j] = temp
        }

        fun quickSort(array: MutableList<Int>, low: Int, high: Int) {
            if (low < high) {
                val pivotIndex = high
                val pivot = array[pivotIndex]


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

                        steps.add(QuickSortStep(
                            array = array.toList(),
                            pivotIndex = pivotIndex,
                            leftPointer = wall,
                            rightPointer = i,
                            partitionRange = Pair(low, high),
                            sortedIndices = sortedIndices.toSet(),
                            sortedBoundary = sortedBoundary
                        ))


                        if (i != wall) {
                            array.swap(i, wall)
                        }


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
