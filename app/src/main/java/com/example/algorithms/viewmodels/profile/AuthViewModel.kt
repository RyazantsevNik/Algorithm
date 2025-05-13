package com.example.algorithms.viewmodels.profile

import android.app.Application
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.algorithms.di.main_api.AuthApi
import com.example.algorithms.di.main_api.LoginRequest
import com.example.algorithms.di.main_api.ProfileApi
import com.example.algorithms.di.main_api.RegisterRequest
import com.example.algorithms.di.main_api.UserResponse
import com.example.algorithms.utils.AuthState
import com.example.algorithms.utils.TokenManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

sealed class AuthResult {
    data object Loading : AuthResult()
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

    private fun handleError(e: Exception): String {
        return when (e) {
            is HttpException -> {
                when (e.code()) {
                    401 -> "Неверное имя пользователя или пароль"
                    400 -> "Проверьте правильность введенных данных"
                    409 -> "Пользователь с таким именем или email уже существует"
                    500 -> "Ошибка сервера. Попробуйте позже"
                    else -> "Произошла ошибка. Попробуйте позже"
                }
            }
            is ConnectException, is UnknownHostException -> "Нет подключения к интернету"
            is SocketTimeoutException -> "Превышено время ожидания ответа от сервера"
            else -> "Произошла неизвестная ошибка. Попробуйте позже"
        }
    }

    fun register(username: String, email: String, password: String) {
        viewModelScope.launch {
            try {
                _authResult.value = AuthResult.Loading
                val response = authApi.register(
                    RegisterRequest(username, email, password)
                )
                TokenManager.saveToken(application, response.accessToken)
                AuthState.setAuthenticated(true)
                _authResult.value = AuthResult.Success(
                    user = response.user,
                    token = response.accessToken
                )
            } catch (e: Exception) {
                _authResult.value = AuthResult.Error(handleError(e))
            }
        }
    }

    fun login(username: String, password: String) {
        viewModelScope.launch {
            try {
                _authResult.value = AuthResult.Loading
                val loginResponse = authApi.login(LoginRequest(username, password))
                
                TokenManager.saveToken(application, loginResponse.accessToken)
                val userProfile = profileApi.getProfile("Bearer ${loginResponse.accessToken}")
                Log.d("TokenAuth", loginResponse.accessToken)
                
                AuthState.setAuthenticated(true)
                _authResult.value = AuthResult.Success(
                    user = userProfile,
                    token = loginResponse.accessToken
                )
            } catch (e: Exception) {
                AuthState.setAuthenticated(false)
                _authResult.value = AuthResult.Error(handleError(e))
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
                _authResult.value = AuthResult.Error(handleError(e))
            }
        }
    }
}