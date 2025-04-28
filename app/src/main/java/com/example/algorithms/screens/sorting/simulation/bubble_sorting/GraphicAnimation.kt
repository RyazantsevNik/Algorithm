package com.example.algorithms.screens.sorting.simulation.bubble_sorting

import android.graphics.Paint
import android.graphics.Typeface
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.unit.dp
import com.example.algorithms.viewmodels.simulation_sorting.base_class_for_simulation.SortingViewModel

@Composable
fun GraphicAnimation(viewModel: SortingViewModel){
    val state by viewModel.state.collectAsState()

    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
            .padding(top = 8.dp)
    ) {
        val barWidth = size.width / state.list.size
        val maxBarHeight = size.height - 40

        if (viewModel.animatedOffsets.size == state.list.size) {
            state.list.forEachIndexed { index, value ->
                val height = (minOf(value, 25) / 25f) * maxBarHeight

                val unsortedColor = Color(0xFFBB86FC)
                val sortedColor = Color(0xFF03DAC5)
                val swapColor = Color.Red

                val color = when {
                    state.i >= state.list.size - 1 -> sortedColor
                    index >= state.list.size - state.i -> sortedColor
                    index == state.j || index == state.j + 1 -> swapColor
                    else -> unsortedColor
                }

                val offsetX = viewModel.animatedOffsets[index].value * barWidth
                val barX = index * barWidth + offsetX
                val barY = size.height - height

                drawRoundRect(
                    color = Color.Black.copy(alpha = 0.2f),
                    topLeft = Offset(barX + 4, barY + 4),
                    size = Size(barWidth, height),
                    cornerRadius = CornerRadius(8f, 8f)
                )

                drawRoundRect(
                    brush = Brush.verticalGradient(
                        colors = listOf(color.copy(alpha = 0.8f), color)
                    ),
                    topLeft = Offset(barX, barY),
                    size = Size(barWidth - 8, height),
                    cornerRadius = CornerRadius(8f, 8f)
                )

                drawContext.canvas.nativeCanvas.apply {
                    drawText(
                        value.toString(),
                        barX + (barWidth - 8) / 2,
                        barY - 10,
                        Paint().apply {
                            setColor(android.graphics.Color.BLACK)
                            textSize = 32f
                            textAlign = Paint.Align.CENTER
                            typeface = Typeface.DEFAULT_BOLD
                        }
                    )
                }
            }
        }
    }
}