package com.example.algorithms.screens.profile

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.SupportAgent
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuItemColors
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.algorithms.R
import com.example.algorithms.di.main_api.UserResponse
import com.example.algorithms.di.main_api.UserUpdateRequest
import com.example.algorithms.navigation.AppRoutes
import com.example.algorithms.ui.theme.BackgroundBottom
import com.example.algorithms.ui.theme.BackgroundTop
import com.example.algorithms.ui.theme.DarkBlue
import com.example.algorithms.ui.theme.ErrorRed
import com.example.algorithms.ui.theme.LightBlue
import com.example.algorithms.ui.theme.PrimaryBlue
import com.example.algorithms.ui.theme.SoftBlue
import com.example.algorithms.ui.theme.SoftOrange
import com.example.algorithms.utils.AuthState
import com.example.algorithms.utils.TokenManager
import com.example.algorithms.viewmodels.profile.AuthViewModel
import com.example.algorithms.viewmodels.profile.ProfileViewModel
import org.koin.androidx.compose.koinViewModel

//@Composable
//fun ProfileScreen(
//    viewModel: ProfileViewModel = koinViewModel(),
//    authViewModel: AuthViewModel = koinViewModel(),
//    navController: NavController
//) {
//    var expanded by remember { mutableStateOf(false) }
//    val profileState by viewModel.profileState.collectAsState()
//    val isAuthenticated by AuthState.isAuthenticated.collectAsState()
//    var showEditDialog by remember { mutableStateOf(false) }
//    var showImagePicker by remember { mutableStateOf(false) }
//    val context = LocalContext.current
//    val launcher = rememberLauncherForActivityResult(
//        contract = ActivityResultContracts.GetContent()
//    ) { uri: Uri? -> uri?.let { viewModel.updateProfilePicture(it) } }
//
//    var menuOffset by remember { mutableStateOf(DpOffset.Zero) }
//
//    LaunchedEffect(Unit) {
//        val token = TokenManager.getToken(context)
//        if (token != null) {
//            viewModel.loadProfile()
//        } else {
//            AuthState.setAuthenticated(false)
//        }
//    }
//
//    val density = LocalDensity.current.density
//
//    Box(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(
//                brush = Brush.verticalGradient(
//                    colors = listOf(BackgroundTop, BackgroundBottom)
//                )
//            )
//    ) {
//        if (isAuthenticated) {
//            Column(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .padding(start = 16.dp, top = 62.dp, end = 16.dp, bottom = 16.dp),
//                horizontalAlignment = Alignment.CenterHorizontally
//            ) {
//                Box(
//                    modifier = Modifier
//                        .size(120.dp)
//                        .clip(CircleShape)
//                        .background(SoftBlue)
//                        .clickable { expanded = true }
//                        .onGloballyPositioned { layoutCoordinates ->
//                            val photoBoxBounds = layoutCoordinates.boundsInRoot()
//                            menuOffset = DpOffset(
//                                x = (photoBoxBounds.left / density).dp,
//                                y = (photoBoxBounds.bottom / density).dp
//                            )
//                        },
//                    contentAlignment = Alignment.Center
//                ) {
//                    profileState?.profilePicture?.let { url ->
//                        AsyncImage(
//                            model = ImageRequest.Builder(LocalContext.current)
//                                .data("http://5.35.126.14:8000${url}")
//                                .crossfade(true)
//                                .build(),
//                            contentDescription = "Фото профиля",
//                            modifier = Modifier.fillMaxSize(),
//                            contentScale = ContentScale.Crop
//                        )
//                    } ?: Icon(
//                        imageVector = Icons.Default.Person,
//                        contentDescription = null,
//                        modifier = Modifier.size(60.dp),
//                        tint = MaterialTheme.colorScheme.onPrimaryContainer
//                    )
//
//                    Icon(
//                        imageVector = Icons.Default.Edit,
//                        contentDescription = "Редактировать фото",
//                        modifier = Modifier
//                            .size(24.dp)
//                            .align(Alignment.BottomEnd)
//                            .offset(x = (-12).dp, y = (-8).dp)
//                            .clickable { expanded = true }
//                            .background(Color.White, CircleShape)
//                            .padding(4.dp),
//                        tint = Color.Black
//                    )
//                }
//
//
//                DropdownMenu(
//                    expanded = expanded,
//                    onDismissRequest = { expanded = false },
//                    offset = menuOffset.copy(
//
//                        x = menuOffset.x - 20.dp,
//                        y = if (profileState?.profilePicture != null) {
//                            menuOffset.y + 52.dp
//                        } else {
//                            menuOffset.y + 10.dp
//                        }
//                    )
//                ) {
//
//                    DropdownMenuItem(
//                        text = { Text("Изменить фото") },
//                        onClick = {
//
//                            showImagePicker = true
//                            expanded = false
//                        }
//                    )
//
//
//                    if (profileState?.profilePicture != null) {
//                        DropdownMenuItem(
//                            text = { Text("Удалить фото") },
//                            onClick = {
//                                viewModel.deleteProfilePhoto()
//                                expanded = false
//                            }
//                        )
//                    }
//                }
//
//
//
//
//
//                Text(
//                    text = profileState?.username ?: "",
//                    style = MaterialTheme.typography.titleLarge,
//                    color = Color.Black,
//                    modifier = Modifier.padding(top = 20.dp, bottom = 46.dp)
//                )
//
//
//                val x = 0.7F
//                LearningProgress(x)
//
//
//                Spacer(modifier = Modifier.weight(1f))
//
//                Column(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(bottom = 24.dp),
//                    verticalArrangement = Arrangement.spacedBy(12.dp)
//                ) {
//                    Button(
//                        onClick = { showEditDialog = true },
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .height(56.dp),
//                        colors = ButtonDefaults.buttonColors(
//                            containerColor = PrimaryBlue
//                        ),
//                        shape = RoundedCornerShape(12.dp)
//                    ) {
//                        Icon(
//                            Icons.Default.Edit,
//                            contentDescription = null,
//                            modifier = Modifier.padding(end = 8.dp)
//                        )
//                        Text(
//                            "Редактировать профиль",
//                            style = MaterialTheme.typography.bodyLarge
//                        )
//                    }
//
//                    Row(
//                        modifier = Modifier.fillMaxWidth(),
//                        horizontalArrangement = Arrangement.spacedBy(8.dp)
//                    ) {
//                        OutlinedButton(
//                            onClick = {
//                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/RyazantsevNik/Algorithm.git"))
//
//                                context.startActivity(intent)
//                            },
//                            modifier = Modifier
//                                .weight(1f)
//                                .height(56.dp),
//                            colors = ButtonDefaults.outlinedButtonColors(
//                                contentColor = PrimaryBlue
//                            ),
//                            border = BorderStroke(1.dp, PrimaryBlue),
//                            shape = RoundedCornerShape(12.dp)
//                        ) {
//                            Icon(
//                                Icons.Default.Code,
//                                contentDescription = null,
//                                modifier = Modifier.padding(end = 8.dp)
//                            )
//                            Text(
//                                "GitHub",
//                                style = MaterialTheme.typography.bodyLarge
//                            )
//                        }
//
//                        OutlinedButton(
//                            onClick = { navController.navigate("help_screen") },
//                            modifier = Modifier
//                                .weight(1f)
//                                .height(56.dp),
//                            colors = ButtonDefaults.outlinedButtonColors(
//                                contentColor = PrimaryBlue
//                            ),
//                            border = BorderStroke(1.dp, PrimaryBlue),
//                            shape = RoundedCornerShape(12.dp)
//                        ) {
//                            Icon(
//                                Icons.Default.Info,
//                                contentDescription = null,
//                                modifier = Modifier.padding(end = 8.dp)
//                            )
//                            Text(
//                                "Справка",
//                                style = MaterialTheme.typography.bodyLarge
//                            )
//                        }
//                    }
//
//                    Button(
//                        onClick = {
//                            authViewModel.logout {
//                                navController.navigate("auth") {
//                                    popUpTo("main") { inclusive = true }
//                                }
//                            }
//                        },
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .height(56.dp),
//                        colors = ButtonDefaults.buttonColors(
//                            containerColor = SoftOrange
//                        ),
//                        shape = RoundedCornerShape(12.dp)
//                    ) {
//                        Icon(
//                            Icons.AutoMirrored.Filled.ExitToApp,
//                            contentDescription = null,
//                            modifier = Modifier.padding(end = 8.dp)
//                        )
//                        Text(
//                            "Выйти",
//                            style = MaterialTheme.typography.bodyLarge
//                        )
//                    }
//                }
//            }
//        } else {
//            Column(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .padding(horizontal = 24.dp, vertical = 32.dp),
//                horizontalAlignment = Alignment.CenterHorizontally,
//                verticalArrangement = Arrangement.Center
//            ) {
//
//                Icon(
//                    imageVector = Icons.Filled.AccountCircle,
//                    contentDescription = "Иконка профиля",
//                    modifier = Modifier
//                        .size(120.dp)
//                        .padding(bottom = 24.dp),
//                    tint = DarkBlue
//                )
//
//
//                Text(
//                    text = "Войдите в аккаунт",
//                    style = MaterialTheme.typography.headlineLarge,
//                    color = MaterialTheme.colorScheme.onSurface,
//                    modifier = Modifier.padding(bottom = 16.dp)
//                )
//
//
//                Text(
//                    text = "Чтобы получить доступ к профилю, чату с AI и отслеживать свой прогресс",
//                    style = MaterialTheme.typography.bodyLarge,
//                    color = MaterialTheme.colorScheme.onSurfaceVariant,
//                    textAlign = TextAlign.Center,
//                    lineHeight = 24.sp,
//                    modifier = Modifier.padding(bottom = 40.dp)
//                )
//
//
//                Button(
//                    onClick = { navController.navigate(AppRoutes.AUTH_SCREEN) },
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .height(56.dp),
//                    shape = MaterialTheme.shapes.medium,
//                    elevation = ButtonDefaults.buttonElevation(
//                        defaultElevation = 2.dp,
//                        pressedElevation = 4.dp
//                    ),
//                    colors = ButtonDefaults.buttonColors(
//                        containerColor = DarkBlue,
//                        contentColor = Color.White
//                    )
//                ) {
//                    Text(
//                        text = "Войти в аккаунт",
//                        style = MaterialTheme.typography.labelLarge
//                    )
//                }
//            }
//        }
//    }
//
//    if (showEditDialog) {
//        EditProfileDialog(
//            user = profileState!!,
//            onDismiss = { showEditDialog = false },
//            onSave = { updatedProfile ->
//                viewModel.updateProfile(
//                    UserUpdateRequest(
//                        username = updatedProfile.username,
//                        email = updatedProfile.email,
//                        currentPassword = updatedProfile.currentPassword,
//                        newPassword = updatedProfile.newPassword
//                    )
//                )
//                showEditDialog = false
//            }
//        )
//    }
//
//    if (showImagePicker) {
//        launcher.launch("image/*")
//        showImagePicker = false
//    }
//}

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = koinViewModel(),
    authViewModel: AuthViewModel = koinViewModel(),
    navController: NavController
) {
    val profileState by viewModel.profileState.collectAsState()
    val isAuthenticated by AuthState.isAuthenticated.collectAsState()
    var showEditDialog by remember { mutableStateOf(false) }
    var showImagePicker by remember { mutableStateOf(false) }
    var expanded by remember { mutableStateOf(false) }
    val context = LocalContext.current

    var menuOffset by remember { mutableStateOf(DpOffset.Zero) }
    val density = LocalDensity.current.density

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? -> uri?.let { viewModel.updateProfilePicture(it) } }

    LaunchedEffect(Unit) {
        val token = TokenManager.getToken(context)
        if (token != null) {
            viewModel.loadProfile()
        } else {
            AuthState.setAuthenticated(false)
        }
    }

    if (isAuthenticated) {

        Box(modifier = Modifier.fillMaxSize()) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(210.dp)
                    .background(
                        color = Color(0xFF313030),
                        shape = RoundedCornerShape(bottomStart = 30.dp, bottomEnd = 30.dp)
                    ),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    text = "Профиль",
                    color = Color.White,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 24.dp, bottom = 144.dp)
                )
            }

            // 📸 Фото профиля — ПОВЕРХ карточки
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .align(Alignment.TopCenter)
                    .offset(y = 60.dp) // 📍 Смещение вниз — ПОЛОВИНА фото в фоне, половина над карточкой
                    .zIndex(2f) // обязательно выше карточки
                    .clip(CircleShape)
                    .clickable { expanded = true }
                    .onGloballyPositioned { layoutCoordinates ->
                        val photoBoxBounds = layoutCoordinates.boundsInRoot()
                        menuOffset = DpOffset(
                            x = (photoBoxBounds.left / density).dp,
                            y = (photoBoxBounds.bottom / density).dp
                        )
                    },
                contentAlignment = Alignment.Center
            ) {
                profileState?.profilePicture?.let { url ->
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data("http://5.35.126.14:8000${url}")
                            .crossfade(true)
                            .build(),
                        contentDescription = "Фото профиля",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                } ?: Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    tint = Color.Gray,
                    modifier = Modifier.size(64.dp)
                )

                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Редактировать фото",
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .offset(x = (-8).dp, y = (-8).dp)
                        .size(24.dp)
                        .background(Color.White, CircleShape)
                        .padding(4.dp)
                        .clickable { expanded = true },
                    tint = Color.Black
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 120.dp, start = 16.dp, end = 16.dp, bottom = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // 🧑 Фото профиля + Имя
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp)
                        .zIndex(1f),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(
                        modifier = Modifier.padding(top = 24.dp, start = 24.dp,end = 24.dp, bottom = 12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false },
                            offset = menuOffset.copy(

                                x = menuOffset.x - 52.dp,
                                y = if (profileState?.profilePicture != null) {
                                    menuOffset.y - 278.dp
                                } else {
                                    menuOffset.y - 268.dp
                                }
                            )
                        ) {

                            DropdownMenuItem(
                                colors = MenuItemColors(
                                    textColor = Color.Black,
                                    leadingIconColor = LightBlue,
                                    trailingIconColor = LightBlue,
                                    disabledTextColor = Color.Gray,
                                    disabledLeadingIconColor = Color.Gray,
                                    disabledTrailingIconColor = Color.Gray,
                                ),
                                text = { Text("Изменить фото") },
                                onClick = {
                                    showImagePicker = true
                                    expanded = false
                                }
                            )


                            if (profileState?.profilePicture != null) {
                                DropdownMenuItem(
                                    text = { Text("Удалить фото") },
                                    onClick = {
                                        viewModel.deleteProfilePhoto()
                                        expanded = false
                                    }
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        Text(
                            text = profileState?.username ?: "Пользователь",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = profileState?.email ?: "email@gmail.com",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Normal,
                            fontSize = 16.sp
                        )

                        Spacer(modifier = Modifier.height(8.dp))
                        LearningProgress(0.7f)
                    }
                }

                // 📋 Меню действий
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    ProfileMenuItem(
                        icon = Icons.Default.Edit,
                        title = "Редактировать профиль",
                        onClick = { showEditDialog = true }
                    )

                    ProfileMenuItem(
                        icon = Icons.Default.Code,
                        title = "GitHub",
                        onClick = {
                            val intent = Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse("https://github.com/RyazantsevNik/Algorithm.git")
                            )
                            context.startActivity(intent)
                        }
                    )

                    ProfileMenuItem(
                        icon = Icons.Default.Info,
                        title = "Справка",
                        onClick = { navController.navigate("help_screen") }
                    )

                    ProfileMenuItem(
                        icon = Icons.AutoMirrored.Filled.ExitToApp,
                        title = "Выйти",
                        iconTint = SoftOrange,
                        onClick = {
                            authViewModel.logout {
                                navController.navigate("auth") {
                                    popUpTo("main") { inclusive = true }
                                }
                            }
                        }
                    )
                }
            }
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Icon(
                imageVector = Icons.Filled.AccountCircle,
                contentDescription = "Иконка профиля",
                modifier = Modifier
                    .size(120.dp)
                    .padding(bottom = 24.dp),
                tint = DarkBlue
            )


            Text(
                text = "Войдите в аккаунт",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(bottom = 16.dp)
            )


            Text(
                text = "Чтобы получить доступ к профилю, чату с AI и отслеживать свой прогресс",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                lineHeight = 24.sp,
                modifier = Modifier.padding(bottom = 40.dp)
            )


            Button(
                onClick = { navController.navigate(AppRoutes.AUTH_SCREEN) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = MaterialTheme.shapes.medium,
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 2.dp,
                    pressedElevation = 4.dp
                ),
                colors = ButtonDefaults.buttonColors(
                    containerColor = DarkBlue,
                    contentColor = Color.White
                )
            ) {
                Text(
                    text = "Войти в аккаунт",
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
    }

    if (showEditDialog) {
        EditProfileDialog(
            user = profileState!!,
            onDismiss = { showEditDialog = false },
            onSave = { updatedProfile ->
                viewModel.updateProfile(
                    UserUpdateRequest(
                        username = updatedProfile.username,
                        email = updatedProfile.email,
                        currentPassword = updatedProfile.currentPassword,
                        newPassword = updatedProfile.newPassword
                    )
                )
                showEditDialog = false
            }
        )
    }

    if (showImagePicker) {
        launcher.launch("image/*")
        showImagePicker = false
    }
}

