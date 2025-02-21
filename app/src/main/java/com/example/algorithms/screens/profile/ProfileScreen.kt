package com.example.algorithms.screens.profile

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.algorithms.di.chat_api.UserResponse
import com.example.algorithms.navigation.AppRoutes
import com.example.algorithms.ui.theme.BackgroundBottom
import com.example.algorithms.ui.theme.BackgroundTop
import com.example.algorithms.ui.theme.DarkBlue
import com.example.algorithms.ui.theme.PrimaryBlue
import com.example.algorithms.ui.theme.SoftBlue
import com.example.algorithms.ui.theme.SoftOrange
import com.example.algorithms.utils.AuthState
import com.example.algorithms.utils.TokenManager
import com.example.algorithms.viewmodels.AuthViewModel
import com.example.algorithms.viewmodels.ProfileViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = koinViewModel(),
    authViewModel: AuthViewModel = koinViewModel(),
    navController: NavController
) {
    val profileState by viewModel.profileState.collectAsState()
    //val error by viewModel.error.collectAsState()
    val isAuthenticated by AuthState.isAuthenticated.collectAsState()
    var showEditDialog by remember { mutableStateOf(false) }
    var showImagePicker by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { viewModel.updateProfilePicture(it) }
    }

    LaunchedEffect(Unit) {
        val token = TokenManager.getToken(context)
        if (token != null) {
            viewModel.loadProfile()
        } else {
            AuthState.setAuthenticated(false)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(BackgroundTop, BackgroundBottom)
                )
            )
    ) {
        if (isAuthenticated) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 16.dp, top = 62.dp, end = 16.dp, bottom = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .background(SoftBlue)
                        .clickable { showImagePicker = true },
                    contentAlignment = Alignment.Center
                ) {
                    profileState?.profilePicture?.let { url ->
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data("http://10.0.2.2:8000${url}")
                                .crossfade(true)
                                .build(),
                            contentDescription = "Фото профиля",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } ?: Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        modifier = Modifier.size(60.dp),
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }

                Text(
                    text = profileState?.username ?: "",
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.Black,
                    modifier = Modifier.padding(top = 20.dp, bottom = 46.dp)
                )


                val x = 0.7F
                LearningProgress(x)


                Spacer(modifier = Modifier.weight(1f))  

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp), 
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = { showEditDialog = true },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp), 
                        colors = ButtonDefaults.buttonColors(
                            containerColor = PrimaryBlue
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(
                            Icons.Default.Edit,
                            contentDescription = null,
                            modifier = Modifier.padding(end = 8.dp)
                        )
                        Text(
                            "Редактировать профиль",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = { /* TODO */ },
                            modifier = Modifier
                                .weight(1f)
                                .height(56.dp), 
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = PrimaryBlue
                            ),
                            border = BorderStroke(1.dp, PrimaryBlue),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(
                                Icons.Default.Code,
                                contentDescription = null,
                                modifier = Modifier.padding(end = 8.dp)
                            )
                            Text(
                                "GitHub",
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }

                        OutlinedButton(
                            onClick = { /* TODO */ },
                            modifier = Modifier
                                .weight(1f)
                                .height(56.dp), 
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = PrimaryBlue
                            ),
                            border = BorderStroke(1.dp, PrimaryBlue),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(
                                Icons.Default.Info,
                                contentDescription = null,
                                modifier = Modifier.padding(end = 8.dp)
                            )
                            Text(
                                "Справка",
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }
                    
                    Button(
                        onClick = {
                            authViewModel.logout {
                                navController.navigate("auth") {
                                    popUpTo("main") { inclusive = true }
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp), 
                        colors = ButtonDefaults.buttonColors(
                            containerColor = SoftOrange
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(
                            Icons.AutoMirrored.Filled.ExitToApp,
                            contentDescription = null,
                            modifier = Modifier.padding(end = 8.dp)
                        )
                        Text(
                            "Выйти",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    modifier = Modifier
                        .size(100.dp)
                        .padding(bottom = 16.dp),
                    tint = DarkBlue
                )

                Text(
                    "Войдите в аккаунт",
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(bottom = 24.dp)
                )

                Text(
                    "Чтобы получить доступ к профилю и отслеживать свой прогресс",
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 32.dp)
                )

                Button(
                    onClick = {
                        navController.navigate(AppRoutes.AUTH_SCREEN)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = DarkBlue
                    ),
                    border = BorderStroke(1.dp, DarkBlue)
                ) {
                    Text("Войти в аккаунт")
                }
            }
        }
    }

    if (showEditDialog) {
        EditProfileDialog(
            user = profileState!!,
            onDismiss = { showEditDialog = false },
            onSave = { updatedProfile ->
                viewModel.updateProfile(updatedProfile)
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
fun EditProfileDialog(
    user: UserResponse,  
    onDismiss: () -> Unit,
    onSave: (UserResponse) -> Unit  
) {
    var username by remember { mutableStateOf(user.username) }
    var email by remember { mutableStateOf(user.email) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    "Редактировать профиль",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                OutlinedTextField(
                    value = username,
                    onValueChange = { username = it },
                    label = { Text("Имя пользователя") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                )

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Отмена")
                    }
                    Button(
                        onClick = {
                            onSave(
                                UserResponse(
                                    id = user.id,
                                    username = username,
                                    email = email,
                                    profilePicture = user.profilePicture
                                )
                            )
                        },
                        modifier = Modifier.padding(start = 8.dp)
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