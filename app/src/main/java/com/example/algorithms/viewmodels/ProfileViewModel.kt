package com.example.algorithms.viewmodels

import android.app.Application
import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.algorithms.di.chat_api.ProfileApi
import com.example.algorithms.di.chat_api.UserResponse
import com.example.algorithms.utils.AuthState
import com.example.algorithms.utils.TokenManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

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

    fun refreshProfile() {
        _profileState.value = null
        loadProfile()
    }

    fun clearProfile() {
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

    fun updateProfilePicture(uri: Uri) {
        viewModelScope.launch {
            try {
                val token = TokenManager.getToken(application)
                if (token != null) {
                    val bearerToken = "Bearer $token"
                    
                    val file = getFileFromUri(uri, application)
                    
                    val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
                    val photoPart = MultipartBody.Part.createFormData(
                        "file", 
                        file.name, 
                        requestFile
                    )
                    
                    val response = profileApi.uploadProfilePhoto(bearerToken, photoPart)
                    
                    loadProfile()
                    
                } else {
                    _error.value = "Токен не найден"
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "Ошибка обновления фото профиля"
            }
        }
    }

    private fun getFileFromUri(uri: Uri, context: Context): File {
        val inputStream = context.contentResolver.openInputStream(uri)
        val file = File(context.cacheDir, "profile_photo_${System.currentTimeMillis()}.jpg")
        
        inputStream?.use { input ->
            file.outputStream().use { output ->
                input.copyTo(output)
            }
        }
        
        return file
    }
}