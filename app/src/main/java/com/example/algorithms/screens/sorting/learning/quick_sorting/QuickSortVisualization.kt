package com.example.algorithms.screens.sorting.learning.quick_sorting

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.algorithms.data.QuickSortStep

@Composable
fun QuickSortVisualization(step: QuickSortStep) {
    data class ElementState(
        val isSorted: Boolean = false,
        val isPivot: Boolean = false,
        val isWall: Boolean = false,
        val isCurrent: Boolean = false,
        val isInCurrentPartition: Boolean = false
    )

    val elementStates = step.array.indices.map { index ->
        ElementState(
            isSorted = step.sortedIndices.contains(index) ?: false,
            isPivot = index == step.pivotIndex,
            isWall = index == step.leftPointer,
            isCurrent = index == step.rightPointer,
            isInCurrentPartition = step.partitionRange?.let { (start, end) -> index in start..end } ?: false
        )
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        step.array.forEachIndexed { index, value ->
            val state = elementStates[index]
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(horizontal = 4.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .background(
                            when {
                                state.isSorted -> Color.Green
                                state.isPivot -> Color.Red
                                state.isWall -> Color.Green
                                state.isCurrent -> Color.Blue
                                else -> Color.White
                            },
                            RoundedCornerShape(6.dp)
                        )
                        .border(
                            width = if (state.isInCurrentPartition) 2.dp else 1.dp,
                            color = if (state.isInCurrentPartition) Color.Black else Color.Gray,
                            shape = RoundedCornerShape(6.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = value.toString(),
                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 14.sp),
                        color = if (state.isSorted || state.isPivot || state.isWall || state.isCurrent) Color.White else Color.Black
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                if (state.isWall) {
                    Box(
                        modifier = Modifier
                            .width(24.dp)
                            .height(6.dp)
                            .background(Color.Green)
                    )
                }
            }
        }
    }
}
