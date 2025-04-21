package com.example.algorithms.screens.sorting.learning.insertion_sorting

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.algorithms.screens.sorting.learning.IntroScreen
import com.example.algorithms.screens.sorting.learning.SortScreen
import com.example.algorithms.viewmodels.step_sorting.InsertionSortStepViewModel
import org.koin.androidx.compose.getViewModel

@Composable
fun InsertionSortingLearning(viewModel: InsertionSortStepViewModel = getViewModel()) {
    var showIntro by remember { mutableStateOf(true) }
    if (showIntro) {
        IntroScreen(
            algorithmTitle = "Сортировка вставками",
            principleContent = "Алгоритм выбирает элемент из неотсортированной части массива и вставляет его в правильную позицию в отсортированной части.",
            passesContent = "На каждом шаге один элемент перемещается в отсортированную часть массива.",
            efficiencyContent = "Алгоритм имеет временную сложность O(n²), эффективен на небольших и почти упорядоченных массивах."
        ) { showIntro = false }
    } else {
        SortScreen(viewModel)
    }
}