package com.example.algorithms.screens.menu

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import com.example.algorithms.di.main_api.AlgorithmProgress
import com.example.algorithms.ui.theme.*
import com.example.algorithms.utils.TokenManager
import com.example.algorithms.viewmodels.profile.ProgressViewModel
import org.koin.androidx.compose.koinViewModel
import kotlin.math.sin


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LearningMapScreen(navController: NavHostController) {
    val progressViewModel: ProgressViewModel = koinViewModel()
    val progress by progressViewModel.progress.collectAsState()
    val context = LocalContext.current
    
    LaunchedEffect(Unit) {
        val token = TokenManager.getToken(context)
        token?.let {
            progressViewModel.loadProgress(it)
        }
    }
    
    var selectedAlgorithm by remember { mutableStateOf<AlgorithmNode?>(null) }
    var selectedCategory by remember { mutableStateOf("Сортировка") }
    
    val categories = listOf("Сортировка", "Поиск", "Графы", "Математика")
    
    Scaffold(
        topBar = {
            Column {
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
                        IconButton(onClick = { navController.navigateUp() }) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Назад",
                                tint = TextPrimary
                            )
                        }
                        Text(
                            text = "Путь обучения",
                            style = MaterialTheme.typography.headlineMedium,
                            color = TextPrimary,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(48.dp))
                    }
                }
                
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(BackgroundTop)
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(categories) { category ->
                        FilterChip(
                            selected = category == selectedCategory,
                            onClick = { selectedCategory = category },
                            label = { Text(category) }
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
            val filteredAlgorithms = getLearningPath(progress).filter { it.category == selectedCategory }
            
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 24.dp),
                verticalArrangement = Arrangement.spacedBy(0.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                items(filteredAlgorithms) { algorithm ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(0.dp)
                    ) {
                        LearningPathNode(
                            algorithm = algorithm,
                            onClick = { selectedAlgorithm = algorithm }
                        )
                        
                        if (algorithm != filteredAlgorithms.last()) {
                            AnimatedPathLine(
                                isCompleted = algorithm.isCompleted,
                                isAvailable = algorithm.isAvailable
                            )
                        }
                    }
                }
            }
        }
        
        selectedAlgorithm?.let { algorithm ->
            AlgorithmInfoDialog(
                algorithm = algorithm,
                onDismiss = { selectedAlgorithm = null }
            )
        }
    }
}

@Composable
private fun LearningPathNode(
    algorithm: AlgorithmNode,
    onClick: () -> Unit
) {
    val nodeColor = when {
        algorithm.isCompleted -> Color(0xFF4CAF50)
        algorithm.isAvailable -> Color(0xFFFFA000)
        else -> Color.Gray.copy(alpha = 0.5f)
    }
    
    val infiniteTransition = rememberInfiniteTransition(label = "node")
    
    // Анимация для непройденных алгоритмов
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )
    
    // Анимация для пройденных алгоритмов
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.9f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "alpha"
    )
    
    Surface(
        modifier = Modifier
            .width(200.dp)
            .height(80.dp)
            .scale(if (algorithm.isAvailable && !algorithm.isCompleted) scale else 1f)
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(16.dp),
                spotColor = nodeColor.copy(alpha = 0.3f)
            ),
        shape = RoundedCornerShape(16.dp),
        color = nodeColor,
        onClick = { if (algorithm.isAvailable) onClick() }
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.alpha(if (algorithm.isCompleted) alpha else 1f)
        ) {
            Text(
                text = algorithm.title,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = 16.sp
                ),
                color = Color.White,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                maxLines = 2,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
            
            if (algorithm.isAvailable) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = "Информация",
                    tint = Color.White.copy(alpha = 0.8f),
                    modifier = Modifier
                        .size(16.dp)
                        .padding(top = 4.dp)
                )
            }
        }
    }
}

@Composable
private fun AnimatedPathLine(
    isCompleted: Boolean,
    isAvailable: Boolean,
    modifier: Modifier = Modifier
) {
    val lineColor = when {
        isCompleted -> Color(0xFF4CAF50)
        isAvailable -> Color(0xFFFFA000)
        else -> Color.Gray.copy(alpha = 0.5f)
    }
    
    val infiniteTransition = rememberInfiniteTransition(label = "path")
    val progress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "progress"
    )
    
    Box(
        modifier = modifier
            .width(8.dp)
            .height(46.dp)
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        lineColor.copy(alpha = 0.8f),
                        lineColor.copy(alpha = 0.4f)
                    )
                )
            )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(progress)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            lineColor,
                            lineColor.copy(alpha = 0.7f)
                        )
                    )
                )
        )
    }
}

