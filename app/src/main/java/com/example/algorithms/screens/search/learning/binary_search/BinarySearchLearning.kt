package com.example.algorithms.screens.search.learning.binary_search

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.algorithms.R
import com.example.algorithms.screens.sorting.learning.IntroScreen
import com.example.algorithms.viewmodels.step_search.BinarySearchViewModel

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun BinarySearchLearning(navController: NavHostController) {
    var showIntro by remember { mutableStateOf(true) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Бинарный поиск",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            color = Color(0xFF0D47A1)
                        ),
                        maxLines = 1
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_back_button),
                            contentDescription = "Back",
                            tint = Color.Unspecified,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                },

                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF90CAF9)
                ),
                modifier = Modifier.shadow(elevation = 2.dp)
            )
        },
        content = {
            if (showIntro) {
                IntroScreen(
                    algorithmTitle = "Бинарный поиск",
                    principleContent = "Алгоритм бинарного поиска находит искомый элемент путём деления массива пополам на каждом шаге и выбора той половины, где может находиться элемент.",
                    passesContent = "Алгоритм продолжает делить массив на половины, сужая диапазон поиска, пока не найдет элемент или не останется одна позиция.",
                    efficiencyContent = "Время работы бинарного поиска — O(log n). Это эффективный алгоритм для отсортированных массивов."
                ) { showIntro = false }
            } else {
                BinarySearchVisualizationScreen(navController)
            }
        }
    )
}