package com.example.algorithms.screens.sorting.learning.insertion_sorting

import android.annotation.SuppressLint
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.algorithms.data.InsertionSortStep

@SuppressLint("UseOfNonLambdaOffsetOverload")
@Composable
fun InsertionSortVisualization(step: InsertionSortStep) {
    val array = step.array
    val selected = step.selectedIndices
    val sortedBoundary = step.sortedBoundary

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.Bottom
    ) {
        array.forEachIndexed { index, value ->

            val offsetY by animateFloatAsState(
                targetValue = if (selected != null && (index == selected.first || index == selected.second)) -10f else 0f,
                label = "offsetY_$index"
            )


            val boxColor = when {
                selected?.first == index || selected?.second == index -> Color.Red
                index < sortedBoundary -> Color(0xFF4CAF50)
                else -> Color.Blue
            }

            Box(
                modifier = Modifier
                    .width(30.dp)
                    .height((value * 15).dp)
                    .offset(y = offsetY.dp)
                    .animateContentSize(),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(boxColor, shape = RoundedCornerShape(4.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = value.toString(), color = Color.White)
                }
            }
        }
    }
}