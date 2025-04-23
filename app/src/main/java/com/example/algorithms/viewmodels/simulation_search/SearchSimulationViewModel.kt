package com.example.algorithms.viewmodels.simulation_search

import kotlinx.coroutines.flow.StateFlow

interface SearchSimulationViewModel {
    val state: StateFlow<SimulationState>
    fun setInputText(value: String)
    fun setTarget(value: String)
    fun applyInput()
    fun reset()
    fun togglePause()
    fun startSearch()
}
