package com.example.algorithms.viewmodels

import androidx.lifecycle.ViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.algorithms.data.SortStep

abstract class BaseSortStepViewModel : ViewModel() {
    abstract val steps: List<SortStep>

    var currentStepIndex by mutableIntStateOf(0)
        private set

    var isAuto by mutableStateOf(false)

    fun toggleAutoMode() {
        isAuto = !isAuto
    }

    fun goToStart() {
        currentStepIndex = 0
        isAuto = false
    }

    fun goToEnd() {
        currentStepIndex = steps.size - 1
    }

    fun goToPreviousStep() {
        if (currentStepIndex > 0) {
            currentStepIndex--
        }
    }

    fun goToNextStep() {
        if (currentStepIndex < steps.size - 1) {
            currentStepIndex++
        }
    }
}