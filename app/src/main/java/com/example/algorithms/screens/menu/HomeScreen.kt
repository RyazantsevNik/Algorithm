package com.example.algorithms.screens.menu

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.algorithms.navigation.AppRoutes
import com.example.algorithms.ui.theme.BackgroundBottom
import com.example.algorithms.ui.theme.BackgroundTop
import com.example.algorithms.ui.theme.PrimaryBlue
import com.example.algorithms.ui.theme.TextPrimary
import com.example.algorithms.ui.theme.TextSecondary
import com.example.algorithms.ui.theme.LightBlue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavHostController) {
    Scaffold(
        topBar = {
            Column {
                TopAppBar(
                    title = { Text("Главная") },
                    actions = {
                        IconButton(onClick = { navController.navigate(AppRoutes.PROFILE) }) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = "Профиль",
                                tint = TextPrimary
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = BackgroundTop,
                        titleContentColor = TextPrimary
                    )
                )
                HorizontalDivider(thickness = 1.dp, color = Color.LightGray)
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(BackgroundTop, BackgroundBottom)
                    )
                )
        ) {
            Column(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
            ) {

                ProgressSection()
                FavoriteSection(navController)
                RecommendationsSection(navController)
                DailyTaskSection()
            }
        }
    }
}

@Composable
private fun ProgressSection() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Ваш прогресс",
                style = MaterialTheme.typography.titleLarge,
                color = TextPrimary
            )
            Spacer(modifier = Modifier.height(8.dp))
            LinearProgressIndicator(
                progress = { 0.625f },
                modifier = Modifier.fillMaxWidth(),
                color = PrimaryBlue,
                trackColor = LightBlue
            )
            Text(
                text = "Изучено 10 из 16 алгоритмов",
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary
            )
        }
    }
}

@Composable
private fun FavoriteSection(navController: NavHostController) {
    Column(
        modifier = Modifier.padding(horizontal = 16.dp)
    ) {
        Text(
            text = "Избранное",
            style = MaterialTheme.typography.titleMedium
        )
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {






        }
    }
}

@Composable
private fun RecommendationsSection(navController: NavHostController) {
    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        Text(
            text = "Рекомендации",
            style = MaterialTheme.typography.titleMedium,
            color = TextPrimary
        )
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .clickable { 
                    navController.navigate(AppRoutes.algorithmSelectionRoute("Быстрая сортировка"))
                },
            colors = CardDefaults.cardColors(containerColor = LightBlue)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Продолжить изучение",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary
                    )
                    Text(
                        text = "Быстрая сортировка",
                        style = MaterialTheme.typography.titleMedium,
                        color = TextPrimary
                    )
                }
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = null,
                    tint = PrimaryBlue
                )
            }
        }
    }
}

@Composable
private fun DailyTaskSection() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Ежедневная задача",
                    style = MaterialTheme.typography.titleMedium,
                    color = TextPrimary
                )
                Text(
                    text = "Изучите основы сортировки пузырьком",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary
                )
            }
            CircularProgressIndicator(
                progress = { 0.0f },
                modifier = Modifier.size(40.dp),
                color = PrimaryBlue,
                trackColor = LightBlue
            )
        }
    }
}

private data class FavoriteAlgorithm(
    val title: String,
    val icon: ImageVector,
    val color: Color
)

private val favoriteAlgorithms = listOf(
    FavoriteAlgorithm(
        "Сортировка пузырьком",
        Icons.Default.Add,
        Color(0xFF1976D2)
    ),
    FavoriteAlgorithm(
        "Бинарный поиск",
        Icons.Default.Search,
        Color(0xFF388E3C)
    )
)