package com.example.algorithms.di

import com.example.algorithms.di.ai_chat.ChatApi
import com.example.algorithms.di.chat_api.ApiClient
import com.example.algorithms.di.chat_api.AuthApi
import com.example.algorithms.di.chat_api.ProfileApi
import com.example.algorithms.di.chat_api.ProgressApi
import com.example.algorithms.viewmodels.*
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    // API Services
    single { ApiClient.retrofit.create(AuthApi::class.java) }
    single { ApiClient.retrofit.create(ProfileApi::class.java) }
    single { ApiClient.retrofit.create(ProgressApi::class.java) }
    single { ApiClient.retrofit.create(ChatApi::class.java) }  //chat

    // ViewModels
    viewModel { AuthViewModel(get(), get(), get()) }
    viewModel { MenuViewModel() }
    viewModel { BubbleSortViewModel() }
    viewModel { SelectionSortViewModel() }
    viewModel { BubbleSortStepViewModel() }
    viewModel { SelectionSortStepViewModel() }
    viewModel { InsertionSortStepViewModel() }
    viewModel { QuickSortStepViewModel() }
    viewModel { ProfileViewModel(get(), get()) }
    viewModel { ChatViewModel(get(), get(), get()) }              //chat
}
