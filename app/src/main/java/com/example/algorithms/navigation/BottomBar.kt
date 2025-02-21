package com.example.algorithms.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.algorithms.ui.theme.LightBlue
import com.example.algorithms.ui.theme.PrimaryBlue
import com.example.algorithms.ui.theme.TextSecondary
import com.example.algorithms.ui.theme.SoftOrange

@Composable
fun BottomBar(navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(8.dp)
            .height(65.dp)
            .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)),
        containerColor = Color.White,
        tonalElevation = 8.dp
    ) {
        // Главная
        NavigationBarItem(
            icon = {
                NavigationItemIcon(
                    icon = Icons.Default.Home,
                    isSelected = currentRoute == AppRoutes.HOME,
                    contentDescription = "Главная"
                )
            },
            label = { NavigationItemText(text = "Главная", isSelected = currentRoute == AppRoutes.HOME) },
            selected = currentRoute == AppRoutes.HOME,
            onClick = { 
                navController.navigate(AppRoutes.HOME) {
                    popUpTo(navController.graph.startDestinationId)
                    launchSingleTop = true
                }
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = PrimaryBlue,
                unselectedIconColor = TextSecondary,
                indicatorColor = LightBlue.copy(alpha = 0.2f)
            )
        )

        // Алгоритмы
        NavigationBarItem(
            icon = {
                NavigationItemIcon(
                    icon = Icons.Default.Add,
                    isSelected = currentRoute == AppRoutes.ALGORITHMS,
                    contentDescription = "Алгоритмы"
                )
            },
            label = { NavigationItemText(text = "Алгоритмы", isSelected = currentRoute == AppRoutes.ALGORITHMS) },
            selected = currentRoute == AppRoutes.ALGORITHMS,
            onClick = { 
                navController.navigate(AppRoutes.ALGORITHMS) {
                    popUpTo(navController.graph.startDestinationId)
                    launchSingleTop = true
                }
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = PrimaryBlue,
                unselectedIconColor = TextSecondary,
                indicatorColor = LightBlue.copy(alpha = 0.2f)
            )
        )

        // AI Чат
        NavigationBarItem(
            icon = {
                NavigationItemIcon(
                    icon = Icons.Default.Create,
                    isSelected = currentRoute == AppRoutes.AI_CHAT,
                    contentDescription = "AI Чат",
                    showBadge = true
                )
            },
            label = { NavigationItemText(text = "AI Чат", isSelected = currentRoute == AppRoutes.AI_CHAT) },
            selected = currentRoute == AppRoutes.AI_CHAT,
            onClick = { 
                navController.navigate(AppRoutes.AI_CHAT) {
                    popUpTo(navController.graph.startDestinationId)
                    launchSingleTop = true
                }
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = PrimaryBlue,
                unselectedIconColor = TextSecondary,
                indicatorColor = LightBlue.copy(alpha = 0.2f)
            )
        )

        // Профиль
        NavigationBarItem(
            icon = {
                NavigationItemIcon(
                    icon = Icons.Default.Person,
                    isSelected = currentRoute == AppRoutes.PROFILE,
                    contentDescription = "Профиль"
                )
            },
            label = { NavigationItemText(text = "Профиль", isSelected = currentRoute == AppRoutes.PROFILE) },
            selected = currentRoute == AppRoutes.PROFILE,
            onClick = { 
                navController.navigate(AppRoutes.PROFILE) {
                    popUpTo(navController.graph.startDestinationId)
                    launchSingleTop = true
                }
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = PrimaryBlue,
                unselectedIconColor = TextSecondary,
                indicatorColor = LightBlue.copy(alpha = 0.2f)
            )
        )
    }
}

@Composable
private fun NavigationItemIcon(
    icon: ImageVector,
    isSelected: Boolean,
    contentDescription: String?,
    showBadge: Boolean = false
) {
    Box(
        modifier = Modifier
            .size(if (isSelected) 32.dp else 24.dp)
            .background(
                color = if (isSelected) LightBlue.copy(alpha = 0.2f) else Color.Transparent,
                shape = CircleShape
            )
            .padding(if (isSelected) 6.dp else 0.dp),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = if (isSelected) PrimaryBlue else TextSecondary
        )
        if (showBadge && !isSelected) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .background(SoftOrange, CircleShape)
                    .align(Alignment.TopEnd)
            )
        }
    }
}

@Composable
private fun NavigationItemText(
    text: String,
    isSelected: Boolean
) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelSmall.copy(
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            fontSize = if (isSelected) 12.sp else 11.sp
        ),
        color = if (isSelected) PrimaryBlue else TextSecondary
    )
}
