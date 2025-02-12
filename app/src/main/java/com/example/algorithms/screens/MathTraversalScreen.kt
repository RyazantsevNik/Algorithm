package com.example.algorithms.screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.example.algorithms.screens.alghoritm_screen.AlgorithmScreen

@Composable
fun MathFactorialScreen(navController: NavHostController) {
    AlgorithmScreen(
        title = "Факториал",
        navController = navController
    ) {
        Text("Визуализация факториала")
    }
}

@Composable
fun MathFibonacciScreen(navController: NavHostController) {
    AlgorithmScreen(
        title = "Числа Фибоначчи",
        navController = navController
    ) {
        Text("Визуализация чисел Фибоначчи")
    }
}