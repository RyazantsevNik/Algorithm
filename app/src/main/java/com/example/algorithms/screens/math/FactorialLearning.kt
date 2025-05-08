package com.example.algorithms.screens.math

import android.util.Log
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.algorithms.R
import com.example.algorithms.screens.algorithm_screen.AlgorithmScreen
import com.example.algorithms.ui.theme.BackgroundBottom
import com.example.algorithms.ui.theme.BackgroundTop
import com.example.algorithms.viewmodels.step_math.FactorialStepViewModel


@Composable
fun FactorialLearning(navController: NavHostController, userToken: String) {
    val viewModel: FactorialStepViewModel = viewModel()
    val steps = viewModel.visibleSteps
    val explanation = viewModel.currentExplanation

    LaunchedEffect(Unit) {
        Log.d("FactorialLearning", "Setting token: ${userToken.take(10)}...")
        viewModel.setToken(userToken)
        viewModel.generateFactorialSteps(5)
    }

    AlgorithmScreen(title = "Факториал", navController = navController) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.verticalGradient(listOf(BackgroundTop, BackgroundBottom)))
                .padding(horizontal = 16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                // Всегда видимая верхняя карточка
                AnimatedContent(
                    targetState = explanation.value,
                    transitionSpec = {
                        slideInVertically { height -> height } + fadeIn() togetherWith
                                slideOutVertically { height -> -height } + fadeOut()
                    },
                    label = "ExplanationTransition"
                ) { text ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        elevation = CardDefaults.cardElevation(4.dp),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Text(
                            text = text,
                            modifier = Modifier.padding(16.dp),
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color(0xFF455A64),
                            fontSize = 18.sp
                        )
                    }
                }

                // Скроллируемая визуализация с авто-прокруткой
                val scrollState = rememberScrollState()
                LaunchedEffect(steps.size) {
                    scrollState.animateScrollTo(scrollState.maxValue)
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color(0x26FFFFFF))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(scrollState)
                            .padding(8.dp)
                    ) {
                        steps.forEach { step ->
                            val bgColor = when {
                                step.value == 0 -> Color(0xFFFFF176)
                                step.result == null -> Color(0xFF81C784)
                                else -> Color(0xFF4FC3F7)
                            }

                            val content = when {
                                step.result == null -> "${step.value}! = ${step.value} × ${step.value - 1}!"
                                step.value == 0 -> "0! = 1"
                                else -> {
                                    val prev = step.result / step.value
                                    "${step.value}! = ${step.value} × ${step.value - 1}! = ${step.value} × $prev = ${step.result}"
                                }
                            }

                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                                    .padding(start = (step.level * 20).dp),
                                colors = CardDefaults.cardColors(containerColor = bgColor),
                                elevation = CardDefaults.cardElevation(6.dp),
                                shape = RoundedCornerShape(16.dp)
                            ) {
                                Text(
                                    text = content,
                                    modifier = Modifier.padding(16.dp),
                                    color = Color.Black,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                }

                // Всегда видимая нижняя панель
                FactorialControlPanel(viewModel)
            }
        }
    }
}



@Composable
fun FactorialControlPanel(viewModel: FactorialStepViewModel) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
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
                onClick = { viewModel.prevStep() },
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

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = { viewModel.toggleAutoMode() },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (viewModel.isAutoMode.value) Color(0xFFFFC107) else Color(0xFF90CAF9),
                contentColor = if (viewModel.isAutoMode.value) Color.Black else Color(0xFF0D47A1)
            )
        ) {
            Text(if (viewModel.isAutoMode.value) "Пауза" else "Авто")
        }
    }
}