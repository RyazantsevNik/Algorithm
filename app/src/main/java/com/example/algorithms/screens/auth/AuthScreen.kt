package com.example.algorithms.screens.auth

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.algorithms.navigation.AppRoutes
import com.example.algorithms.viewmodels.AuthResult
import com.example.algorithms.viewmodels.AuthViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun AuthScreen(
    navController: NavController,
    viewModel: AuthViewModel = koinViewModel()
) {
    var isRegistration by remember { mutableStateOf(false) }
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    
    val authResult by viewModel.authResult.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(authResult) {
        when (authResult) {
            is AuthResult.Success -> {
                Toast.makeText(
                    context,
                    if (isRegistration) "Регистрация успешна!" else "Вход выполнен успешно!",
                    Toast.LENGTH_SHORT
                ).show()
                navController.navigate(AppRoutes.HOME) {
                    popUpTo(AppRoutes.AUTH_SCREEN) { inclusive = true }
                }
            }
            is AuthResult.Error -> {
                Toast.makeText(context, (authResult as AuthResult.Error).message, Toast.LENGTH_LONG).show()
            }
            else -> {}
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Логотип или иконка
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = null,
                modifier = Modifier
                    .size(100.dp)
                    .padding(16.dp),
                tint = MaterialTheme.colorScheme.primary
            )

            // Заголовок
            Text(
                text = if (isRegistration) "Регистрация" else "Вход",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            // Поля ввода
            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Имя пользователя") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                leadingIcon = {
                    Icon(Icons.Default.Person, contentDescription = null)
                }
            )

            if (isRegistration) {
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    leadingIcon = {
                        Icon(Icons.Default.Email, contentDescription = null)
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email
                    )
                )
            }

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Пароль") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                leadingIcon = {
                    Icon(Icons.Default.Lock, contentDescription = null)
                },
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            if (passwordVisible) Icons.Default.AddCircle
                            else Icons.Default.Add,
                            contentDescription = null
                        )
                    }
                },
                visualTransformation = if (passwordVisible) 
                    VisualTransformation.None 
                else 
                    PasswordVisualTransformation()
            )

            // Кнопка действия
            Button(
                onClick = {
                    if (isRegistration) {
                        viewModel.register(username, email, password)
                    } else {
                        viewModel.login(username, password)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                if (authResult is AuthResult.Loading) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(24.dp)
                    )
                } else {
                    Text(if (isRegistration) "Зарегистрироваться" else "Войти")
                }
            }

            // Переключатель между регистрацией и входом
            TextButton(
                onClick = { 
                    isRegistration = !isRegistration
                    username = ""
                    email = ""
                    password = ""
                },
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Text(
                    if (isRegistration) 
                        "Уже есть аккаунт? Войти" 
                    else 
                        "Нет аккаунта? Зарегистрироваться"
                )
            }
        }
    }
}