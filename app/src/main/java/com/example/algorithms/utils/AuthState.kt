package com.example.algorithms.utils

import android.content.Context
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object AuthState {
    private val _isAuthenticated = MutableStateFlow(false)
    val isAuthenticated: StateFlow<Boolean> = _isAuthenticated

    fun checkAuthState(context: Context) {
        val token = TokenManager.getToken(context)
        _isAuthenticated.value = !token.isNullOrEmpty()
    }

    fun setAuthenticated(value: Boolean) {
        _isAuthenticated.value = value
    }
} 