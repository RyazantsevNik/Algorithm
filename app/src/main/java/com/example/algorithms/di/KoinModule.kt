package com.example.algorithms.di

import com.example.algorithms.viewmodels.BubbleSortStepViewModel
import com.example.algorithms.viewmodels.BubbleSortViewModel
import com.example.algorithms.viewmodels.MenuViewModel
import com.example.algorithms.viewmodels.SelectionSortStepViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    // ViewModel
    viewModel { MenuViewModel() }
    viewModel { BubbleSortViewModel() }
    viewModel { BubbleSortStepViewModel() }
    viewModel { SelectionSortStepViewModel() }
}