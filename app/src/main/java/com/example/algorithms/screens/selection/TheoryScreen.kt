package com.example.algorithms.screens.selection

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavHostController
import com.example.algorithms.screens.algorithm_screen.AlgorithmScreen

@Composable
fun TheoryScreen(navController: NavHostController, algorithmTitle: String) {
    AlgorithmScreen(
        title = algorithmTitle,
        navController = navController
    ) {
        Text(
            text = "Здесь будет теория про $algorithmTitle",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )
    }
}