package com.example.algorithms.screens.auth

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.algorithms.navigation.AppRoutes
import com.example.algorithms.ui.theme.DarkBlue
import com.example.algorithms.ui.theme.ErrorRed
import com.example.algorithms.ui.theme.LightBlue
import com.example.algorithms.ui.theme.PrimaryBlue
import com.example.algorithms.utils.Validators
import com.example.algorithms.viewmodels.profile.AuthResult
import com.example.algorithms.viewmodels.profile.AuthViewModel
import org.koin.androidx.compose.koinViewModel
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff

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
    
    var usernameError by remember { mutableStateOf<String?>(null) }
    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }

    val alphanumericFilter = { text: String ->
        text.filter { it.isLetterOrDigit() }
    }

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
                navController.navigate(AppRoutes.PROFILE) {
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
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = null,
                modifier = Modifier
                    .size(100.dp)
                    .padding(16.dp),
                tint = DarkBlue
            )

            Text(
                text = if (isRegistration) "Регистрация" else "Вход",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 72.dp)
            )

            OutlinedTextField(
                value = username,
                onValueChange = { 
                    username = alphanumericFilter(it)
                    if (isRegistration) {
                        val validation = Validators.validateUsername(username)
                        usernameError = validation.errorMessage
                    }
                },
                label = { Text("Имя пользователя") },
                isError = usernameError != null,
                supportingText = {
                    if (usernameError != null) {
                        Text(
                            text = usernameError!!,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = PrimaryBlue,
                    unfocusedBorderColor = LightBlue,
                    errorBorderColor = ErrorRed,
                    focusedTextColor = Color.Black
                )
            )

            if (isRegistration) {
                OutlinedTextField(
                    value = email,
                    onValueChange = { 
                        email = it
                        val validation = Validators.validateEmail(email)
                        emailError = validation.errorMessage
                    },
                    label = { Text("Email") },
                    isError = emailError != null,
                    supportingText = {
                        if (emailError != null) {
                            Text(
                                text = emailError!!,
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = PrimaryBlue,
                        unfocusedBorderColor = LightBlue,
                        errorBorderColor = ErrorRed
                    )
                )
            }

            OutlinedTextField(
                value = password,
                onValueChange = { 
                    password = alphanumericFilter(it)
                    val validation = Validators.validatePassword(password)
                    passwordError = validation.errorMessage
                },
                label = { Text("Пароль") },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                isError = passwordError != null,
                supportingText = {
                    if (passwordError != null) {
                        Text(
                            text = passwordError!!,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = if (passwordVisible) 
                                Icons.Default.VisibilityOff 
                            else 
                                Icons.Default.Visibility,
                            contentDescription = if (passwordVisible) "Скрыть пароль" else "Показать пароль"
                        )
                    }
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = PrimaryBlue,
                    unfocusedBorderColor = LightBlue,
                    errorBorderColor = ErrorRed,
                    focusedTextColor = Color.Black
                )
            )

            Button(
                onClick = {
                    if (isRegistration) {
                        val usernameValidation = Validators.validateUsername(username)
                        val emailValidation = Validators.validateEmail(email)
                        val passwordValidation = Validators.validatePassword(password)

                        usernameError = usernameValidation.errorMessage
                        emailError = emailValidation.errorMessage
                        passwordError = passwordValidation.errorMessage

                        if (usernameValidation.isValid && 
                            emailValidation.isValid && 
                            passwordValidation.isValid) {
                            viewModel.register(username, email, password)
                        }
                    } else {
                        val usernameValidation = Validators.validateUsername(username)
                        val passwordValidation = Validators.validatePassword(password)

                        usernameError = usernameValidation.errorMessage
                        passwordError = passwordValidation.errorMessage

                        if (usernameValidation.isValid && passwordValidation.isValid) {
                            viewModel.login(username, password)
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = PrimaryBlue,
                    contentColor = Color.White
                ),
                enabled = when {
                    isRegistration -> username.isNotEmpty() && 
                                    email.isNotEmpty() && 
                                    password.isNotEmpty() &&
                                    usernameError == null &&
                                    emailError == null &&
                                    passwordError == null
                    else -> username.isNotEmpty() && 
                            password.isNotEmpty() &&
                            usernameError == null &&
                            passwordError == null
                }
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

            TextButton(
                onClick = { 
                    isRegistration = !isRegistration
                    username = ""
                    email = ""
                    password = ""
                    usernameError = null
                    emailError = null
                    passwordError = null
                },
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Text(
                    if (isRegistration) 
                        "Уже есть аккаунт? Войти" 
                    else 
                        "Нет аккаунта? Зарегистрироваться",
                    color = Color.DarkGray
                )
            }
        }
    }
}