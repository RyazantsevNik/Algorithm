package com.example.algorithms.screens.sorting.learning

import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.algorithms.R
import com.example.algorithms.viewmodels.step_sorting.BubbleSortStepViewModel
import com.example.algorithms.viewmodels.step_sorting.base_class_for_step.BaseSortStepViewModel

@Composable
fun SortScreen(viewModel: BaseSortStepViewModel) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE3F2FD))
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
            ) {
                ExplanationCard(
                    stepIndex = viewModel.currentStepIndex,
                    steps = viewModel.steps,
                )
            }
            Spacer(modifier = Modifier.height(8.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentAlignment = Alignment.Center
            ) {
                SortVisualization(
                    step = viewModel.steps[viewModel.currentStepIndex],
                )
            }

            SortedElementsCard(viewModel = viewModel)

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = { viewModel.goToPreviousStep() },
                    enabled = viewModel.currentStepIndex > 0,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF4A90E2),
                        contentColor = Color.White
                    )
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_to_the_previous),
                        contentDescription = "Previous",
                        modifier = Modifier.size(24.dp)
                    )
                }

                Button(
                    onClick = { viewModel.handleNextStep() },
                    enabled = viewModel.currentStepIndex < viewModel.steps.size - 1,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    Text("Следующий шаг")
                }
            }
        }

        LaunchedEffect(key1 = viewModel.isAuto, key2 = viewModel.currentStepIndex) {
            if (viewModel.isAuto && viewModel.currentStepIndex < viewModel.steps.size - 1) {
                kotlinx.coroutines.delay(800L)
                viewModel.goToNextStep()
            } else if (viewModel.currentStepIndex == viewModel.steps.size - 1) {
                viewModel.isAuto = false
            }
        }
    }
}