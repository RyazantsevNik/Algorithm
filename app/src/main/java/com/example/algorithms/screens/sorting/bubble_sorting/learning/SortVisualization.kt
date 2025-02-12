package com.example.algorithms.screens.sorting.bubble_sorting.learning

import androidx.compose.runtime.Composable
import com.example.algorithms.data.BubbleSortStep
import com.example.algorithms.data.SelectionSortStep
import com.example.algorithms.data.SortStep
import com.example.algorithms.screens.sorting.seletion_sorting.learning.SelectionSortVisualization

@Composable
fun SortVisualization(step: SortStep) {
    when (step) {
        is BubbleSortStep -> BubbleSortVisualization(step)
        is SelectionSortStep -> SelectionSortVisualization(step)
    }
}
