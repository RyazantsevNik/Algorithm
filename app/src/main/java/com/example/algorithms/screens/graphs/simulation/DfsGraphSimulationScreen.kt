package com.example.algorithms.screens.graphs.simulation

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.algorithms.screens.algorithm_screen.AlgorithmScreen
import com.example.algorithms.screens.sorting.simulation.CustomButton
import com.example.algorithms.viewmodels.simulation_graphs.DfsGraphSimulationViewModel
import com.example.algorithms.viewmodels.simulation_graphs.NodeStatus


@Composable
fun DfsGraphSimulationScreen(navController: NavHostController) {
    val viewModel: DfsGraphSimulationViewModel = viewModel()

    AlgorithmScreen(
        title = "Поиск в глубину (DFS)",
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
                    text = "Кандидаты (стек)",
                    value = viewModel.stack.reversed().joinToString(" -> ") { it.value }
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
