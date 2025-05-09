package com.example.algorithms.screens.menu

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.algorithms.R
import com.example.algorithms.di.main_api.AlgorithmProgress
import com.example.algorithms.navigation.AppRoutes
import com.example.algorithms.ui.theme.*
import com.example.algorithms.utils.TokenManager
import com.example.algorithms.viewmodels.profile.ProgressViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavHostController) {
    val progressViewModel: ProgressViewModel = koinViewModel()
    val progress by progressViewModel.progress.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        val token = TokenManager.getToken(context)
        token?.let {
            progressViewModel.loadProgress(it)
        }
    }

    Scaffold(
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(BackgroundTop)
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Главная",
                        style = MaterialTheme.typography.headlineMedium,
                        color = TextPrimary,
                        fontWeight = FontWeight.Bold
                    )
                    IconButton(
                        onClick = { navController.navigate(AppRoutes.LEARNING_MAP) }
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_robot_logo),
                            contentDescription = "Карта обучения",
                            tint = TextPrimary,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(BackgroundTop, BackgroundBottom)
                    )
                )
        ) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(categories) { category ->
                    CategoryCard(
                        category = category,
                        progress = calculateCategoryProgress(category.title, progress),
                        onClick = {
                            navController.navigate("${AppRoutes.ALGORITHMS}/${category.title}")
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun CategoryCard(
    category: Category,
    progress: Float,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(category.gradient)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Image(
                    painter = painterResource(id = category.imageRes),
                    contentDescription = category.title,
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )

                Column {
                    Text(
                        text = category.title,
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    LinearProgressIndicator(
                        progress = { progress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp)),
                        color = Color.White,
                        trackColor = Color.White.copy(alpha = 0.3f)
                    )
                    
                    Text(
                        text = "${(progress * 100).toInt()}%",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White
                    )
                }
            }
        }
    }
}

private data class Category(
    val title: String,
    val imageRes: Int,
    val gradient: Brush
)

private val categories = listOf(
    Category(
        title = "Сортировка",
        imageRes = R.drawable.im_sorting,
        gradient = Brush.linearGradient(
            colors = listOf(
                Color(0xFF2196F3),
                Color(0xFF1976D2)
            )
        )
    ),
    Category(
        title = "Поиск",
        imageRes = R.drawable.im_search,
        gradient = Brush.linearGradient(
            colors = listOf(
                Color(0xFFFF9800),
                Color(0xFFF57C00)
            )
        )
    ),
    Category(
        title = "Графы",
        imageRes = R.drawable.im_graph,
        gradient = Brush.linearGradient(
            colors = listOf(
                Color(0xFF9C27B0),
                Color(0xFF7B1FA2)
            )
        )
    ),
    Category(
        title = "Математика",
        imageRes = R.drawable.im_math,
        gradient = Brush.linearGradient(
            colors = listOf(
                Color(0xFF4CAF50),
                Color(0xFF388E3C)
            )
        )
    )
)

private fun calculateCategoryProgress(categoryTitle: String, progress: List<AlgorithmProgress>): Float {
    val categoryAlgorithms = when (categoryTitle) {
        "Сортировка" -> listOf("bubble_sort", "selection_sort", "insertion_sort", "quick_sort")
        "Поиск" -> listOf("binary_search", "linear_search")
        "Графы" -> listOf("dfs_search", "bfs_search")
        "Математика" -> listOf("factorial", "fibonacci")
        else -> emptyList()
    }
    
    val completedCount = progress.count { it.algorithm in categoryAlgorithms && it.completed }
    return if (categoryAlgorithms.isEmpty()) 0f else completedCount.toFloat() / categoryAlgorithms.size
}