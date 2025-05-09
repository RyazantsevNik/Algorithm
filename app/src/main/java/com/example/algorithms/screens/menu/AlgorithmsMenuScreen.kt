package com.example.algorithms.screens.menu

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Fort
import androidx.compose.material.icons.filled.LinearScale
import androidx.compose.material.icons.filled.Merge
import androidx.compose.material.icons.filled.SelectAll
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Timeline
import androidx.compose.material.icons.filled.BubbleChart
import androidx.compose.material.icons.filled.AccountTree
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.CallSplit
import androidx.compose.material.icons.filled.Expand
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Route
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.algorithms.R
import com.example.algorithms.data.AlgorithmCategory
import com.example.algorithms.data.AlgorithmItem
import com.example.algorithms.data.algorithmCategories
import com.example.algorithms.navigation.AppRoutes
import com.example.algorithms.ui.theme.BackgroundBottom
import com.example.algorithms.ui.theme.BackgroundTop
import com.example.algorithms.ui.theme.LightBlue
import com.example.algorithms.ui.theme.PastelPurple
import com.example.algorithms.ui.theme.PrimaryBlue
import com.example.algorithms.ui.theme.SoftGreen
import com.example.algorithms.ui.theme.SoftOrange
import com.example.algorithms.ui.theme.TextSecondary
import com.example.algorithms.viewmodels.menu.MenuViewModel
import com.example.algorithms.utils.TokenManager
import com.example.algorithms.viewmodels.profile.ProgressViewModel
import org.koin.androidx.compose.koinViewModel
import androidx.compose.ui.text.font.FontWeight
import com.example.algorithms.viewmodels.menu.FavoritesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlgorithmsMenuScreen(
    navController: NavHostController,
    initialCategory: String? = null
) {
    val viewModel: MenuViewModel = viewModel()
    val progressViewModel: ProgressViewModel = koinViewModel()
    val context = LocalContext.current
    val listState = rememberLazyListState()

    var searchText by remember { mutableStateOf("") }
    var isSearchActive by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        val token = TokenManager.getToken(context)
        token?.let {
            progressViewModel.loadProgress(it)
        }
    }

    LaunchedEffect(initialCategory) {
        if (initialCategory != null) {
            val categoryIndex = algorithmCategories.indexOfFirst { it.title == initialCategory }
            if (categoryIndex != -1) {
                listState.animateScrollToItem(categoryIndex)
            }
        }
    }

    val progress by progressViewModel.progress.collectAsState()
    val algorithmProgress = remember(progress) {
        progress.associate { it.algorithm to it.completed }
    }

    val filteredItems = algorithmCategories.flatMap { it.items }
        .filter { item -> item.title.contains(searchText, ignoreCase = true) }

    Scaffold(
        containerColor = Color.Transparent,
        topBar = {}
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Brush.verticalGradient(listOf(BackgroundTop, BackgroundBottom)))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .align(Alignment.TopCenter)
                    .clip(RoundedCornerShape(20.dp))
                    .zIndex(1f)
                    .background(LightBlue)
                    .padding(horizontal = 8.dp, vertical = 8.dp),
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.logo_for_app),
                        contentDescription = "Логотип",
                        modifier = Modifier.size(42.dp)
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Box(modifier = Modifier.weight(1f)) {
                        OutlinedTextField(
                            value = searchText,
                            onValueChange = {
                                searchText = it
                                isSearchActive = it.isNotEmpty()
                            },
                            placeholder = {
                                Text(
                                    "Поиск",
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            },
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                containerColor = MaterialTheme.colorScheme.surface,
                                focusedBorderColor = MaterialTheme.colorScheme.primary,
                                unfocusedBorderColor = Color.Transparent,
                                cursorColor = MaterialTheme.colorScheme.primary
                            ),
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                            keyboardActions = KeyboardActions(
                                onDone = { isSearchActive = true }
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    IconButton(onClick = { navController.navigate(AppRoutes.INFO) }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_info_button),
                            contentDescription = "Информация",
                            tint = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }
            LazyColumn(state = listState) {
                item {
                    Spacer(modifier = Modifier.height(90.dp))
                }
                items(algorithmCategories) { category ->
                    CategoryWithSubItems(
                        category = category,
                        navController = navController,
                        viewModel = viewModel
                    )
                }
            }

            if (isSearchActive && searchText.isNotEmpty() && filteredItems.isNotEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 32.dp)
                        .offset(y = 80.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.surface)
                ) {
                    filteredItems.forEachIndexed { index, item ->
                        Text(
                            text = item.title,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    isSearchActive = false
                                    searchText = ""
                                    navController.navigate(AppRoutes.algorithmSelectionRoute(item.title))
                                }
                                .padding(horizontal = 16.dp, vertical = 12.dp)
                                .background(MaterialTheme.colorScheme.surface),
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        if (index < filteredItems.lastIndex) {
                            HorizontalDivider(
                                modifier = Modifier.padding(horizontal = 16.dp),
                                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CategoryWithSubItems(
    category: AlgorithmCategory,
    navController: NavHostController,
    viewModel: MenuViewModel
) {
    val expandedCategoriesState by viewModel.expandedCategories.collectAsStateWithLifecycle()
    val isExpanded = expandedCategoriesState[category.title] ?: false
    val rotation by animateFloatAsState(
        targetValue = if (isExpanded) 90f else 0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = ""
    )

    val categoryGradient = when (category.title) {
        "Сортировка" -> Brush.linearGradient(
            colors = listOf(
                Color(0xFF2196F3),
                Color(0xFF1976D2)
            )
        )
        "Математика" -> Brush.linearGradient(
            colors = listOf(
                Color(0xFF4CAF50),
                Color(0xFF388E3C)
            )
        )
        "Поиск" -> Brush.linearGradient(
            colors = listOf(
                Color(0xFFFF9800),
                Color(0xFFF57C00)
            )
        )
        "Графы" -> Brush.linearGradient(
            colors = listOf(
                Color(0xFF9C27B0),
                Color(0xFF7B1FA2)
            )
        )
        else -> Brush.linearGradient(
            colors = listOf(
                Color(0xFF757575),
                Color(0xFF616161)
            )
        )
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .clickable { viewModel.toggleCategory(category.title) },
            colors = CardDefaults.cardColors(containerColor = Color.Transparent),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(categoryGradient)
                    .clip(RoundedCornerShape(16.dp))
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = when (category.title) {
                                "Сортировка" -> Icons.Default.Sort
                                "Математика" -> Icons.Default.Calculate
                                "Поиск" -> Icons.Default.Search
                                "Графы" -> Icons.Default.AccountTree
                                else -> Icons.Default.Info
                            },
                            contentDescription = category.title,
                            tint = Color.White,
                            modifier = Modifier.size(28.dp)
                        )
                        Text(
                            text = category.title,
                            style = MaterialTheme.typography.titleLarge,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = "Развернуть",
                        tint = Color.White,
                        modifier = Modifier.rotate(rotation)
                    )
                }
            }
        }

        AnimatedVisibility(
            visible = isExpanded,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            Column {
                category.items.forEach { item ->
                    SubItem(item = item, navController = navController, viewModel = viewModel)
                }
            }
        }
    }
}

@Composable
fun SubItem(
    item: AlgorithmItem,
    navController: NavHostController,
    viewModel: MenuViewModel
) {
    val progressViewModel: ProgressViewModel = koinViewModel()
    val favoritesViewModel: FavoritesViewModel = koinViewModel()
    val progress by progressViewModel.progress.collectAsState()
    val algorithmKey = getAlgorithmKey(item.title)
    val isCompleted = progress.any { it.algorithm == algorithmKey && it.completed }
    val isFavorite by favoritesViewModel.favoriteIds.collectAsState()
    
    val scale by animateFloatAsState(
        targetValue = 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = ""
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 4.dp)
            .scale(scale)
            .clickable {
                navController.navigate(AppRoutes.algorithmSelectionRoute(item.title))
            },
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF8F9FA)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        border = BorderStroke(
            width = 2.dp,
            brush = if (isCompleted) {
                Brush.linearGradient(
                    colors = listOf(
                        Color(0xFF81C784),
                        Color(0xFF4CAF50),
                        Color(0xFF81C784)
                    )
                )
            } else {
                Brush.linearGradient(
                    colors = listOf(
                        Color(0xFFE0E0E0),
                        Color(0xFFBDBDBD)
                    )
                )
            }
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(
                            color = when (item.title) {
                                "Факториал", "Числа Фибоначчи" -> Color(0xFFE8F5E9)
                                "Бинарный поиск", "Линейный поиск" -> Color(0xFFFFF3E0)
                                "Сортировка пузырьком", "Сортировка вставками", 
                                "Сортировка выбором", "Быстрая сортировка" -> Color(0xFFE3F2FD)
                                "Обход в ширину", "Обход в глубину",
                                "Алгоритм Дейкстры", "Алгоритм Крускала" -> Color(0xFFF3E5F5)
                                else -> Color(0xFFF5F5F5)
                            },
                            shape = RoundedCornerShape(12.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = when (item.title) {
                            "Факториал" -> Icons.Default.Calculate
                            "Фибоначчи" -> Icons.Default.Timeline
                            "Бинарный поиск" -> Icons.Default.Search
                            "Линейный поиск" -> Icons.Default.LinearScale
                            "Сортировка пузырьком" -> Icons.Default.BubbleChart
                            "Сортировка вставками" -> Icons.Default.Sort
                            "Сортировка выбором" -> Icons.Default.SelectAll
                            "Сортировка слиянием" -> Icons.Default.Merge
                            "Быстрая сортировка" -> Icons.Default.Speed
                            "Обход в ширину" -> Icons.Default.Expand
                            "Обход в глубину" -> Icons.Default.CallSplit
                            else -> Icons.Default.Code
                        },
                        contentDescription = item.title,
                        tint = when (item.title) {
                            "Факториал", "Числа Фибоначчи" -> Color(0xFF2E7D32)
                            "Бинарный поиск", "Линейный поиск" -> Color(0xFFE65100)
                            "Сортировка пузырьком", "Сортировка вставками", 
                            "Сортировка выбором", "Быстрая сортировка" -> Color(0xFF1565C0)
                            "Обход в ширину", "Обход в глубину"-> Color(0xFF424242)
                            else -> Color(0xFF424242)
                        },
                        modifier = Modifier.size(24.dp)
                    )
                }
                Column {
                    Text(
                        text = item.title,
                        style = MaterialTheme.typography.titleMedium,
                        color = Color(0xFF212121)
                    )
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(2.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        repeat(item.starRating) {
                            Icon(
                                imageVector = Icons.Filled.Star,
                                contentDescription = "Звезда",
                                tint = SoftOrange,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }
            IconButton(
                onClick = { favoritesViewModel.toggleFavorite(item) },
                modifier = Modifier.size(28.dp)
            ) {
                Icon(
                    imageVector = if (item.title in isFavorite) Icons.Filled.Star else Icons.Outlined.Star,
                    contentDescription = "Добавить в избранное",
                    tint = if (item.title in isFavorite) SoftOrange else Color.Gray,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

private fun getAlgorithmKey(title: String): String {
    return when (title) {
        "Факториал" -> "factorial"
        "Числа Фибоначчи" -> "fibonacci"
        "Бинарный поиск" -> "binary_search"
        "Линейный поиск" -> "linear_search"
        "Обход в глубину" -> "dfs_search"
        "Обход в ширину" -> "bfs_search"
        "Сортировка пузырьком" -> "bubble_sort"
        "Сортировка выбором" -> "selection_sort"
        "Сортировка вставками" -> "insertion_sort"
        "Быстрая сортировка" -> "quick_sort"
        else -> title.lowercase().replace(" ", "_")
    }
}