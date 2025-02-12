package com.example.algorithms.screens.sorting.seletion_sorting.learning

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.algorithms.screens.sorting.bubble_sorting.learning.IntroScreen
import com.example.algorithms.screens.sorting.bubble_sorting.learning.SortScreen
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
            efficiencyContent = "Алгоритм также имеет временную сложность O(n²), но выполняет меньше обменов, чем сортировка пузырьком."
        ) { showIntro = false }
    } else {
        SortScreen(viewModel)
    }
}