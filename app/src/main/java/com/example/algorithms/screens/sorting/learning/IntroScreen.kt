package com.example.algorithms.screens.sorting.learning

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun IntroScreen(
    algorithmTitle: String,
    principleContent: String,
    passesContent: String,
    efficiencyContent: String,
    onStart: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE3F2FD))
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "📚 $algorithmTitle",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF0D47A1)
        )
        Spacer(modifier = Modifier.height(16.dp))
        StyledInfoCard(
            title = "📌 Принцип работы",
            content = principleContent
        )
        StyledInfoCard(
            title = "🔄 Повторные проходы",
            content = passesContent
        )
        StyledInfoCard(
            title = "⏳ Эффективность",
            content = efficiencyContent
        )
        Spacer(modifier = Modifier.height(32.dp))
        Button(
            onClick = onStart,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF90CAF9),
                contentColor = Color(0xFF0D47A1)
            )
        ) {
            Text("🚀 Начать обучение", style = MaterialTheme.typography.titleMedium)
        }
    }
}