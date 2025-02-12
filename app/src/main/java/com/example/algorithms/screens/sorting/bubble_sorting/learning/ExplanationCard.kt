package com.example.algorithms.screens.sorting.bubble_sorting.learning

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.algorithms.data.BubbleSortStep
import com.example.algorithms.data.SelectionSortStep
import com.example.algorithms.data.SortStep

@Composable
fun ExplanationCard(stepIndex: Int, steps: List<SortStep>) {
    val explanation = when (val step = steps[stepIndex]) {
        is BubbleSortStep -> {
            when {
                stepIndex == 0 ->
                    "Принцип работы: последовательно сравниваем значения соседних элементов и меняем числа местами," +
                            " если предыдущее оказывается больше последующего."
                stepIndex == steps.lastIndex ->
                    "Массив отсортирован!"
                step.comparedIndices == null -> {
                    if (step.sortedBoundary < step.array.size)
                        "Закончился проход по неотсортированной части. Теперь начинается новый проход для оставшихся элементов."
                    else
                        "Массив отсортирован!"
                }
                else -> {
                    val (i1, i2) = step.comparedIndices!!
                    val prevArray = steps.getOrNull(stepIndex - 1)?.array ?: step.array
                    if (prevArray != step.array)
                        "Обмен: элементы ${prevArray[i1]} и ${prevArray[i2]} поменялись местами."
                    else
                        "Сравниваем элементы ${step.array[i1]} и ${step.array[i2]}."
                }
            }
        }
        is SelectionSortStep -> {
            when {
                stepIndex == 0 ->
                    "Принцип работы: находим наименьший элемент из неотсортированной части массива и меняем его местами" +
                            " с первым элементом этой части."
                stepIndex == steps.lastIndex ->
                    "Массив отсортирован!"
                step.selectedIndices == null -> {
                    if (step.sortedBoundary < step.array.size)
                        "Минимальный элемент найден и помещён на своё место. Начинается новый поиск в оставшейся части массива."
                    else
                        "Массив отсортирован!"
                }
                else -> {
                    val (i1, i2) = step.selectedIndices!!
                    val prevArray = steps.getOrNull(stepIndex - 1)?.array ?: step.array
                    if (prevArray != step.array)
                        "Обмен: элементы ${prevArray[i1]} и ${prevArray[i2]} поменялись местами."
                    else
                        "Сравниваем элементы ${step.array[i1]} и ${step.array[i2]} для поиска минимального."
                }
            }
        }
        else -> "Неизвестный шаг."
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Шаг $stepIndex",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = Color(0xFF0D47A1)
            )
            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                thickness = 1.dp,
                color = Color(0xFF0D47A1)
            )
            Text(
                text = explanation,
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF455A64)
            )
        }
    }
}