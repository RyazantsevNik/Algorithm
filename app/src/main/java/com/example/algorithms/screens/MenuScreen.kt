package com.example.algorithms.screens

import android.annotation.SuppressLint
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.algorithms.data.AlgorithmCategory
import com.example.algorithms.data.AlgorithmItem
import com.example.algorithms.data.algorithmCategories
import com.example.algorithms.navigation.AppRoutes
import com.example.algorithms.viewmodels.MenuViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuScreen(navController: NavHostController) {
    // Получение ViewModel через Koin
    val viewModel: MenuViewModel = viewModel()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Алгоритмы") })
        }
    ) { padding ->
        LazyColumn(modifier = Modifier.padding(padding)) {
            algorithmCategories.forEach { category ->
                item {
                    CategoryWithSubItems(
                        category = category,
                        navController = navController,
                        viewModel = viewModel
                    )
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
    // Получаем текущее состояние expandedCategories
    val expandedCategoriesState by viewModel.expandedCategories.collectAsState(initial = emptyMap())

    // Проверяем, раскрыта ли категория
    val isExpanded = expandedCategoriesState[category.title] ?: false

    // Анимация для иконки
    val rotation by animateFloatAsState(targetValue = if (isExpanded) 90f else 0f, label = "")

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .clickable { viewModel.toggleCategory(category.title) },
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            shape = MaterialTheme.shapes.medium
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = androidx.compose.ui.graphics.Brush.verticalGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.8f),
                                MaterialTheme.colorScheme.secondary.copy(alpha = 0.6f)
                            )
                        )
                    )
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = category.title,
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = "Expand",
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.rotate(rotation)
                    )
                }
            }
        }

        if (isExpanded) {
            category.items.forEach { item ->
                SubItem(
                    item = item,
                    navController = navController,
                    viewModel = viewModel
                )
            }
        }
    }
}

@SuppressLint("UseOfNonLambdaOffsetOverload")
@Composable
fun SubItem(
    item: AlgorithmItem,
    navController: NavHostController,
    viewModel: MenuViewModel
) {
    // Получаем текущее состояние clickedItems
    val clickedItemsState by viewModel.clickedItems.collectAsState(initial = emptyMap())
    // Проверяем, был ли клик на этот конкретный элемент
    val isClicked = clickedItemsState[item.title] ?: false

    // Анимация смещения
    val offset by animateDpAsState(
        targetValue = if (isClicked) 325.dp else 0.dp,
        animationSpec = tween(durationMillis = 500),
        finishedListener = {
            if (isClicked) {
                navController.navigate(AppRoutes.algorithmSelectionRoute(item.title))
            }
        }, label = ""
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .clickable { viewModel.clickItem(item.title) },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = MaterialTheme.shapes.small
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = androidx.compose.ui.graphics.Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.9f),
                            MaterialTheme.colorScheme.surface.copy(alpha = 0.8f)
                        )
                    )
                )
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.offset(x = offset)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = item.title,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}