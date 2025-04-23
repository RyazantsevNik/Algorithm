package com.example.algorithms.screens.search.learning.linear_search

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

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LinearSearchLearning(navController: NavHostController) {
    var showIntro by remember { mutableStateOf(true) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Линейный поиск",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            color = Color(0xFF0D47A1) // Тёмно-синий для заголовка
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
                Spacer(modifier = Modifier.height(20.dp))
                IntroScreen(
                    algorithmTitle = "Линейный поиск",
                    principleContent = "Линейный поиск последовательно проверяет каждый элемент массива, начиная с первого, до тех пор, пока не будет найден искомый элемент или не будет пройден весь массив.",
                    passesContent = "На каждом шаге алгоритм сравнивает текущий элемент с целевым значением. Если они совпадают — поиск завершён, иначе — переходит к следующему элементу.",
                    efficiencyContent = "В худшем случае линейный поиск выполняет O(n) сравнений. Он прост в реализации и неэффективен на больших массивах."
                ) { showIntro = false }
            } else {
                LinearSearchVisualizationScreen(navController)
            }
        }
    )
}