@Composable
fun ProfileMenuItem(
    icon: ImageVector,
    title: String,
    iconTint: Color = PrimaryBlue,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconTint
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                color = Color.Black
            )
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = Color.Gray
            )
        }
    }
}


@Composable
fun EditProfileDialog(
    user: UserResponse,
    onDismiss: () -> Unit,
    onSave: (UserUpdateRequest) -> Unit
) {
    var username by remember { mutableStateOf(user.username) }
    var email by remember { mutableStateOf(user.email) }
    var currentPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordError by remember { mutableStateOf<String?>(null) }

    val isPasswordSectionTouched =
        currentPassword.isNotEmpty() || newPassword.isNotEmpty() || confirmPassword.isNotEmpty()
    val isPasswordValid = when {
        isPasswordSectionTouched && (currentPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) -> {
            passwordError = "Все поля пароля обязательны"
            false
        }

        isPasswordSectionTouched && (newPassword.length < 6 || currentPassword.length < 6) -> {
            passwordError = "Пароли должны содержать не менее 6 символов"
            false
        }

        isPasswordSectionTouched && newPassword != confirmPassword -> {
            passwordError = "Новый пароль и подтверждение не совпадают"
            false
        }

        isPasswordSectionTouched && newPassword == currentPassword -> {
            passwordError = "Новый пароль должен отличаться от текущего"
            false
        }

        else -> {
            passwordError = null
            true
        }
    }

    val isSaveEnabled = passwordError == null

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "Редактировать профиль",
                    style = MaterialTheme.typography.titleLarge.copy(color = LightBlue),
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                OutlinedTextField(
                    value = username,
                    onValueChange = { username = it },
                    label = { Text("Имя пользователя") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                )

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                )

                Text(
                    "Изменение пароля (необязательно)",
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
                )

                OutlinedTextField(
                    value = currentPassword,
                    onValueChange = { currentPassword = it },
                    label = { Text("Текущий пароль") },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    isError = passwordError != null
                )

                OutlinedTextField(
                    value = newPassword,
                    onValueChange = { newPassword = it },
                    label = { Text("Новый пароль") },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    isError = passwordError != null
                )

                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    label = { Text("Подтвердите новый пароль") },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    isError = passwordError != null
                )

                passwordError?.let {
                    Text(
                        text = it,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(
                        onClick = onDismiss,
                        colors = ButtonColors(
                            containerColor = ErrorRed,
                            contentColor = Color.White,
                            disabledContentColor = Color.LightGray,
                            disabledContainerColor = Color.DarkGray
                        )
                    ) {
                        Text("Отмена")
                    }

                    Button(
                        onClick = {
                            val userUpdateRequest = UserUpdateRequest(
                                username = username,
                                email = email,
                                currentPassword = currentPassword.ifEmpty { null },
                                newPassword = newPassword.ifEmpty { null }
                            )

                            Log.d("EditProfileDialog", "Sending request: $userUpdateRequest")
                            onSave(userUpdateRequest)
                            onDismiss()
                        },
                        enabled = isSaveEnabled,
                        modifier = Modifier.padding(start = 8.dp),
                        colors = ButtonColors(
                            containerColor = LightBlue,
                            contentColor = Color.White,
                            disabledContentColor = Color.LightGray,
                            disabledContainerColor = Color.DarkGray
                        )
                    ) {
                        Text("Сохранить")
                    }
                }
            }
        }
    }
}

