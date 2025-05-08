package com.example.algorithms.screens.math

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cached
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.algorithms.R
import com.example.algorithms.screens.algorithm_screen.AlgorithmScreen
import com.example.algorithms.utils.toFibName
import com.example.algorithms.viewmodels.step_math.FibonacciNode
import com.example.algorithms.viewmodels.step_math.FibonacciStepViewModel
import com.example.algorithms.viewmodels.step_math.FibonacciVisualState

@Composable
fun FibonacciLearning(navController: NavHostController, userToken: String) {
    val viewModel: FibonacciStepViewModel = viewModel()
    val stepIndex by viewModel.currentStepIndex
    val currentStep = viewModel.steps[stepIndex]

    LaunchedEffect(Unit) {
        viewModel.setToken(userToken)
    }

    AlgorithmScreen(title = "Числа Фибоначчи", navController = navController) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFD1E7FF))
                .padding(horizontal = 16.dp, vertical = 4.dp)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {

                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                ) {
                    Text(
                        text = currentStep.explanation,
                        modifier = Modifier.padding(16.dp),
                        fontSize = 16.sp,
                        color = Color.DarkGray
                    )
                }

                val scrollState = rememberScrollState()
                LaunchedEffect(currentStep.visual.tree.size) {
                    scrollState.animateScrollTo(scrollState.maxValue)
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color(0x26FFFFFF))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(scrollState)
                            .padding(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        FibonacciTreeView(currentStep.visual)
                    }
                }
                FibonacciControlPanel(viewModel)
            }
        }
    }
}

@Composable
fun FibonacciControlPanel(viewModel: FibonacciStepViewModel) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = { viewModel.reset() },
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
                onClick = { viewModel.previousStep() },
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
                onClick = { viewModel.nextStep() },
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
        }
    }
}


@Composable
fun FibonacciTreeView(state: FibonacciVisualState) {
    if (state.tree.isEmpty()) {
        Text(
            text = "Нет визуализации на этом шаге.",
            color = Color.Gray)
        return
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        state.tree.forEach { node ->
            FibonacciNodeView(node)
        }
    }
}


@Composable
fun FibonacciNodeView(node: FibonacciNode) {
    val bgColor = when {
        node.cached -> Color(0xFFB3E5FC)
        node.calculated -> Color(0xFFC8E6C9)
        node.highlighted -> Color(0xFFFFF176)
        else -> Color(0xFFF5F5F5)
    }

    val textColor = if (node.calculated) Color(0xFF2E7D32) else Color.Black

    Card(
        colors = CardDefaults.cardColors(containerColor = bgColor),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = node.id.toFibName(),
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = textColor
                )

                if (node.calculated) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Вычислен",
                        tint = Color(0xFF4CAF50)
                    )
                } else if (node.cached) {
                    Icon(
                        imageVector = Icons.Default.Cached,
                        contentDescription = "Кэширован",
                        tint = Color(0xFF2196F3)
                    )
                }
            }

            if (node.value != null && node.calculated) {
                Text(
                    text = "Результат: ${node.value}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = textColor,
                    modifier = Modifier.padding(top = 6.dp)
                )
            }

            if (node.children.isNotEmpty()) {
                Text(
                    text = "Состоит из: ${
                        node.children.map { it.toFibName() }.joinToString(" + ")
                    }",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}
