package com.example.algorithms.di

import com.example.algorithms.di.ai_chat.ChatApi
import com.example.algorithms.di.main_api.ApiClient
import com.example.algorithms.di.main_api.AuthApi
import com.example.algorithms.di.main_api.ProfileApi
import com.example.algorithms.di.main_api.ProgressApi
import com.example.algorithms.di.main_api.ProgressRepository
import com.example.algorithms.viewmodels.chat.ChatViewModel
import com.example.algorithms.viewmodels.menu.FavoritesViewModel
import com.example.algorithms.viewmodels.menu.MenuViewModel
import com.example.algorithms.viewmodels.profile.AuthViewModel
import com.example.algorithms.viewmodels.profile.ProfileViewModel
import com.example.algorithms.viewmodels.profile.ProgressViewModel
import com.example.algorithms.viewmodels.simulation_graphs.BfsGraphSimulationViewModel
import com.example.algorithms.viewmodels.simulation_graphs.DfsGraphSimulationViewModel
import com.example.algorithms.viewmodels.simulation_search.LinearSearchSimulationViewModel
import com.example.algorithms.viewmodels.simulation_sorting.BubbleSortViewModel
import com.example.algorithms.viewmodels.simulation_sorting.InsertionSortViewModel
import com.example.algorithms.viewmodels.simulation_sorting.SelectionSortViewModel
import com.example.algorithms.viewmodels.step_graphs.BfsSearchViewModel
import com.example.algorithms.viewmodels.step_graphs.DfsSearchViewModel
import com.example.algorithms.viewmodels.step_math.FactorialStepViewModel
import com.example.algorithms.viewmodels.step_math.FibonacciStepViewModel
import com.example.algorithms.viewmodels.step_search.BinarySearchViewModel
import com.example.algorithms.viewmodels.step_search.LinearSearchViewModel
import com.example.algorithms.viewmodels.step_sorting.BubbleSortStepViewModel
import com.example.algorithms.viewmodels.step_sorting.InsertionSortStepViewModel
import com.example.algorithms.viewmodels.step_sorting.QuickSortStepViewModel
import com.example.algorithms.viewmodels.step_sorting.SelectionSortStepViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    // API Services
    single { ApiClient.retrofit.create(AuthApi::class.java) }
    single { ApiClient.retrofit.create(ProfileApi::class.java) }
    single { ApiClient.retrofit.create(ProgressApi::class.java) }
    single { ApiClient.retrofit.create(ChatApi::class.java) }  //chat
    single { ProgressRepository(get()) }



    // ViewModels
    viewModel { AuthViewModel(get(), get(), get()) }
    viewModel { MenuViewModel() }
    viewModel { BubbleSortViewModel() }
    viewModel { InsertionSortViewModel() }
    viewModel { SelectionSortViewModel() }
    viewModel { BubbleSortStepViewModel() }
    viewModel { SelectionSortStepViewModel() }
    viewModel { InsertionSortStepViewModel() }
    viewModel { FavoritesViewModel(get()) }
    viewModel { QuickSortStepViewModel() }
    viewModel { LinearSearchViewModel() }
    viewModel { BinarySearchViewModel() }
    viewModel { LinearSearchSimulationViewModel() }
    viewModel { DfsSearchViewModel() }
    viewModel { BfsSearchViewModel() }
    viewModel { BfsGraphSimulationViewModel() }
    viewModel { DfsGraphSimulationViewModel() }
    viewModel { FactorialStepViewModel() }
    viewModel { FibonacciStepViewModel() }
    viewModel { ProgressViewModel(get()) }
    viewModel { ProfileViewModel(get(), get()) }
    viewModel { ChatViewModel(get(), get(), get()) }              //chat
}
