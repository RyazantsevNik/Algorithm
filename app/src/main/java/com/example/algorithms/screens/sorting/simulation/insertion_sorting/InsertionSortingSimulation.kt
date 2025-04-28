package com.example.algorithms.screens.sorting.simulation.insertion_sorting

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.algorithms.viewmodels.simulation_sorting.InsertionSortViewModel
import org.koin.androidx.compose.getViewModel
import android.graphics.Paint
import android.graphics.Typeface
import android.util.Log
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
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.algorithms.screens.sorting.simulation.CustomButton
import com.example.algorithms.screens.sorting.simulation.bubble_sorting.CardMarking
import com.example.algorithms.screens.sorting.simulation.bubble_sorting.CustomProgressBar
import com.example.algorithms.screens.sorting.simulation.bubble_sorting.SpeedControl
import com.example.algorithms.viewmodels.simulation_sorting.base_class_for_simulation.SortingViewModel

@Composable
fun InsertionSortingSimulation(viewModel: InsertionSortViewModel = getViewModel()) {
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
                        Color(0xFFE3F2FD),
                        Color(0xFFFFFFFF)
                    )
                )
            )
            .padding(top = 8.dp, start = 8.dp, end = 8.dp)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 4.dp),
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



        GraphicAnimationForInsertionSort(viewModel)

        Spacer(modifier = Modifier.height(16.dp))

        SpeedControl(viewModel)
        CardMarking(viewModel,3)
        CustomProgressBar(progress = ((state.i.toFloat() / (state.list.size - 1).coerceAtLeast(1)) * 100).toInt())

        Spacer(modifier = Modifier.height(8.dp))


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
                    Log.d("index.value","keyvalue = ${state.keyValue} keyIndex = ${state.keyIndex}")
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
fun GraphicAnimationForInsertionSort(viewModel: SortingViewModel) {
    val state by viewModel.state.collectAsState()

    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
            .padding(top = 8.dp)
    ) {
        val barWidth = size.width / state.list.size
        val maxBarHeight = size.height - 40

        if (viewModel.animatedOffsets.size == state.list.size) {
            state.list.forEachIndexed { index, value ->
                val color = when {

                    !state.isRunning && state.i == 0 -> Color(0xFFBB86FC)


                    state.i >= state.list.size -> Color(0xFF03DAC5)


                    index == state.i && state.i == state.list.size - 1 && !state.isRunning -> Color(0xFF03DAC5)


                    state.keyIndex == index -> return@forEachIndexed


                    index == state.i && state.i < state.list.size -> Color.Red
                    index == state.currentComparisonIndex -> Color.Blue


                    index < state.i && index != state.currentComparisonIndex && index != state.i -> Color(0xFF03DAC5)


                    else -> Color(0xFFBB86FC)
                }

                val height = (minOf(value, 25) / 25f) * maxBarHeight
                val offsetX = viewModel.animatedOffsets[index].value * barWidth
                val barX = index * barWidth + offsetX
                val barY = size.height - height

                drawBar(barX, barY, barWidth, height, value, color)
            }


            val keyIndex = state.keyIndex
            val keyValue = state.keyValue
            if (keyIndex != null && keyValue != null) {
                val height = (minOf(keyValue, 25) / 25f) * maxBarHeight


                val offsetX = viewModel.animatedOffsets[keyIndex].value * barWidth
                val barX = keyIndex * barWidth + offsetX
                val barY = size.height - height

                drawBar(barX, barY, barWidth, height, keyValue, Color.Yellow)
            }
        }
    }
}


fun DrawScope.drawBar(
    x: Float,
    y: Float,
    width: Float,
    height: Float,
    value: Int,
    color: Color
) {
    drawRoundRect(
        color = Color.Black.copy(alpha = 0.2f),
        topLeft = Offset(x + 4, y + 4),
        size = Size(width, height),
        cornerRadius = CornerRadius(8f, 8f)
    )

    drawRoundRect(
        brush = Brush.verticalGradient(
            colors = listOf(color.copy(alpha = 0.8f), color)
        ),
        topLeft = Offset(x, y),
        size = Size(width - 8, height),
        cornerRadius = CornerRadius(8f, 8f)
    )

    drawContext.canvas.nativeCanvas.apply {
        drawText(
            value.toString(),
            x + (width - 8) / 2,
            y - 10,
            Paint().apply {
                setColor(android.graphics.Color.BLACK)
                textSize = 32f
                textAlign = Paint.Align.CENTER
                typeface = Typeface.DEFAULT_BOLD
            }
        )
    }
}