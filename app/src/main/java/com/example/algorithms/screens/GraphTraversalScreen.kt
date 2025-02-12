package com.example.algorithms.screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.example.algorithms.screens.alghoritm_screen.AlgorithmScreen

@Composable
fun GraphDFSScreen(navController: NavHostController) {
    AlgorithmScreen(
        title = "Обход в глубину",
        navController = navController
    ) {
        Text("Визуализация обхода в глубину")
    }
}

@Composable
fun GraphBFSScreen(navController: NavHostController) {
    AlgorithmScreen(
        title = "Обход в ширину",
        navController = navController
    ) {
        Text("Визуализация обхода в ширину")
    }
}