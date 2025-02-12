package com.example.algorithms.screens.sorting.bubble_sorting.learning

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.algorithms.viewmodels.BaseSortStepViewModel
import com.example.algorithms.viewmodels.BubbleSortStepViewModel
import com.example.algorithms.viewmodels.SelectionSortStepViewModel

@Composable
fun SortedElementsCard(viewModel: BaseSortStepViewModel) {
    // Рассчитываем sortedCount в зависимости от типа ViewModel
    val sortedCount = when (viewModel) {
        is BubbleSortStepViewModel ->
            viewModel.steps[viewModel.currentStepIndex].array.size -
                    viewModel.steps[viewModel.currentStepIndex].sortedBoundary
        is SelectionSortStepViewModel ->
            viewModel.steps[viewModel.currentStepIndex].sortedBoundary
        else -> 0
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Отсортировано элементов: $sortedCount",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = Color(0xFF0D47A1)
            )
        }
    }
}