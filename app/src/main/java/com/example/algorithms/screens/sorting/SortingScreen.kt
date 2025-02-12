package com.example.algorithms.screens.sorting

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.example.algorithms.screens.alghoritm_screen.AlgorithmScreen
import com.example.algorithms.screens.sorting.bubble_sorting.learning.BubbleSortingLearning
import com.example.algorithms.screens.sorting.bubble_sorting.simulation.BubbleSortingSimulation
import com.example.algorithms.screens.sorting.seletion_sorting.learning.SelectionSortingLearning

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
fun SortingQuickScreen(navController: NavHostController) {
    AlgorithmScreen(
        title = "Быстрая сортировка",
        navController = navController
    ) {
        Text("Визуализация быстрой сортировки")
    }
}

