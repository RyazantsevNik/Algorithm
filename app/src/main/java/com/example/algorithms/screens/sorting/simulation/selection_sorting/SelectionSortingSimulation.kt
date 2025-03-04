package com.example.algorithms.screens.sorting.simulation.selection_sorting

import android.graphics.Paint
import android.graphics.Typeface
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.algorithms.screens.sorting.simulation.CustomButton
import com.example.algorithms.screens.sorting.simulation.bubble_sorting.CardMarking
import com.example.algorithms.screens.sorting.simulation.bubble_sorting.CustomProgressBar
import com.example.algorithms.screens.sorting.simulation.bubble_sorting.GraphicAnimation
import com.example.algorithms.screens.sorting.simulation.bubble_sorting.SpeedControl
import com.example.algorithms.viewmodels.SelectionSortViewModel
import com.example.algorithms.viewmodels.base_class_for_simulation.SortingViewModel
import org.koin.androidx.compose.getViewModel

@Composable
fun SelectionSortingSimulation(viewModel: SelectionSortViewModel = getViewModel()){
    val state by viewModel.state.collectAsState()

    LaunchedEffect(state.list) {
        viewModel.updateAnimatedOffsets(state.list.size)
    }

    LaunchedEffect(state.isRunning, state.isPaused) {
        viewModel.runSorting()
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFE3F2FD), // Светлый голубой (верх)
                        Color(0xFFFFFFFF)   // Белый (низ)
                    )
                )
            )
            .padding(top = 8.dp, start = 8.dp, end = 8.dp)
    ) {

        // Управление размером массива
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            CustomButton(
                text = "-",
                onClick = { viewModel.updateArraySize(state.arraySize - 1) },
                modifier = Modifier.size(50.dp),
            )

            Text(
                text = state.arraySize.toString(),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            CustomButton(
                text = "+",
                onClick = { viewModel.updateArraySize(state.arraySize + 1) },
                modifier = Modifier.size(50.dp),
            )

            Spacer(modifier = Modifier.width(16.dp))

            CustomButton(
                text = "Изменить",
                onClick = { viewModel.toggleEditing() },
                modifier = Modifier
                    .height(50.dp)
                    .width(120.dp)
                    .padding(horizontal = 8.dp),
                textColor = Color.Black
            )
        }

        // Диалог редактирования массива
        if (state.isEditing) {
            AlertDialog(
                onDismissRequest = { viewModel.toggleEditing() },
                title = { Text("Введите числа через запятую") },
                text = {
                    OutlinedTextField(
                        value = state.inputText,
                        onValueChange = { newValue ->
                            if (newValue.all { it.isDigit() || it == ',' }) {
                                viewModel.setInputText(newValue)
                            }
                        },
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                confirmButton = {
                    TextButton(onClick = {
                        viewModel.setListFromInput()
                        viewModel.toggleEditing()
                        viewModel.resetIndexes()
                    }) {
                        Text("OK")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { viewModel.toggleEditing() }) {
                        Text("Отмена")
                    }
                }
            )
        }
        // Графическая анимация
        GraphicAnimationForSelectionSort(viewModel)

        Spacer(modifier = Modifier.height(16.dp))

        // Управление скоростью
        SpeedControl(viewModel)


        //Карточки шаг/сравнение
        CardMarking(viewModel)

        //Прогресс бар
        CustomProgressBar(progress = state.progress.toInt())

        Spacer(modifier = Modifier.height(8.dp))

        // Кнопки управления
        Row(
            modifier = Modifier
                .fillMaxWidth().fillMaxHeight()
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)
        ) {
            CustomButton(
                text = when {
                    state.i >= state.list.size - 1 -> "Повторить"
                    state.isRunning -> if (state.isPaused) "Продолжить" else "Пауза"
                    else -> "Старт"
                },
                onClick = {
                    when {
                        state.i >= state.list.size - 1 -> viewModel.repeatSorting()
                        state.isRunning -> viewModel.togglePause()
                        else -> viewModel.startSorting()
                    }
                },
                modifier = Modifier.weight(1f),
            )

            CustomButton(
                text = "Сброс",
                onClick = { viewModel.reset() },
                modifier = Modifier.weight(1f),
            )
        }
    }
}

@Composable
fun GraphicAnimationForSelectionSort(viewModel: SortingViewModel) {
    val state by viewModel.state.collectAsState()

    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
            .padding(vertical = 16.dp)
    ) {
        val barWidth = size.width / state.list.size
        val maxBarHeight = size.height - 40

        if (viewModel.animatedOffsets.size == state.list.size) {
            state.list.forEachIndexed { index, value ->
                val height = (minOf(value, 25) / 25f) * maxBarHeight

                val unsortedColor = Color(0xFFBB86FC)
                val sortedColor = Color(0xFF03DAC5)
                val currentElementColor = Color.Red
                val minElementColor = Color.Yellow
                val comparisonColor = Color.Blue

                val color = when {
                    state.i >= state.list.size - 1 -> sortedColor
                    index < state.i -> sortedColor
                    index == state.i -> currentElementColor
                    index == state.minIndex -> minElementColor
                    index == state.currentComparisonIndex -> comparisonColor
                    else -> unsortedColor
                }

                // Используем animatedOffsets только для горизонтального смещения
                val offsetX = viewModel.animatedOffsets[index].value * barWidth
                val barX = index * barWidth + offsetX
                val barY = size.height - height // Убираем вертикальное смещение

                // Тень для блока
                drawRoundRect(
                    color = Color.Black.copy(alpha = 0.2f),
                    topLeft = Offset(barX + 4, barY + 4),
                    size = Size(barWidth, height),
                    cornerRadius = CornerRadius(8f, 8f)
                )

                // Основной блок
                drawRoundRect(
                    brush = Brush.verticalGradient(
                        colors = listOf(color.copy(alpha = 0.8f), color)
                    ),
                    topLeft = Offset(barX, barY),
                    size = Size(barWidth - 8, height),
                    cornerRadius = CornerRadius(8f, 8f)
                )

                // Текст (значение блока)
                drawContext.canvas.nativeCanvas.apply {
                    drawText(
                        value.toString(),
                        barX + (barWidth - 8) / 2,
                        barY - 10,
                        Paint().apply {
                            setColor(android.graphics.Color.BLACK)
                            textSize = 32f
                            textAlign = Paint.Align.CENTER
                            typeface = Typeface.DEFAULT_BOLD
                        }
                    )
                }
            }
        }
    }
}