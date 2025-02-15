package com.example.algorithms.viewmodels

import android.app.Application
import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.algorithms.di.chat_api.AuthApi
import com.example.algorithms.di.chat_api.LoginRequest
import com.example.algorithms.di.chat_api.RegisterRequest
import com.example.algorithms.di.chat_api.UserResponse
import com.example.algorithms.di.chat_api.ProfileApi
import com.example.algorithms.navigation.AppRoutes
import com.example.algorithms.utils.TokenManager
import com.example.algorithms.utils.AuthState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class AuthResult {
    object Loading : AuthResult()
    data class Success(
        val user: UserResponse,
        val token: String
    ) : AuthResult()
    data class Error(val message: String) : AuthResult()
}

class AuthViewModel(
    private val authApi: AuthApi,
    private val profileApi: ProfileApi,
    private val application: Application
) : ViewModel() {
    private val _authResult = MutableStateFlow<AuthResult?>(null)
    val authResult: StateFlow<AuthResult?> = _authResult

    fun register(username: String, email: String, password: String) {
        viewModelScope.launch {
            try {
                _authResult.value = AuthResult.Loading
                val response = authApi.register(
                    RegisterRequest(username, email, password)
                )
                TokenManager.saveToken(application, response.access_token)
                AuthState.setAuthenticated(true)
                _authResult.value = AuthResult.Success(
                    user = response.user,
                    token = response.access_token
                )
            } catch (e: Exception) {
                _authResult.value = AuthResult.Error(e.message ?: "Неизвестная ошибка")
            }
        }
    }

    fun login(username: String, password: String) {
        viewModelScope.launch {
            try {
                _authResult.value = AuthResult.Loading
                val loginResponse = authApi.login(LoginRequest(username, password))
                
                TokenManager.saveToken(application, loginResponse.access_token)
                val userProfile = profileApi.getProfile("Bearer ${loginResponse.access_token}")
                
                AuthState.setAuthenticated(true)
                _authResult.value = AuthResult.Success(
                    user = userProfile,
                    token = loginResponse.access_token
                )
            } catch (e: Exception) {
                AuthState.setAuthenticated(false)
                _authResult.value = AuthResult.Error(e.message ?: "Ошибка входа")
            }
        }
    }

    fun logout(onLogoutComplete: () -> Unit) {
        viewModelScope.launch {
            try {
                TokenManager.clearToken(application)
                AuthState.setAuthenticated(false)
                _authResult.value = null
                onLogoutComplete()
            } catch (e: Exception) {
                _authResult.value = AuthResult.Error(e.message ?: "Ошибка выхода")
            }
        }
    }

//    private fun saveAuthToken(token: String) {
//        context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
//            .edit()
//            .putString("auth_token", token)
//            .apply()
//    }

    private fun saveToken(token: String) {
        // TODO: Сохранить токен в SharedPreferences
        // Это нужно будет реализовать позже
    }
}