package com.example.algorithms.screens.search.simulation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.algorithms.screens.algorithm_screen.AlgorithmScreen
import com.example.algorithms.screens.sorting.simulation.CustomButton
import com.example.algorithms.screens.sorting.simulation.bubble_sorting.CustomProgressBar
import com.example.algorithms.viewmodels.simulation_search.LinearSearchSimulationViewModel
import com.example.algorithms.viewmodels.simulation_search.SearchSimulationViewModel
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment


@Composable
fun LinearSearchSimulationScreen(navController: NavHostController) {
    val viewModel: LinearSearchSimulationViewModel = viewModel()

    AlgorithmScreen(
        title = "Линейный поиск",
        navController = navController
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Настройки
            SettingsSection(viewModel)

            Spacer(modifier = Modifier.height(16.dp))

            // Визуализация
            LinearSearchVisualizer(viewModel)

            Spacer(modifier = Modifier.height(16.dp))

            // Панель управления
            ControlPanel(viewModel)
        }
    }
}

@Composable
fun SettingsSection(viewModel: SearchSimulationViewModel) {
    val state by viewModel.state.collectAsState()

    var isDialogOpen by remember { mutableStateOf(false) }

    Column {
        // Кнопка "Изменить массив"
        CustomButton(
            text = "Изменить массив",
            onClick = { isDialogOpen = true },
            modifier = Modifier.fillMaxWidth(),
            backgroundColor = Color(0xFF64B5F6),
            textColor = Color.White
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Поле для ввода искомого числа
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            TextField(
                value = state.targetInput,
                onValueChange = {
                    if (it.length <= 4 && it.all { char -> char.isDigit() }) {
                        viewModel.setTarget(it)
                    }
                },
                label = { Text("Искомое число") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier
                    .width(90.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .border(1.dp, Color.Gray, RoundedCornerShape(10.dp))
                    .background(Color(0xFFF5F5F5))
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Диалоговое окно для изменения массива
        if (isDialogOpen) {
            AlertDialog(
                onDismissRequest = { isDialogOpen = false },
                title = { Text("Изменить массив") },
                text = {
                    TextField(
                        value = state.inputText,
                        onValueChange = {
                            viewModel.setInputText(it)
                        },
                        label = { Text("Массив (через запятую)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                confirmButton = {
                    CustomButton(
                        text = "Применить",
                        onClick = {
                            viewModel.applyInput()
                            isDialogOpen = false
                        },
                        backgroundColor = Color(0xFF64B5F6),
                        textColor = Color.White
                    )
                },
                dismissButton = {
                    CustomButton(
                        text = "Отмена",
                        onClick = { isDialogOpen = false },
                        backgroundColor = Color(0xFFE57373),
                        textColor = Color.White
                    )
                }
            )
        }
    }
}

@Composable
fun ControlPanel(viewModel: SearchSimulationViewModel) {
    val state by viewModel.state.collectAsState()

    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        if (state.targetInput.isNotBlank()) {
            CustomButton(
                text = if (state.isRunning) if (state.isPaused) "Продолжить" else "Пауза" else "Старт",
                onClick = {
                    if (!state.isRunning) {
                        viewModel.startSearch()
                    } else {
                        viewModel.togglePause()
                    }
                },
                modifier = Modifier.weight(1f),
                backgroundColor = if (state.isRunning) Color(0xFFFFC107) else Color(0xFF64B5F6),
                textColor = Color.White,
            )
        } else {
            CustomButton(
                text = "Введите искомое",
                onClick = {},
                modifier = Modifier.weight(1f),
                backgroundColor = Color.Red,
                textColor = Color.White,
            )
        }

        CustomButton(
            text = "Сброс",
            onClick = { viewModel.reset() },
            modifier = Modifier.weight(1f),
            backgroundColor = Color(0xFFE57373),
            textColor = Color.White
        )
    }
}

@Composable
fun LinearSearchVisualizer(viewModel: LinearSearchSimulationViewModel) {
    val state by viewModel.state.collectAsState()

    LazyVerticalGrid(
        columns = GridCells.Fixed(7),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        itemsIndexed(state.list) { index, value ->
            val isDisabled = index < state.currentIndex
            val isCurrent = index == state.currentIndex
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(
                        when {
                            value == state.targetInput.toIntOrNull() && isCurrent -> Color(0xFF66BB6A)
                            isCurrent -> Color(0xFFFFEB3B)
                            isDisabled -> Color(0xFF9E9E9E)
                            else -> Color(0xFFE0E0E0)
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = value.toString(),
                    fontWeight = FontWeight.Bold,
                    color = when {
                        isDisabled -> Color.White
                        isCurrent -> Color.Black
                        else -> Color.DarkGray
                    }
                )
            }
        }
    }
}