package com.example.algorithms.screens.sorting

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.example.algorithms.screens.algorithm_screen.AlgorithmScreen
import com.example.algorithms.screens.sorting.learning.bubble_sorting.BubbleSortingLearning
import com.example.algorithms.screens.sorting.learning.insertion_sorting.InsertionSortingLearning
import com.example.algorithms.screens.sorting.learning.quick_sorting.QuickSortingLearning
import com.example.algorithms.screens.sorting.simulation.bubble_sorting.BubbleSortingSimulation
import com.example.algorithms.screens.sorting.learning.selection_sorting.SelectionSortingLearning
import com.example.algorithms.screens.sorting.simulation.insertion_sorting.InsertionSortingSimulation
import com.example.algorithms.screens.sorting.simulation.selection_sorting.SelectionSortingSimulation
import com.example.algorithms.screens.sorting.learning.quick_sorting.QuickSortingLearning


@Composable
fun BubbleSortingVisualizationScreen(navController: NavHostController) {
    AlgorithmScreen(
        title = "Сортировка пузырьком",
        navController = navController
    ) {
        BubbleSortingSimulation()
    }
}

@Composable
fun BubbleSortingLearningScreen(navController: NavHostController) {
    AlgorithmScreen(
        title = "Сортировка пузырьком",
        navController = navController
    ) {
        BubbleSortingLearning()
    }
}

@Composable
fun SortingSelectionLearningScreen(navController: NavHostController) {
    AlgorithmScreen(
        title = "Сортировка выбором",
        navController = navController
    ) {
        SelectionSortingLearning()
    }
}

@Composable
fun SelectionSortingVisualizationScreen(navController: NavHostController) {
    AlgorithmScreen(
        title = "Сортировка выбором",
        navController = navController
    ) {
        SelectionSortingSimulation()
    }
}

@Composable
fun InsertionSortingLearningScreen(navController: NavHostController) {
    AlgorithmScreen(
        title = "Сортировка вставками",
        navController = navController
    ) {
        InsertionSortingLearning()
    }
}

@Composable
fun InsertionSortingVisualizationScreen(navController: NavHostController) {
    AlgorithmScreen(
        title = "Сортировка вставками",
        navController = navController
    ) {
        InsertionSortingSimulation()
    }
}

@Composable
fun QuickSortingLearningScreen(navController: NavHostController) {
    AlgorithmScreen(
        title = "Быстрая сортировка",
        navController = navController
    ) {
        QuickSortingLearning()
    }
}

