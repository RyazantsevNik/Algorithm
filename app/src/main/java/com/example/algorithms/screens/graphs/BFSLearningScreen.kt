package com.example.algorithms.screens.graphs

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.algorithms.R
import com.example.algorithms.screens.algorithm_screen.AlgorithmScreen
import com.example.algorithms.screens.search.learning.binary_search.StepCard
import com.example.algorithms.viewmodels.step_graphs.BfsSearchViewModel

@Composable
fun BfsSearchScreen(navController: NavHostController) {
    val viewModel: BfsSearchViewModel = viewModel()
    val current = viewModel.current.value
    val visited = viewModel.visited
    val remainingCandidates = viewModel.remainingCandidates
    val currentStep = viewModel.getCurrentStep()
    val explanation = viewModel.explanation.value

    AlgorithmScreen(title = "Обход в ширину (BFS)", navController = navController) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            StepCard(currentStep,explanation)

            Spacer(modifier = Modifier.height(14.dp))


            BfsGraphVisualizer(
                currentNode = current,
                visited = visited,
                remainingCandidates = remainingCandidates
            )

            Spacer(modifier = Modifier.height(24.dp))

            ControlPanel(viewModel)

        }
    }
}

@Composable
fun ControlPanel(viewModel: BfsSearchViewModel){
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



@Composable
fun BfsGraphVisualizer(
    currentNode: String?,
    visited: List<String>,
    remainingCandidates: List<String>
) {

    val xOffset = 90f
    val yOffset = 100f


    val nodePositions = mapOf(
        "a" to Offset(400f + xOffset, 100f + yOffset),
        "b" to Offset(200f + xOffset, 270f + yOffset),
        "c" to Offset(400f + xOffset, 270f + yOffset),
        "d" to Offset(600f + xOffset, 270f + yOffset),
        "e" to Offset(50f + xOffset, 440f + yOffset),
        "f" to Offset(250f + xOffset, 440f + yOffset),
        "h" to Offset(400f + xOffset, 440f + yOffset),
        "i" to Offset(550f + xOffset, 440f + yOffset),
        "j" to Offset(750f + xOffset, 440f + yOffset),
        "k" to Offset(50f + xOffset, 610f + yOffset),
        "g" to Offset(400f + xOffset, 610f + yOffset),
        "l" to Offset(750f + xOffset, 610f + yOffset)
    )

    val edges = listOf(
        "a" to "b", "a" to "c", "a" to "d",
        "b" to "e", "b" to "f",
        "c" to "h",
        "d" to "i", "d" to "j",
        "e" to "k",
        "h" to "g",
        "j" to "l"
    )


    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
    ) {

        edges.forEach { (from, to) ->
            val start = nodePositions[from] ?: Offset.Zero
            val end = nodePositions[to] ?: Offset.Zero
            val isVisited = visited.contains(from) && visited.contains(to)
            val color = if (isVisited) Color(0xFFFFA726) else Color.Gray

            drawLine(
                color = color,
                start = start,
                end = end,
                strokeWidth = 8f
            )
        }


        nodePositions.forEach { (nodeId, position) ->
            var color = when {
                nodeId == currentNode -> Color.Red
                remainingCandidates.contains(nodeId) -> Color.Green
                visited.contains(nodeId) -> Color(0xFFFFA726)
                else -> Color.LightGray
            }

            drawCircle(
                color = color,
                radius = 65f,
                center = position
            )


            drawContext.canvas.nativeCanvas.drawText(
                nodeId,
                position.x,
                position.y + 25f,
                android.graphics.Paint().apply {
                    textAlign = android.graphics.Paint.Align.CENTER
                    textSize = 52f
                    color = Color.Black
                    isFakeBoldText = true
                }
            )
        }
    }
}

