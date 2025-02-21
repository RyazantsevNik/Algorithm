package com.example.algorithms.screens.sorting.learning.quick_sorting

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.algorithms.screens.sorting.learning.IntroScreen
import com.example.algorithms.screens.sorting.learning.SortScreen
import com.example.algorithms.viewmodels.QuickSortStepViewModel
import org.koin.androidx.compose.getViewModel

@Composable
fun QuickSortingLearning(viewModel: QuickSortStepViewModel = getViewModel()) {
    var showIntro by remember { mutableStateOf(true) }
    
    if (showIntro) {
        IntroScreen(
            algorithmTitle = "Быстрая сортировка",
            principleContent = "Алгоритм выбирает опорный элемент и разделяет массив на две части: " +
                "элементы меньше опорного и элементы больше опорного. Затем рекурсивно сортирует эти части.",
            passesContent = "Каждый проход разбивает массив на подмассивы и устанавливает опорный " +
                "элемент на правильную позицию.",
            efficiencyContent = "Алгоритм имеет среднюю временную сложность O(n log n), " +
                "эффективен для больших наборов данных. В худшем случае O(n²)."
        ) { showIntro = false }
    } else {
        SortScreen(viewModel)
    }
} 