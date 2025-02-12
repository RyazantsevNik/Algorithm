package com.example.algorithms.screens.sorting.simulation.bubble_sorting

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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.algorithms.screens.sorting.simulation.CustomButton
import com.example.algorithms.viewmodels.BubbleSortViewModel
import org.koin.androidx.compose.getViewModel

@Composable
fun BubbleSortingSimulation(viewModel: BubbleSortViewModel = getViewModel()) {
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
        GraphicAnimation(viewModel)

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
