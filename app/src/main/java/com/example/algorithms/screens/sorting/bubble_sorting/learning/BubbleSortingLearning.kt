package com.example.algorithms.screens.sorting.bubble_sorting.learning

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.algorithms.viewmodels.BubbleSortStepViewModel
import org.koin.androidx.compose.getViewModel

@Composable
fun BubbleSortingLearning(viewModel: BubbleSortStepViewModel = getViewModel()) {
    var showIntro by remember { mutableStateOf(true) }

    if (showIntro) {
        IntroScreen(
            algorithmTitle = "Сортировка пузырьком",
            principleContent = "Алгоритм проходит по массиву, сравнивая соседние элементы и меняя их местами, если они стоят в неправильном порядке.",
            passesContent = "После каждого прохода последний элемент оказывается на своём месте, и цикл повторяется для оставшихся элементов.",
            efficiencyContent = "Алгоритм имеет временную сложность O(n²), что делает его понятным для обучения, но неэффективным для больших массивов."
        ) { showIntro = false }
    } else {
        SortScreen(viewModel)
    }
}