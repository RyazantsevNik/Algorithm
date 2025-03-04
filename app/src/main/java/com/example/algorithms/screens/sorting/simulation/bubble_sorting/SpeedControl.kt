package com.example.algorithms.screens.sorting.simulation.bubble_sorting

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.algorithms.viewmodels.base_class_for_simulation.SortingViewModel

@Composable
fun SpeedControl(viewModel: SortingViewModel) {
    val state by viewModel.state.collectAsState()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        // Новый слайдер
        CustomSlider(
            value = (1050f - state.delayTime) / 1000f,
            onValueChange = { newValue ->
                viewModel.updateDelayTime((1050L - (newValue * 1000).toLong()))
            },
            valueRange = 0f..1f,
            steps = 9,
            thumbDisplay = { value ->
                val fixedValues = listOf(5, 10, 20, 30, 40, 50, 60, 70, 80, 90, 100)
                val index = (value * fixedValues.size).toInt().coerceIn(0, fixedValues.size - 1)
                if (index == 10) "100" else "${fixedValues[index]}%"
            }
        )
    }
}