package com.example.algorithms.viewmodels

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.algorithms.data.profile_data.Profile
import com.example.algorithms.di.chat_api.ProfileApi
import com.example.algorithms.di.chat_api.UserProfile
import com.example.algorithms.di.chat_api.UserResponse
import com.example.algorithms.utils.TokenManager
import com.example.algorithms.utils.AuthState

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val profileApi: ProfileApi,
    private val application: Application
) : ViewModel() {
    private val _profileState = MutableStateFlow<UserResponse?>(null)
    val profileState: StateFlow<UserResponse?> = _profileState

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private var isInitialized = false

    init {
        loadProfile()
    }

    fun loadProfile() {
        if (_profileState.value != null) return  // Если профиль уже загружен, не делаем повторный запрос
        
        viewModelScope.launch {
            try {
                val token = TokenManager.getToken(application)
                if (token != null) {
                    val bearerToken = "Bearer $token"
                    val profile = profileApi.getProfile(bearerToken)
                    _profileState.value = profile
                    AuthState.setAuthenticated(true)
                    isInitialized = true
                } else {
                    AuthState.setAuthenticated(false)
                    _error.value = "Токен не найден"
                }
            } catch (e: Exception) {
                AuthState.setAuthenticated(false)
                _error.value = e.message ?: "Ошибка загрузки профиля"
            }
        }
    }

    fun refreshProfile() {  // Новый метод для принудительного обновления
        _profileState.value = null
        loadProfile()
    }

    fun clearProfile() {  // Метод для очистки данных при выходе
        _profileState.value = null
        isInitialized = false
    }

    fun updateProfile(newProfile: UserResponse) {
        viewModelScope.launch {
            try {
                val token = TokenManager.getToken(application)
                if (token != null) {
                    val bearerToken = "Bearer $token"
                    val updatedProfile = profileApi.updateProfile(bearerToken, newProfile)
                    _profileState.value = updatedProfile
                } else {
                    _error.value = "Токен не найден"
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "Ошибка обновления профиля"
            }
        }
    }
}




//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.example.algorithms.data.profile_data.User
//import com.example.algorithms.di.ApiService
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.StateFlow
//import kotlinx.coroutines.flow.asStateFlow
//import kotlinx.coroutines.launch
//
//class ProfileViewModel(private val apiService: ApiService) : ViewModel() {
//    private val _user = MutableStateFlow<User?>(null)
//    val user: StateFlow<User?> = _user.asStateFlow()
//
//    private val _isLoading = MutableStateFlow(false)
//    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
//
//    private val _error = MutableStateFlow<String?>(null)
//    val error: StateFlow<String?> = _error.asStateFlow()
//
//    fun loadProfile() {
//        viewModelScope.launch {
//            _isLoading.value = true
//            try {
//                //val token = getStoredToken() // Реализовать получение токена
//                val token = "123123123"
//                val userProfile = apiService.getProfile("Bearer $token")
//                _user.value = userProfile
//                _error.value = null
//            } catch (e: Exception) {
//                _error.value = e.message
//            } finally {
//                _isLoading.value = false
//            }
//        }
//    }
//}