@Composable
private fun AlgorithmInfoDialog(
    algorithm: AlgorithmNode,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = algorithm.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(
                                when {
                                    algorithm.isCompleted -> Color(0xFF4CAF50)
                                    algorithm.isAvailable -> Color(0xFFFFA000)
                                    else -> Color.Gray
                                }
                            )
                    )
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Text(
                    text = "Статус: ${if (algorithm.isCompleted) "Изучено" else if (algorithm.isAvailable) "Доступно" else "Недоступно"}",
                    style = MaterialTheme.typography.bodyLarge
                )
                
                if (algorithm.prerequisites.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Предварительные требования:",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold
                    )
                    algorithm.prerequisites.forEach { prereq ->
                        Text(
                            text = "• $prereq",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
                
                if (algorithm.nextSteps.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Следующие шаги:",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold
                    )
                    algorithm.nextSteps.forEach { next ->
                        Text(
                            text = "• $next",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Text(
                    text = algorithm.description,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

private data class AlgorithmNode(
    val id: String,
    val title: String,
    val category: String,
    val isCompleted: Boolean,
    val isAvailable: Boolean,
    val prerequisites: List<String>,
    val nextSteps: List<String>,
    val description: String
)

private fun getLearningPath(progress: List<AlgorithmProgress>): List<AlgorithmNode> {
    val progressMap = progress.associate { it.algorithm to it.completed }

    return listOf(
        AlgorithmNode(
            id = "bubble_sort",
            title = "Сортировка пузырьком",
            category = "Сортировка",
            isCompleted = progressMap["bubble_sort"] ?: false,
            isAvailable = true,
            prerequisites = emptyList(),
            nextSteps = listOf("Сортировка выбором", "Сортировка вставками"),
            description = "Базовый алгоритм сортировки, который многократно проходит по списку, сравнивает соседние элементы и меняет их местами, если они расположены в неправильном порядке."
        ),
        AlgorithmNode(
            id = "selection_sort",
            title = "Сортировка выбором",
            category = "Сортировка",
            isCompleted = progressMap["selection_sort"] ?: false,
            isAvailable = true,
            prerequisites = listOf("Сортировка пузырьком"),
            nextSteps = listOf("Сортировка вставками", "Быстрая сортировка"),
            description = "Алгоритм сортировки, который находит минимальный элемент в неотсортированной части массива и помещает его в начало."
        ),
        AlgorithmNode(
            id = "insertion_sort",
            title = "Сортировка вставками",
            category = "Сортировка",
            isCompleted = progressMap["insertion_sort"] ?: false,
            isAvailable = true,
            prerequisites = listOf("Сортировка пузырьком"),
            nextSteps = listOf("Быстрая сортировка"),
            description = "Алгоритм сортировки, который строит отсортированный массив по одному элементу за раз."
        ),
        AlgorithmNode(
            id = "quick_sort",
            title = "Быстрая сортировка",
            category = "Сортировка",
            isCompleted = progressMap["quick_sort"] ?: false,
            isAvailable = true,
            prerequisites = listOf("Сортировка выбором", "Сортировка вставками"),
            nextSteps = listOf("Линейный поиск"),
            description = "Эффективный алгоритм сортировки, использующий стратегию 'разделяй и властвуй'."
        ),
        AlgorithmNode(
            id = "linear_search",
            title = "Линейный поиск",
            category = "Поиск",
            isCompleted = progressMap["linear_search"] ?: false,
            isAvailable = true,
            prerequisites = listOf("Быстрая сортировка"),
            nextSteps = listOf("Бинарный поиск"),
            description = "Простой алгоритм поиска, который последовательно проверяет каждый элемент списка."
        ),
        AlgorithmNode(
            id = "binary_search",
            title = "Бинарный поиск",
            category = "Поиск",
            isCompleted = progressMap["binary_search"] ?: false,
            isAvailable = true,
            prerequisites = listOf("Линейный поиск"),
            nextSteps = listOf("Обход в глубину"),
            description = "Эффективный алгоритм поиска в отсортированном массиве."
        ),
        AlgorithmNode(
            id = "dfs_search",
            title = "Обход в глубину",
            category = "Графы",
            isCompleted = progressMap["dfs_search"] ?: false,
            isAvailable = true,
            prerequisites = listOf("Бинарный поиск"),
            nextSteps = listOf("Обход в ширину"),
            description = "Алгоритм обхода графа, который исследует как можно дальше вдоль каждой ветви перед возвратом."
        ),
        AlgorithmNode(
            id = "bfs_search",
            title = "Обход в ширину",
            category = "Графы",
            isCompleted = progressMap["bfs_search"] ?: false,
            isAvailable = true,
            prerequisites = listOf("Обход в глубину"),
            nextSteps = listOf("Факториал"),
            description = "Алгоритм обхода графа, который исследует все вершины на текущем уровне перед переходом на следующий уровень."
        ),
        AlgorithmNode(
            id = "factorial",
            title = "Факториал",
            category = "Математика",
            isCompleted = progressMap["factorial"] ?: false,
            isAvailable = true,
            prerequisites = listOf("Обход в ширину"),
            nextSteps = listOf("Числа Фибоначчи"),
            description = "Математическая функция, вычисляющая произведение всех положительных целых чисел от 1 до n."
        ),
        AlgorithmNode(
            id = "fibonacci",
            title = "Числа Фибоначчи",
            category = "Математика",
            isCompleted = progressMap["fibonacci"] ?: false,
            isAvailable = true,
            prerequisites = listOf("Факториал"),
            nextSteps = emptyList(),
            description = "Последовательность чисел, где каждое следующее число является суммой двух предыдущих."
        )
    ).filter { it.isAvailable || it.isCompleted }
}