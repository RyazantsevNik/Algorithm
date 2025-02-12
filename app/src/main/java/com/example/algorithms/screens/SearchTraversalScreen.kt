package com.example.algorithms.screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.example.algorithms.screens.alghoritm_screen.AlgorithmScreen

@Composable
fun SearchLinearScreen(navController: NavHostController) {
    AlgorithmScreen(
        title = "Линейный поиск",
        navController = navController
    ) {
        Text("Визуализация линейного поиска")
    }
}

@Composable
fun SearchBinaryScreen(navController: NavHostController) {
    AlgorithmScreen(
        title = "Бинарный поиск",
        navController = navController
    ) {
        Text("Визуализация бинарного поиска")
    }
}