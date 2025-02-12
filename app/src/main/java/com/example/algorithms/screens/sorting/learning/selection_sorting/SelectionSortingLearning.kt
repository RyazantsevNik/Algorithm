package com.example.algorithms.screens.sorting.learning.selection_sorting

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.algorithms.screens.sorting.learning.IntroScreen
import com.example.algorithms.screens.sorting.learning.SortScreen
import com.example.algorithms.viewmodels.SelectionSortStepViewModel
import org.koin.androidx.compose.getViewModel

@Composable
fun SelectionSortingLearning(viewModel: SelectionSortStepViewModel = getViewModel()) {
    var showIntro by remember { mutableStateOf(true) }
    if (showIntro) {
        IntroScreen(
            algorithmTitle = "Сортировка выбором",
            principleContent = "Алгоритм ищет минимальный элемент в неотсортированной части массива и перемещает его в начало.",
            passesContent = "На каждом шаге один элемент помещается в отсортированную часть массива.",
            efficiencyContent = "Алгоритм имеет временную сложность O(n²), выполняет n-1 операций обменов, эффективен на небольших или частично отсортированных массивах данных."
        ) { showIntro = false }
    } else {
        SortScreen(viewModel)
    }
}