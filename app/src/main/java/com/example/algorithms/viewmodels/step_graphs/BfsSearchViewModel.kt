package com.example.algorithms.viewmodels.step_graphs

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.algorithms.viewmodels.profile.ProgressViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject


data class StateSnapshots(
    val visited: List<String>,
    val current: String?,
    val queue: List<String>,
    val explanation: String,
    val remainingCandidates: List<String>
)

class BfsSearchViewModel : ViewModel(), KoinComponent {
    private val progressViewModel: ProgressViewModel by inject()
    private var isCompleted = false
    private var userToken: String? = null

    fun setToken(token: String) {
        userToken = token
    }

    private val graph = listOf(
        GraphNode("a", listOf("b", "c", "d")),
        GraphNode("b", listOf("e", "f")),
        GraphNode("c", listOf("h")),
        GraphNode("d", listOf("i", "j")),
        GraphNode("e", listOf("k")),
        GraphNode("f", emptyList()),
        GraphNode("h", listOf("g")),
        GraphNode("i", emptyList()),
        GraphNode("j", listOf("l")),
        GraphNode("k", emptyList()),
        GraphNode("g", emptyList()),
        GraphNode("l", emptyList())
    )

    private val _remainingCandidates = mutableStateListOf<String>()
    val remainingCandidates: List<String> get() = _remainingCandidates

    private val _visited = mutableStateListOf<String>()
    val visited: List<String> get() = _visited

    private val _current = mutableStateOf<String?>(null)
    val current: State<String?> = _current

    private val _candidates = mutableStateListOf<String>()
    val candidates: List<String> get() = _candidates

    private val _explanation = mutableStateOf("")
    val explanation: State<String> get() = _explanation

    private val queue = mutableListOf<String>()

    private val history = mutableListOf<StateSnapshots>()
    private val steps = mutableListOf<StateSnapshots>()
    private var _stepIndex = mutableIntStateOf(0)
    val stepIndex: State<Int> = _stepIndex

    private var autoJob: Job? = null
    private val _isAutoMode = mutableStateOf(false)
    val isAutoMode: State<Boolean> = _isAutoMode

    init {
        reset()
    }

    fun toggleAutoMode() {
        if (_isAutoMode.value) {
            autoJob?.cancel()
            autoJob = null
            _isAutoMode.value = false
        } else {
            _isAutoMode.value = true
            autoJob = viewModelScope.launch {
                while (isActive) {
                    delay(1000L)
                    nextStep()
                    if (queue.isEmpty()) {
                        toggleAutoMode()
                        break
                    }
                }
            }
        }
    }

    fun reset() {
        stopAuto()
        _visited.clear()
        _candidates.clear()
        queue.clear()
        _remainingCandidates.clear()
        _current.value = null
        _explanation.value = "Начинаем обход графа в ширину с вершины 'a'. Точки достижимые из текущей становятся кандидатами. Кандидаты добавляются в очередь и обрабатываются по порядку."
        history.clear()
        steps.clear()
        _stepIndex.intValue = 0

        queue.add("a")
        steps.add(
            StateSnapshots(
                visited = _visited.toList(),
                current = _current.value,
                queue = queue.toList(),
                explanation = _explanation.value,
                remainingCandidates = _remainingCandidates.toList()
            )
        )
    }

    fun nextStep() {
        if (_stepIndex.intValue < steps.lastIndex) {
            _stepIndex.intValue++
            restoreSnapshot(steps[_stepIndex.intValue])
            return
        }

        if (queue.isEmpty()) {
            _current.value = null
            _candidates.clear()
            _explanation.value = "Обход завершён."
            
            if (!isCompleted) {
                isCompleted = true
                userToken?.let { token ->
                    Log.d("BfsSearch", "Saving progress for bfs_search")
                    progressViewModel.updateProgress(
                        token = token,
                        algorithm = "bfs_search",
                        completed = true
                    )
                }
            }
            return
        }

        val nodeId = queue.removeAt(0)

        if (_visited.contains(nodeId)) {
            nextStep()
            return
        }

        history.add(
            StateSnapshots(
                visited = _visited.toList(),
                current = _current.value,
                queue = queue.toList(),
                explanation = _explanation.value,
                remainingCandidates = _remainingCandidates.toList()
            )
        )

        _current.value = nodeId
        _visited.add(nodeId)

        val node = graph.find { it.id == nodeId }
        val unvisitedNeighbors = node?.neighbors?.filter { !_visited.contains(it) } ?: emptyList()
        _candidates.clear()
        _candidates.addAll(unvisitedNeighbors)

        queue.addAll(unvisitedNeighbors)
        _remainingCandidates.addAll(unvisitedNeighbors)
        _remainingCandidates.remove(nodeId)

        _explanation.value = buildExplanation(nodeId, unvisitedNeighbors)

        steps.add(
            StateSnapshots(
                visited = _visited.toList(),
                current = _current.value,
                queue = queue.toList(),
                explanation = _explanation.value,
                remainingCandidates = _remainingCandidates.toList()
            )
        )
        _stepIndex.intValue++
    }

    fun previousStep() {
        if (_stepIndex.intValue > 0) {
            _stepIndex.intValue--
            val snapshot = steps[_stepIndex.intValue]
            restoreSnapshot(snapshot)
        }
    }

    private fun restoreSnapshot(snapshot: StateSnapshots) {
        _visited.clear()
        _visited.addAll(snapshot.visited)
        _current.value = snapshot.current
        queue.clear()
        queue.addAll(snapshot.queue)
        _explanation.value = snapshot.explanation
        _remainingCandidates.clear()
        _remainingCandidates.addAll(snapshot.remainingCandidates)
    }

    private fun buildExplanation(current: String, neighbors: List<String>): String {
        val sb = StringBuilder()
        sb.append("Текущая вершина: '$current'.\n")
        if (neighbors.isEmpty()) {
            sb.append("Нет непосещённых соседей.\nПереходим к следующему элементу очереди.")
        } else {
            sb.append("Добавляем в очередь кандидатов: ${neighbors.joinToString(", ")}.\nПереходим к следующему элементу очереди.")
        }
        return sb.toString()
    }

    fun getCurrentStep(): Int {
        return _stepIndex.intValue
    }

    private fun stopAuto() {
        autoJob?.cancel()
        _isAutoMode.value = false
    }

    fun goToNextStep() {
        if (_stepIndex.value >= steps.size - 1) return

        _stepIndex.value++
        val currentState = steps[_stepIndex.value]
        _visited.clear()
        _visited.addAll(currentState.visited)
        _current.value = currentState.current
        queue.clear()
        queue.addAll(currentState.queue)
        _remainingCandidates.clear()
        _remainingCandidates.addAll(currentState.remainingCandidates)
        _explanation.value = currentState.explanation

        if (_stepIndex.value == steps.size - 1 && !isCompleted) {
            isCompleted = true
            userToken?.let { token ->
                Log.d("BfsSearch", "Saving progress for bfs_search")
                progressViewModel.updateProgress(
                    token = token,
                    algorithm = "bfs_search",
                    completed = true
                )
            }
        }
    }
}
