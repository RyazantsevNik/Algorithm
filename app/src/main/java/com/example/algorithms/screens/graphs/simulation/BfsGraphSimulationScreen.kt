package com.example.algorithms.screens.graphs.simulation

import android.graphics.Paint
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.algorithms.screens.algorithm_screen.AlgorithmScreen
import com.example.algorithms.screens.sorting.simulation.CustomButton
import com.example.algorithms.ui.theme.LightBlue
import com.example.algorithms.viewmodels.simulation_graphs.BfsGraphSimulationViewModel
import com.example.algorithms.viewmodels.simulation_graphs.NodeStatus
import com.example.algorithms.viewmodels.simulation_graphs.TreeNodeUi

@Composable
fun BfsGraphSimulationScreen(navController: NavHostController) {
    val viewModel: BfsGraphSimulationViewModel = viewModel()

    AlgorithmScreen(
        title = "Обход в ширину(BFS)",
        navController = navController
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 4.dp, start = 12.dp, end = 12.dp)
                .verticalScroll(rememberScrollState())
        ) {


            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 4.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                CustomButton(
                    "-",
                    onClick = { viewModel.decreaseNodeCount() },
                    modifier = Modifier.size(50.dp)
                )


                Text(
                    text = "${viewModel.totalNodes.value}",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )


                CustomButton(
                    "+",
                    onClick = { viewModel.increaseNodeCount() },
                    modifier = Modifier.size(50.dp)
                )
            }

            TreeGraphVisualizer(viewModel.nodes)



            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)
                ) {
                    InfoCard(
                        text = "Посещённых узлов",
                        value = "${viewModel.nodes.count { it.status == NodeStatus.Visited }}"
                    )
                    InfoCard(
                        text = "Текущий узел",
                        value = viewModel.nodes.find { it.status == NodeStatus.Current }?.value
                            ?: "Нет"
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                WideInfoCard(
                    text = "Кандидаты",
                    value = viewModel._queue.map { it.value }.joinToString(" -> ") { it }
                )

            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)
            ) {

                CustomButton(
                    text = when {
                        viewModel.isTraversalFinished.value -> "Повторить"
                        viewModel.isAutoRunning.value && viewModel.isPaused.value -> "Возобновить"
                        viewModel.isAutoRunning.value -> "Пауза"
                        else -> "Старт"
                    },
                    onClick = {
                        if (viewModel.isTraversalFinished.value) {
                            viewModel.repeatTraversal()
                        } else if (viewModel.isAutoRunning.value) {
                            viewModel.togglePause()
                        } else {
                            viewModel.startTraversal()
                        }
                    },
                    modifier = Modifier.weight(1f),
                )

                Spacer(modifier = Modifier.height(16.dp))

                CustomButton(
                    text = "Сброс",
                    onClick = { viewModel.resetTraversal() },
                    modifier = Modifier.weight(1f),
                )
            }
        }
    }
}

@Composable
fun InfoCard(
    text: String,
    value: String,
    color: Color = Color.Black
) {
    Card(
        modifier = Modifier
            .width(150.dp)
            .height(50.dp)
            .border(
                width = 1.dp,
                color = Color.Gray,
                shape = RoundedCornerShape(12.dp)
            ),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = LightBlue,
            contentColor = Color.DarkGray
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 6.dp, vertical = 4.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = text,
                fontSize = 10.sp,
                color = Color.DarkGray,
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = value,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = color,
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}


@Composable
fun WideInfoCard(text: String, value: String) {
    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        Card(
            modifier = Modifier
                .width(315.dp)
                .height(50.dp),
            colors = CardDefaults.cardColors(
                containerColor = LightBlue,
                contentColor = Color.DarkGray
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = text,
                    fontSize = 10.sp,
                    color = Color.DarkGray,
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                HorizontalDivider(
                    color = Color.White,
                    thickness = 1.dp,
                    modifier = Modifier.fillMaxWidth(0.8f)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = value,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    textAlign = TextAlign.Center,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}



@Composable
fun TreeGraphVisualizer(nodes: List<TreeNodeUi>) {

    val nodeColors = nodes.associate { node ->
        node.id to when (node.status) {
            NodeStatus.Default -> Color(0xFFBBDEFB)
            NodeStatus.Found -> Color.Green
            NodeStatus.Visited -> Color(0xFFFFA500)
            NodeStatus.Current -> Color.Red
        }
    }.mapValues { (_, targetColor) ->
        animateColorAsState(targetValue = targetColor, label = "").value
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {

            if (nodes.isEmpty()) return@Canvas

            val nodePositions = mutableMapOf<Int, Offset>()


            val levels = nodes.groupBy { node ->
                var level = 0
                var currentParent = node.parentId
                while (currentParent != null) {
                    level++
                    currentParent = nodes.find { it.id == currentParent }?.parentId
                }
                level
            }

            val verticalStep = size.height / (levels.keys.maxOrNull()?.plus(1) ?: 1)


            levels.forEach { (level, levelNodes) ->
                val horizontalStep = size.width / (levelNodes.size + 1)
                levelNodes.forEachIndexed { index, node ->
                    val position = Offset(
                        x = horizontalStep * (index + 1),
                        y = verticalStep * level + verticalStep / 2
                    )
                    nodePositions[node.id] = position
                }
            }


            nodes.forEach { node ->
                val parentPosition = node.parentId?.let { nodePositions[it] }
                val childPosition = nodePositions[node.id]
                if (parentPosition != null && childPosition != null) {
                    drawLine(
                        color = Color.Gray,
                        start = parentPosition,
                        end = childPosition,
                        strokeWidth = 4f
                    )
                }
            }


            nodes.forEach { node ->
                val position = nodePositions[node.id] ?: return@forEach
                val animatedColor = nodeColors[node.id] ?: Color.Transparent

                drawCircle(
                    color = animatedColor,
                    radius = 40f,
                    center = position
                )


                drawContext.canvas.nativeCanvas.apply {
                    drawText(
                        node.value,
                        position.x,
                        position.y + 10f,
                        Paint().apply {
                            textAlign = Paint.Align.CENTER
                            textSize = 30f
                            color = Color.Black.toArgb()
                        }
                    )
                }
            }
        }
    }
}