package com.example.algorithms.screens.search.learning.linear_search

import android.annotation.SuppressLint
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
import com.example.algorithms.screens.search.learning.binary_search.SearchInfoCard
import com.example.algorithms.screens.search.learning.binary_search.StepCard
import com.example.algorithms.viewmodels.step_search.BinarySearchViewModel
import com.example.algorithms.viewmodels.step_search.LinearSearchViewModel


@SuppressLint("SuspiciousIndentation")
@Composable
fun LinearSearchVisualizationScreen(navController: NavHostController) {
    val viewModel: LinearSearchViewModel = viewModel()
    val stepIndex by viewModel.stepIndex
    val explanation by viewModel.explanation
    val currentIndex by viewModel.currentIndex

    AlgorithmScreen(
        title = "Линейный поиск",
        navController = navController
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {

            StepCard(stepIndex = stepIndex, explanation = explanation)

            Spacer(modifier = Modifier.height(86.dp))


            LinearSearchVisualizer(
                data = viewModel.data,
                currentIndex = currentIndex,
                target = viewModel.target
            )

            Spacer(modifier = Modifier.height(104.dp))

            SearchInfoCard(target = viewModel.target)

            Spacer(modifier = Modifier.height(12.dp))

            LinearControlPanel(viewModel = viewModel)
        }
    }
}

@Composable
fun LinearControlPanel(viewModel: LinearSearchViewModel) {

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Button(
            onClick = { viewModel.goToStart() },
            modifier = Modifier
                .weight(1f)
                .height(48.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF90CAF9),
                contentColor = Color(0xFF0D47A1)
            )
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_on_the_beginning),
                contentDescription = null,
                modifier = Modifier.size(24.dp),
            )
        }
        Spacer(modifier = Modifier.width(4.dp))
        Button(
            onClick = { viewModel.goToPreviousStep() },
            modifier = Modifier
                .weight(1f)
                .height(48.dp),
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
            modifier = Modifier
                .weight(1f)
                .height(48.dp),
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
            modifier = Modifier
                .weight(1f)
                .height(48.dp),
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
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp),
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
fun LinearSearchVisualizer(
    data: List<Int>,
    currentIndex: Int,
    target: Int
) {
    val transition = rememberInfiniteTransition(label = "highlight")
    val offsetY by transition.animateFloat(
        initialValue = -10f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ), label = "offsetY"
    )

    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            data.forEachIndexed { index, value ->
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    if (index == currentIndex) {

                        Text(
                            text = target.toString(),
                            color = Color(0xFFEF6C00),
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.offset(y = offsetY.dp)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                    } else {
                        Spacer(modifier = Modifier.height(24.dp))
                    }

                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .background(
                                when {
                                    index == currentIndex && value == target -> Color(0xFF66BB6A)
                                    index == currentIndex -> Color(0xFFFFEB3B)
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
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                            color = if (index == currentIndex) Color.Black else Color.DarkGray
                        )
                    }
                }
            }
        }
    }
}
