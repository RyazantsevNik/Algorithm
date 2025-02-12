package com.example.algorithms.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.algorithms.screens.selection.AlgorithmSelectionScreen
import com.example.algorithms.screens.MenuScreen
import com.example.algorithms.screens.sorting.BubbleSortingVisualizationScreen
import com.example.algorithms.screens.selection.PracticeScreen
import com.example.algorithms.screens.selection.TheoryScreen
import com.example.algorithms.screens.sorting.BubbleSortingLearningScreen
import com.example.algorithms.screens.sorting.SortingSelectionLearningScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = AppRoutes.MENU,
        modifier = Modifier.fillMaxSize()
    ) {
        composable(AppRoutes.MENU) {
            MenuScreen(navController)
        }

        // Экран выбора теории/практики
        composable(
            route = AppRoutes.ALGORITHM_SELECTION,
            arguments = listOf(navArgument("algorithmTitle") { type = NavType.StringType })
        ) { backStackEntry ->
            val algorithmTitle = backStackEntry.arguments?.getString("algorithmTitle") ?: "Алгоритм"
            AlgorithmSelectionScreen(
                algorithmTitle = algorithmTitle,
                navController = navController,
                onLearningClick = { title ->
                    navController.navigate(AppRoutes.theoryScreenRoute(title))
                },
                onSimulationClick = { title ->
                    navController.navigate(AppRoutes.practiceScreenRoute(title))
                }
            )
        }
        // Экран теории
        composable(
            route = AppRoutes.THEORY_SCREEN,
            arguments = listOf(navArgument("algorithmTitle") { type = NavType.StringType })
        ) { backStackEntry ->
            val algorithmTitle = backStackEntry.arguments?.getString("algorithmTitle") ?: "Алгоритм"
            when (algorithmTitle) {
                "Сортировка пузырьком" -> BubbleSortingLearningScreen(navController)
                "Сортировка выбором" -> SortingSelectionLearningScreen(navController)
                // TODO Добавьте другие теории алгоритмов здесь

                else -> TheoryScreen(navController, algorithmTitle)
            }
        }

        // Экран практики
        composable(
            route = AppRoutes.PRACTISE_SCREEN,
            arguments = listOf(navArgument("algorithmTitle") { type = NavType.StringType })
        ) { backStackEntry ->
            val algorithmTitle = backStackEntry.arguments?.getString("algorithmTitle") ?: "Алгоритм"
            when (algorithmTitle) {
                "Сортировка пузырьком" -> BubbleSortingVisualizationScreen(navController)
                // TODO Добавьте другие практики алгоритмов здесь

                else -> PracticeScreen(navController, algorithmTitle)
            }
        }
    }
}