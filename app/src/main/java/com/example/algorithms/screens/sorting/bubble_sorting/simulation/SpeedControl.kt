package com.example.algorithms.screens.sorting.bubble_sorting.simulation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.algorithms.viewmodels.BubbleSortViewModel

@Composable
fun SpeedControl(viewModel: BubbleSortViewModel) {
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
                val percentage = (value * 100).toInt()
                if (percentage == 100) "100" else "$percentage%"
            }
        )
    }
}