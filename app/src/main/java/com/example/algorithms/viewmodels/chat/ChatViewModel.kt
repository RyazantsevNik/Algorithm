package com.example.algorithms.viewmodels.chat

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.algorithms.di.ai_chat.ChatApi
import com.example.algorithms.di.ai_chat.ChatRequest
import com.example.algorithms.di.ai_chat.Message
import com.example.algorithms.viewmodels.profile.ProfileViewModel
import kotlinx.coroutines.launch
import java.net.ConnectException
import java.net.SocketTimeoutException

class ChatViewModel(
    private val chatApi: ChatApi,
    private val profileViewModel: ProfileViewModel,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    val isProfileReady = mutableStateOf(false)

    private val _chatMessages = mutableStateListOf<Message>()
    val chatMessages: List<Message> get() = _chatMessages

    private val _loadingState = mutableStateOf(false)
    val loadingState: State<Boolean> get() = _loadingState

    private val _errorState = mutableStateOf<String?>(null)
    val errorState: State<String?> get() = _errorState

    val isLoading = mutableStateOf(true)
    val isAuthenticated = mutableStateOf(false)
    val userName = mutableStateOf("")

    init {
        viewModelScope.launch {
            profileViewModel.profileState.collect { profile ->
                isAuthenticated.value = profile != null
                isLoading.value = false
                userName.value = profile?.username ?: ""
            }
        }
    }

    private fun handleErrorResponse(code: Int) {
        when (code) {
            401 -> {
                _errorState.value = "Сессия истекла"
                profileViewModel.clearProfile()
            }
            else -> _errorState.value = "Ошибка сервера: $code"
        }
    }
        private fun handleNetworkError(e: Exception) {
        _errorState.value = when (e) {
            is SocketTimeoutException -> "Таймаут соединения"
            is ConnectException -> "Нет подключения к интернету"
            else -> "Ошибка сети: ${e.localizedMessage}"
        }
    }

    fun loadChatHistory() {
        viewModelScope.launch {
            try {
                val token = profileViewModel.getAuthToken()
                val response = chatApi.getChatHistory("Bearer $token")
                Log.d("chat","выполнил запрос вот ответ: $response")

                if (response.isSuccessful) {
                    response.body()?.let { messageList ->
                        _chatMessages.clear()
                        _chatMessages.addAll(messageList.map { msg -> Message(msg.role, msg.content) })
                    }
                } else {
                    handleErrorResponse(response.code())
                }
            } catch (e: Exception) {
                handleNetworkError(e)
            }
        }
    }

    fun sendMessage(message: String) {
        if (!isAuthenticated.value) {
            _errorState.value = "Требуется авторизация"
            return
        }

        viewModelScope.launch {
            _loadingState.value = true
            _errorState.value = null

            _chatMessages.add(Message("user", message))

            try {
                val token = profileViewModel.getAuthToken()
                val request = ChatRequest(message, _chatMessages.map { Message(it.role, it.content) })
                val response = chatApi.sendMessage("Bearer $token", request)

                if (response.isSuccessful) {
                    response.body()?.let {
                        _chatMessages.add(Message("assistant", it.response))
                    }
                } else {
                    handleErrorResponse(response.code())
                }
            } catch (e: Exception) {
                handleNetworkError(e)
            } finally {
                _loadingState.value = false
            }
        }
    }

    fun clearChatHistory() {
        if (!isAuthenticated.value) {
            _errorState.value = "Требуется авторизация"
            return
        }

        viewModelScope.launch {
            _loadingState.value = true
            _errorState.value = null

            try {
                val token = profileViewModel.getAuthToken()
                val response = chatApi.clearChatHistory("Bearer $token")

                if (response.isSuccessful) {
                    _chatMessages.clear()
                } else {
                    handleErrorResponse(response.code())
                }
            } catch (e: Exception) {
                handleNetworkError(e)
            } finally {
                _loadingState.value = false
            }
        }
    }
}