@Composable
fun LearningProgress(progress: Float) {
    val animatedProgress = remember { Animatable(0f) }

    LaunchedEffect(progress) {
        animatedProgress.animateTo(
            targetValue = progress,
            animationSpec = tween(durationMillis = 1000, easing = FastOutSlowInEasing)
        )
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(12.dp)
                .clip(RoundedCornerShape(6.dp))
                .background(SoftBlue)
                .shadow(elevation = 4.dp, shape = RoundedCornerShape(6.dp))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(fraction = animatedProgress.value)
                    .height(12.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(
                        brush = Brush.horizontalGradient(
                            listOf(Color(0xFF4A90E2), Color(0xFF007AFF))
                        )
                    )
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Прогресс обучения: ${(progress * 100).toInt()}%",
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            ),
            color = Color(0xFF007AFF),
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HelpScreen(navController: NavController) {
    val context = LocalContext.current
    val appVersion = remember { getAppVersion(context) }

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                title = { Text("Справка") },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = SoftBlue
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo_for_app),
                contentDescription = "App Logo",
                modifier = Modifier
                    .size(180.dp)
                    .padding(vertical = 24.dp)
            )

            HelpSection(title = "Основные функции") {
                Text(
                    text = "• Интерактивные уроки по алгоритмам\n" +
                            "• Симуляция работы алгоритмов с тонкой настройкой\n" +
                            "• Возможность общения с ИИ\n" +
                            "• Система прогресса в личном профиле",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            HelpSection(title = "Как начать работу") {
                Column {
                    Text(
                        text = "1. Зарегистрируйтесь или войдите в аккаунт\n" +
                                "2. Выберите курс в разделе 'Алгоритмы'\n" +
                                "3. Обучайтесь алгоритмам и получайте мгновенный фидбэк\n" +
                                "4. Попробуйте настроить и запустить собственную симуляцию алгоритма\n" +
                                "5. При возникновении вопросов, спросите их у Ai чата\n" +
                                "6. Отслеживайте прогресс в личном кабинете",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            HelpSection(title = "Часто задаваемые вопросы") {
                ExpandableFAQ(
                    question = "Как сбросить прогресс?",
                    answer = "Перейдите в настройки профиля и выберите 'Сбросить прогресс'"
                )
                ExpandableFAQ(
                    question = "Какая нейросеть используется в Ai чат?",
                    answer = "Для чата с нейросетью используется LLM модель - DeepSeek-R1"
                )
                ExpandableFAQ(
                    question = "Как связаться с разработчиками?",
                    answer = "Нажмите кнопку 'Написать в поддержку' ниже или напишите на ryazantsevnikita8@gmail.com"
                )
            }

            HelpSection(title = "") {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    HorizontalDivider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 4.dp),
                        thickness = 1.dp
                    )
                    Text(
                        text = "Версия $appVersion",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "© 2025 Nikita Ryazantsev",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Button(
                onClick = { context.openSupportEmail() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary
                )
            ) {
                Icon(
                    Icons.Outlined.SupportAgent,
                    contentDescription = "Support",
                    modifier = Modifier.size(ButtonDefaults.IconSize)
                )
                Spacer(modifier = Modifier.width(ButtonDefaults.IconSpacing))
                Text("Написать в поддержку")
            }
        }
    }
}

@Composable
private fun HelpSection(
    title: String,
    content: @Composable () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            color = DarkBlue
        )
        Spacer(modifier = Modifier.height(8.dp))
        content()
    }
}

@Composable
private fun ExpandableFAQ(question: String, answer: String) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        onClick = { expanded = !expanded }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = question,
                    style = MaterialTheme.typography.bodyLarge
                )
                Icon(
                    imageVector = if (expanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                    contentDescription = "Expand"
                )
            }
            if (expanded) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = answer,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

private fun getAppVersion(context: Context): String {
    return try {
        val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
        packageInfo.versionName ?: "1.0.0"
    } catch (e: Exception) {
        "1.0.0"
    }
}

private fun Context.openSupportEmail() {
    val intent = Intent(Intent.ACTION_SENDTO).apply {
        data = Uri.parse("mailto:ryazantsevnikita8@gmail.com")
        putExtra(Intent.EXTRA_SUBJECT, "Вопрос по приложению")
    }
    startActivity(Intent.createChooser(intent, "Выберите почтовый клиент"))
}