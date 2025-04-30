package com.example.algorithms.navigation

import InfoScreen
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.algorithms.screens.auth.AuthScreen
import com.example.algorithms.screens.chat.ChatScreen
import com.example.algorithms.screens.graphs.BfsSearchScreen
import com.example.algorithms.screens.graphs.DfsSearchScreen
import com.example.algorithms.screens.graphs.simulation.BfsGraphSimulationScreen
import com.example.algorithms.screens.graphs.simulation.DfsGraphSimulationScreen
import com.example.algorithms.screens.selection.AlgorithmSelectionScreen
import com.example.algorithms.screens.menu.AlgorithmsMenuScreen
import com.example.algorithms.screens.menu.HomeScreen
import com.example.algorithms.screens.profile.HelpScreen
import com.example.algorithms.screens.profile.ProfileScreen
import com.example.algorithms.screens.search.learning.binary_search.BinarySearchLearning
import com.example.algorithms.screens.search.learning.linear_search.LinearSearchLearning
import com.example.algorithms.screens.search.simulation.BinarySearchSimulationScreen
import com.example.algorithms.screens.search.simulation.LinearSearchSimulationScreen
import com.example.algorithms.screens.sorting.BubbleSortingVisualizationScreen
import com.example.algorithms.screens.selection.PracticeScreen
import com.example.algorithms.screens.selection.TheoryScreen
import com.example.algorithms.screens.sorting.BubbleSortingLearningScreen
import com.example.algorithms.screens.sorting.InsertionSortingLearningScreen
import com.example.algorithms.screens.sorting.InsertionSortingVisualizationScreen
import com.example.algorithms.screens.sorting.QuickSortingLearningScreen
import com.example.algorithms.screens.sorting.SelectionSortingVisualizationScreen
import com.example.algorithms.screens.sorting.SortingSelectionLearningScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = { BottomBar(navController) }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = AppRoutes.HOME,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            composable(AppRoutes.HOME) {
                HomeScreen(navController)
            }

            composable(AppRoutes.ALGORITHMS) {
                AlgorithmsMenuScreen(navController)
            }


            composable(AppRoutes.AI_CHAT) {
                ChatScreen(navController)
            }


            composable(AppRoutes.PROFILE) {
                ProfileScreen(navController = navController)
            }

            composable(AppRoutes.INFO) {
                InfoScreen(onBackClick = { navController.popBackStack() })
            }

            composable(AppRoutes.AUTH_SCREEN) {
                AuthScreen(
                    navController = navController
                )
            }

            composable(AppRoutes.HELP_SCREEN) {
                HelpScreen(navController = navController)
            }

            // Экран выбора теории/практики
            composable(
                route = AppRoutes.ALGORITHM_SELECTION,
                arguments = listOf(navArgument("algorithmTitle") { type = NavType.StringType })
            ) { backStackEntry ->
                val algorithmTitle =
                    backStackEntry.arguments?.getString("algorithmTitle") ?: "Алгоритм"
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
                val algorithmTitle =
                    backStackEntry.arguments?.getString("algorithmTitle") ?: "Алгоритм"
                when (algorithmTitle) {
                    "Сортировка пузырьком" -> BubbleSortingLearningScreen(navController)
                    "Сортировка выбором" -> SortingSelectionLearningScreen(navController)
                    "Сортировка вставками" -> InsertionSortingLearningScreen(navController)
                    "Быстрая сортировка" -> QuickSortingLearningScreen(navController)
                    "Линейный поиск" -> LinearSearchLearning(navController)
                    "Бинарный поиск" -> BinarySearchLearning(navController)
                    "Обход в глубину" -> DfsSearchScreen(navController)
                    "Обход в ширину" -> BfsSearchScreen(navController)
                    // TODO Добавьте другие теории алгоритмов здесь

                    else -> TheoryScreen(navController, algorithmTitle)
                }
            }

            // Экран практики
            composable(
                route = AppRoutes.PRACTISE_SCREEN,
                arguments = listOf(navArgument("algorithmTitle") { type = NavType.StringType })
            ) { backStackEntry ->
                val algorithmTitle =
                    backStackEntry.arguments?.getString("algorithmTitle") ?: "Алгоритм"
                when (algorithmTitle) {
                    "Сортировка пузырьком" -> BubbleSortingVisualizationScreen(navController)
                    "Сортировка выбором" -> SelectionSortingVisualizationScreen(navController)
                    "Сортировка вставками" -> InsertionSortingVisualizationScreen(navController)
                    "Линейный поиск" -> LinearSearchSimulationScreen(navController)
                    "Бинарный поиск" -> BinarySearchSimulationScreen(navController)
                    "Обход в ширину" -> BfsGraphSimulationScreen(navController)
                    "Обход в глубину" -> DfsGraphSimulationScreen(navController)
                    // TODO Добавьте другие практики алгоритмов здесь

                    else -> PracticeScreen(navController, algorithmTitle)
                }
            }
        }
    }
}