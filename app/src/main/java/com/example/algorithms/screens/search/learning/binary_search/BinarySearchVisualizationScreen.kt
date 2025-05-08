package com.example.algorithms.screens.search.learning.binary_search

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.algorithms.R
import com.example.algorithms.screens.algorithm_screen.AlgorithmScreen
import com.example.algorithms.viewmodels.step_search.BinarySearchViewModel


@Composable
fun BinarySearchVisualizationScreen(navController: NavHostController) {
    val viewModel: BinarySearchViewModel = viewModel()
    val currentStep = viewModel.currentStep.value
    val mid = viewModel.mid.value
    val disabledIndices = viewModel.disabledIndices
    val explanation = viewModel.explanation.value
    val stepIndex = viewModel.stepIndex.value

    AlgorithmScreen(
        title = "Бинарный поиск",
        navController = navController
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            StepCard(stepIndex = stepIndex, explanation = explanation)

            Spacer(modifier = Modifier.height(38.dp))

            VisualizerSection(
                data = viewModel.data,
                mid = mid,
                disabledIndices = disabledIndices,
                currentStep = currentStep,
                target = viewModel.target
            )

            Spacer(modifier = Modifier.height(104.dp))

            SearchInfoCard(target = viewModel.target)

            Spacer(modifier = Modifier.height(12.dp))

            ControlPanel(viewModel = viewModel)
        }
    }
}

@Composable
fun StepCard(stepIndex: Int, explanation: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp, horizontal = 16.dp)) {
            Text(
                text = "Шаг $stepIndex",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = Color(0xFF0D47A1)
            )
            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 2.dp),
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


@Composable
fun VisualizerSection(
    data: List<Int>,
    mid: Int,
    disabledIndices: List<Int>,
    currentStep: BinarySearchViewModel.SearchStep,
    target: Int
) {
    BinarySearchVisualizer(
        data = data,
        mid = mid,
        disabledIndices = disabledIndices,
        currentStep = currentStep,
        target = target
    )
}



@Composable
fun SearchInfoCard(target: Int) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Ищем элемент: $target",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = Color(0xFF0D47A1)
            )
        }
    }
}



@Composable
fun ControlPanel(viewModel: BinarySearchViewModel) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Button(
            onClick = { viewModel.goToStart() },
            modifier = Modifier.weight(1f).height(48.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF90CAF9),
                contentColor = Color(0xFF0D47A1)
            )
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_on_the_beginning),
                contentDescription = null,
                modifier = Modifier.size(24.dp)
            )
        }
        Spacer(modifier = Modifier.width(4.dp))
        Button(
            onClick = { viewModel.goToPreviousStep() },
            modifier = Modifier.weight(1f).height(48.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF90CAF9),
                contentColor = Color(0xFF0D47A1)
            )
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_to_the_previous),
                contentDescription = null,
                modifier = Modifier.size(24.dp)
            )
        }
        Spacer(modifier = Modifier.width(4.dp))
        Button(
            onClick = { viewModel.goToNextStep() },
            enabled = when (viewModel.currentStep.value) {
                is BinarySearchViewModel.SearchStep.Found,
                is BinarySearchViewModel.SearchStep.NotFound -> false
                else -> true
            },
            modifier = Modifier.weight(1f).height(48.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF90CAF9),
                contentColor = Color(0xFF0D47A1)
            )
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_to_the_next),
                contentDescription = null,
                modifier = Modifier.size(24.dp)
            )
        }
        Spacer(modifier = Modifier.width(4.dp))
        Button(
            onClick = { viewModel.goToEnd() },
            modifier = Modifier.weight(1f).height(48.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF90CAF9),
                contentColor = Color(0xFF0D47A1)
            )
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_on_the_ending),
                contentDescription = null,
                modifier = Modifier.size(24.dp)
            )
        }
    }

    Spacer(modifier = Modifier.height(12.dp))

    Button(
        onClick = { viewModel.toggleAutoMode() },
        modifier = Modifier.fillMaxWidth().height(48.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (viewModel.isAuto) Color(0xFFFFC107) else Color(0xFF90CAF9),
            contentColor = if (viewModel.isAuto) Color.Black else Color(0xFF0D47A1)
        )
    ) {
        Text(if (viewModel.isAuto) "Пауза" else "Авто")
    }
}

@Composable
fun BinarySearchVisualizer(
    data: List<Int>,
    mid: Int,
    disabledIndices: List<Int>,
    currentStep: BinarySearchViewModel.SearchStep,
    target: Int
) {
    val transition = rememberInfiniteTransition(label = "")
    val arrowOffset by transition.animateFloat(
        initialValue = -10f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ), label = ""
    )

    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            data.forEachIndexed { index, value ->
                val isDisabled = disabledIndices.contains(index)
                val isCurrent = when (currentStep) {
                    is BinarySearchViewModel.SearchStep.CalculateMid -> index == mid
                    is BinarySearchViewModel.SearchStep.CompareValues -> index == mid
                    else -> false
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    if (isCurrent && currentStep is BinarySearchViewModel.SearchStep.CalculateMid) {
                        Icon(
                            painter = painterResource(R.drawable.ic_down_arrow),
                            contentDescription = "Current",
                            modifier = Modifier
                                .size(24.dp)
                                .offset(y = arrowOffset.dp),
                            tint = Color(0xFFEF6C00)
                        )
                    } else {
                        Spacer(modifier = Modifier.height(24.dp))
                    }


                    if (isCurrent && currentStep is BinarySearchViewModel.SearchStep.CompareValues) {
                        Text(
                            text = when {
                                value == target -> "=="
                                value < target -> "<"
                                else -> ">"
                            },
                            color = Color(0xFF0D47A1),
                            fontWeight = FontWeight.Bold
                        )

                    } else {
                        Spacer(modifier = Modifier.height(40.dp))
                    }


                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .background(
                                when {
                                    value == target && currentStep is BinarySearchViewModel.SearchStep.Found -> Color(0xFF66BB6A)
                                    isCurrent -> Color(0xFFFFEB3B)
                                    isDisabled -> Color(0xFF9E9E9E)
                                    else -> Color(0xFFE0E0E0)
                                }

                            )
                            .border(
                                width = 1.dp,
                                color = Color.Gray,
                                shape = RoundedCornerShape(6.dp)
                            )
                        ,
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
    }
}
