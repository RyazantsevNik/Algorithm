package com.example.algorithms.screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.example.algorithms.screens.alghoritm_screen.AlgorithmScreen

@Composable
fun TreeTraversalScreen(navController: NavHostController) {
    AlgorithmScreen(
        title = "Обход дерева",
        navController = navController
    ) {
        Text("Визуализация обхода дерева")
    }
}