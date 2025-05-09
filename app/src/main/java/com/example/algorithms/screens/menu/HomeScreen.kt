package com.example.algorithms.screens.menu

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.AccountTree
import androidx.compose.material.icons.filled.BubbleChart
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.CallSplit
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Expand
import androidx.compose.material.icons.filled.LinearScale
import androidx.compose.material.icons.filled.Merge
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Route
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SelectAll
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Timeline
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
import com.example.algorithms.data.AlgorithmItem
import com.example.algorithms.di.main_api.AlgorithmProgress
import com.example.algorithms.navigation.AppRoutes
import com.example.algorithms.ui.theme.*
import com.example.algorithms.utils.TokenManager
import com.example.algorithms.viewmodels.profile.ProgressViewModel
import com.example.algorithms.viewmodels.menu.FavoritesViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavHostController) {
    val progressViewModel: ProgressViewModel = koinViewModel()
    val favoritesViewModel: FavoritesViewModel = koinViewModel()
    val progress by progressViewModel.progress.collectAsState()
    val favoriteAlgorithms by favoritesViewModel.favoriteAlgorithms.collectAsState()
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
                            painter = painterResource(id = R.drawable.ic_path),
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
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        categories.take(2).forEach { category ->
                            CategoryCard(
                                category = category,
                                progress = calculateCategoryProgress(category.title, progress),
                                onClick = {
                                    navController.navigate("${AppRoutes.ALGORITHMS}/${category.title}")
                                },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        categories.drop(2).forEach { category ->
                            CategoryCard(
                                category = category,
                                progress = calculateCategoryProgress(category.title, progress),
                                onClick = {
                                    navController.navigate("${AppRoutes.ALGORITHMS}/${category.title}")
                                },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }

                if (favoriteAlgorithms.isNotEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        // Декоративная линия слева
                        Box(
                            modifier = Modifier
                                .width(2.dp)
                                .height(16.dp)
                                .background(
                                    brush = Brush.verticalGradient(
                                        colors = listOf(
                                            SoftOrange,
                                            SoftOrange.copy(alpha = 0.5f)
                                        )
                                    ),
                                    shape = RoundedCornerShape(1.dp)
                                )
                                .align(Alignment.CenterStart)
                        )
                        
                        // Заголовок
                        Text(
                            text = "Избранные алгоритмы",
                            style = MaterialTheme.typography.titleSmall,
                            color = SoftOrange,
                            modifier = Modifier
                                .padding(start = 8.dp)
                                .align(Alignment.CenterStart)
                        )
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        favoriteAlgorithms.forEach { algorithm ->
                            FavoriteAlgorithmCard(
                                algorithm = algorithm,
                                onClick = {
                                    navController.navigate(AppRoutes.algorithmSelectionRoute(algorithm.title))
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CategoryCard(
    category: Category,
    progress: Float,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
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

@Composable
fun FavoriteAlgorithmCard(
    algorithm: AlgorithmItem,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .background(
                        color = when (algorithm.title) {
                            "Бинарный поиск", "Линейный поиск" -> Color(0xFFFFF3E0)
                            "Сортировка пузырьком", "Сортировка вставками", 
                            "Сортировка выбором", "Быстрая сортировка" -> Color(0xFFE3F2FD)
                            "Обход в ширину", "Обход в глубину" -> Color(0xFFF3E5F5)
                            "Факториал", "Числа Фибоначчи" -> Color(0xFFE8F5E9)
                            else -> Color(0xFFF5F5F5)
                        },
                        shape = RoundedCornerShape(8.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = when (algorithm.title) {
                        "Бинарный поиск" -> Icons.Default.Search
                        "Линейный поиск" -> Icons.Default.LinearScale
                        "Сортировка пузырьком" -> Icons.Default.BubbleChart
                        "Сортировка вставками" -> Icons.Default.Sort
                        "Сортировка выбором" -> Icons.Default.SelectAll
                        "Сортировка слиянием" -> Icons.Default.Merge
                        "Быстрая сортировка" -> Icons.Default.Speed
                        "Обход в ширину" -> Icons.Default.Expand
                        "Обход в глубину" -> Icons.Default.CallSplit
                        "Факториал" -> Icons.Default.Calculate
                        "Числа Фибоначчи" -> Icons.Default.Timeline
                        else -> Icons.Default.Code
                    },
                    contentDescription = algorithm.title,
                    tint = when (algorithm.title) {
                        "Факториал", "Числа Фибоначчи" -> Color(0xFF2E7D32)
                        "Бинарный поиск", "Линейный поиск" -> Color(0xFFE65100)
                        "Сортировка пузырьком", "Сортировка вставками", 
                        "Сортировка выбором", "Быстрая сортировка" -> Color(0xFF1565C0)
                        "Обход в ширину", "Обход в глубину" -> Color(0xFF7B1FA2)
                        else -> Color(0xFF424242)
                    },
                    modifier = Modifier.size(20.dp)
                )
            }
            
            Spacer(modifier = Modifier.width(12.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = algorithm.title,
                    style = MaterialTheme.typography.bodyLarge,
                    color = TextPrimary
                )
                Text(
                    text = when {
                        algorithm.title.contains("Сортировка") -> "Сортировка"
                        algorithm.title.contains("Поиск") -> "Поиск"
                        algorithm.title.contains("Обход") -> "Графы"
                        else -> "Математика"
                    },
                    style = MaterialTheme.typography.bodySmall,
                    color = SoftOrange
                )
            }
            
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = SoftOrange.copy(alpha = 0.8f),
                modifier = Modifier.size(20.dp)
            )
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