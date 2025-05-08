package com.example.algorithms.screens.search.simulation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.expandIn
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.algorithms.screens.algorithm_screen.AlgorithmScreen
import com.example.algorithms.viewmodels.simulation_search.BinarySearchSimulationViewModel

@Composable
fun BinarySearchSimulationScreen(navController: NavHostController) {
    val viewModel: BinarySearchSimulationViewModel = viewModel()

    AlgorithmScreen(
        title = "Бинарный поиск",
        navController = navController
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            SettingsSection(viewModel)
            Spacer(modifier = Modifier.height(16.dp))
            BinarySearchVisualizer(viewModel)
            Spacer(modifier = Modifier.height(16.dp))
            ControlPanel(viewModel)
        }
    }
}


@Composable
fun BinarySearchVisualizer(viewModel: BinarySearchSimulationViewModel) {
    val state by viewModel.state.collectAsState()

    AnimatedVisibility(
        visible = state.isSorting,
        enter = fadeIn() + expandIn(),
        exit = fadeOut() + shrinkOut(),
        modifier = Modifier.zIndex(2f)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.7f)),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.animateContentSize()
            ) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(60.dp)
                        .padding(8.dp),
                    color = Color(0xFF66BB6A),
                    strokeWidth = 4.dp
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Сортировка массива",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                )
            }
        }
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(7),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        itemsIndexed(state.list) { index, value ->
            val isCurrent = index == state.currentIndex
            val isEliminated = index in state.eliminatedIndices

            val color = when {
                value == state.targetInput.toIntOrNull() && isCurrent -> Color(0xFF66BB6A)
                isCurrent -> Color(0xFFFFEB3B)
                isEliminated -> Color(0xFFBDBDBD)
                else -> Color(0xFFE0E0E0)
            }

            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(9.dp))
                    .background(color),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = value.toString(),
                    fontWeight = FontWeight.Bold,
                    color = if (isCurrent) Color.Black else Color.DarkGray
                )
            }
        }
    }
}
