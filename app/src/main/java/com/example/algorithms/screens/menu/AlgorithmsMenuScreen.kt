package com.example.algorithms.screens.menu

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
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
import com.example.algorithms.ui.theme.TextPrimary
import com.example.algorithms.ui.theme.TextSecondary
import com.example.algorithms.viewmodels.menu.MenuViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlgorithmsMenuScreen(navController: NavHostController) {
    val viewModel: MenuViewModel = viewModel()

    var searchText by remember { mutableStateOf("") }
    var isSearchActive by remember { mutableStateOf(false) }

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
                            tint = MaterialTheme.colorScheme.onPrimaryContainer
                            ,modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }
            LazyColumn {
                item {
                    Spacer(modifier = Modifier.height(90.dp))
                }
                items(algorithmCategories){ category ->
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
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchField(
    value: String,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text("Поиск...") },
        leadingIcon = {
            Icon(Icons.Default.Search, contentDescription = "Поиск")
        },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = Color.Transparent,
//            textColor = MaterialTheme.colorScheme.onSurface,
//            placeholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
//            leadingIconColor = MaterialTheme.colorScheme.primary
        ),
        shape = RoundedCornerShape(16.dp),
        singleLine = true,
        modifier = Modifier
            .padding(horizontal = 8.dp)
            .fillMaxWidth()
            .height(48.dp)
    )
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
        animationSpec = tween(300),
        label = ""
    )

    Column(modifier = Modifier.fillMaxWidth()) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .clickable { viewModel.toggleCategory(category.title) },
            colors = CardDefaults.cardColors(
                containerColor = when (category.title) {
                    "Сортировка" -> PrimaryBlue
                    "Математика" -> SoftGreen
                    "Поиск" -> PastelPurple
                    "Графы" -> SoftOrange
                    else -> TextSecondary
                }
            )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
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
                        Image(
                            painter = painterResource(
                                id = when (category.title) {
                                    "Сортировка" -> R.drawable.ic_sort
                                    "Математика" -> R.drawable.ic_math
                                    "Поиск" -> R.drawable.ic_search
                                    "Графы" -> R.drawable.ic_graph
                                    else -> R.drawable.ic_info_button
                                }
                            ),
                            contentDescription = category.title,
                            modifier = Modifier.size(24.dp)
                        )
                        Text(
                            text = category.title,
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = "Развернуть",
                        tint = MaterialTheme.colorScheme.onPrimary,
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
    // Градиент для фона карточки
    val gradientColors = listOf(
        Color(0xFF1B679A), // Фиолетовый
        Color(0xFF479BBC)  // Светло-фиолетовый
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 8.dp)
            .clickable {
                viewModel.clickItem(item.title)
                navController.navigate(AppRoutes.algorithmSelectionRoute(item.title))
            },
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        shape = RoundedCornerShape(16.dp) // Скругление углов
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Brush.verticalGradient(gradientColors)) // Применяем градиент
                .padding(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = null,
                        tint = Color.Yellow, // Яркий акцент
                        modifier = Modifier.size(24.dp)
                    )
                    Column {
                        Text(
                            text = item.title,
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color.White // Белый текст на градиенте
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        // Отображение звёзд в зависимости от starRating
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            repeat(3) { index ->
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = null,
                                    tint = if (index < item.starRating) Color(0xFFFFD700) else Color.Gray, // Золотые и серые звёзды
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }
                }

                // Иконка "Пройдено"
                if (item.isCompleted) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Пройдено",
                        tint = Color.Green, // Зелёная иконка
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    }
}