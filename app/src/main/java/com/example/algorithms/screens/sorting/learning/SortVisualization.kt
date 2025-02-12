package com.example.algorithms.screens.sorting.learning

import androidx.compose.runtime.Composable
import com.example.algorithms.data.BubbleSortStep
import com.example.algorithms.data.InsertionSortStep
import com.example.algorithms.data.SelectionSortStep
import com.example.algorithms.data.SortStep
import com.example.algorithms.screens.sorting.learning.bubble_sorting.BubbleSortVisualization
import com.example.algorithms.screens.sorting.learning.insertion_sorting.InsertionSortVisualization
import com.example.algorithms.screens.sorting.learning.selection_sorting.SelectionSortVisualization

@Composable
fun SortVisualization(step: SortStep) {
    when (step) {
        is BubbleSortStep -> BubbleSortVisualization(step)
        is SelectionSortStep -> SelectionSortVisualization(step)
        is InsertionSortStep -> InsertionSortVisualization(step)
    }
}
