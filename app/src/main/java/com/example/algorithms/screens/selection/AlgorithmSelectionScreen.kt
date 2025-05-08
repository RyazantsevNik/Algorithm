package com.example.algorithms.screens.selection

import android.annotation.SuppressLint
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.algorithms.screens.algorithm_screen.AlgorithmScreen
import androidx.compose.runtime.*
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import com.example.algorithms.R
import kotlinx.coroutines.delay

@SuppressLint("UseOfNonLambdaOffsetOverload")
@Composable
fun AlgorithmSelectionScreen(
    algorithmTitle: String,
    navController: NavHostController,
    onLearningClick: (String) -> Unit,
    onSimulationClick: (String) -> Unit
) {
    AlgorithmScreen(
        title = algorithmTitle,
        navController = navController
    ) {
        Box(
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
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            var startAnimation by remember { mutableStateOf(false) }

            LaunchedEffect(Unit) {
                delay(300)
                startAnimation = true
            }

            val alpha by animateFloatAsState(
                targetValue = if (startAnimation) 1f else 0f,
                animationSpec = tween(durationMillis = 800),
                label = "AlphaAnimation"
            )
            val offsetY by animateDpAsState(
                targetValue = if (startAnimation) 0.dp else 50.dp,
                animationSpec = tween(durationMillis = 800),
                label = "OffsetAnimation"
            )

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Выберите раздел",
                    style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold),
                    color = Color(0xFF0D47A1),
                    modifier = Modifier
                        .padding(bottom = 20.dp)
                        .graphicsLayer { this.alpha = alpha }
                )
                Text(
                    text = "Изучите основы алгоритма или попробуйте его в действии!",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF455A64),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(bottom = 20.dp)
                        .graphicsLayer { this.alpha = alpha }
                )


                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    SectionCard(
                        title = "Обучение",
                        painter = painterResource(id = R.drawable.ic_learning_card),
                        backgroundColor = Color(0xFFBBDEFB),
                        contentColor = Color(0xFF0D47A1),
                        description = "Изучите теорию, примеры и шаги алгоритма",
                        onClick = { onLearningClick(algorithmTitle) },
                        modifier = Modifier
                            .weight(1f)
                            .offset(y = offsetY)
                            .shadow(6.dp, RoundedCornerShape(12.dp))
                    )
                    SectionCard(
                        title = "Симуляция",
                        painter = painterResource(id = R.drawable.ic_visualization_card),
                        backgroundColor = Color(0xFFC8E6C9),
                        contentColor = Color(0xFF1B5E20),
                        description = "Смоделируйте работу алгоритма на своих данных",
                        onClick = { onSimulationClick(algorithmTitle) },
                        modifier = Modifier
                            .weight(1f)
                            .offset(y = offsetY)
                            .shadow(6.dp, RoundedCornerShape(12.dp))
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Box(
                    modifier = Modifier
                        .graphicsLayer { this.alpha = alpha }
                ) {
                    InfoCard()
                }
            }
        }
    }
}