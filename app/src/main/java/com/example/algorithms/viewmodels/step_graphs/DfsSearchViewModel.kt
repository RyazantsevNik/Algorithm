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


data class GraphNode(
    val id: String,
    val neighbors: List<String>
)

data class StateSnapshot(
    val visited: List<String>,
    val current: String?,
    val stack: List<String>,
    val explanation: String,
    val remainingCandidates: List<String>
)


class DfsSearchViewModel : ViewModel(), KoinComponent {
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
    val explanation: State<String> = _explanation

    private val stack = mutableListOf<String>()

    private val history = mutableListOf<StateSnapshot>()
    private val steps = mutableListOf<StateSnapshot>()
    private var _stepIndex = mutableIntStateOf(0)
    val stepIndex: State<Int> = _stepIndex

    private var autoJob: Job? = null
    private var _isAutoMode = mutableStateOf(false)
    var isAutoMode: State<Boolean> = _isAutoMode


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


                    if (stack.isEmpty()) {
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
        stack.clear()
        _remainingCandidates.clear()
        _current.value = null
        _explanation.value = "Начинаем обход графа в глубину с вершины 'a'. Точки достижимые из текущей становятся кандидатами. Кандидаты обрабатываются по методу последний вошёл первый вышел (стек)."
        history.clear()
        steps.clear()
        _stepIndex.intValue = 0

        stack.add("a")
        steps.add(
            StateSnapshot(
                visited = _visited.toList(),
                current = _current.value,
                stack = stack.toList(),
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

        if (stack.isEmpty()) {
            _current.value = null
            _candidates.clear()
            _explanation.value = "Обход завершён."
            
            if (!isCompleted) {
                isCompleted = true
                userToken?.let { token ->
                    Log.d("DfsSearch", "Saving progress for dfs_search")
                    progressViewModel.updateProgress(
                        token = token,
                        algorithm = "dfs_search",
                        completed = true
                    )
                }
            }
            return
        }

        val nodeId = stack.removeAt(stack.size - 1)

        if (_visited.contains(nodeId)) {
            nextStep()
            return
        }


        history.add(
            StateSnapshot(
                visited = _visited.toList(),
                current = _current.value,
                stack = stack.toList(),
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

        stack.addAll(unvisitedNeighbors.reversed())
        _remainingCandidates.addAll(unvisitedNeighbors)
        _remainingCandidates.remove(nodeId)

        _explanation.value = buildExplanation(nodeId, unvisitedNeighbors)


        steps.add(
            StateSnapshot(
                visited = _visited.toList(),
                current = _current.value,
                stack = stack.toList(),
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

    private fun restoreSnapshot(snapshot: StateSnapshot) {
        _visited.clear()
        _visited.addAll(snapshot.visited)
        _current.value = snapshot.current
        stack.clear()
        stack.addAll(snapshot.stack)
        _explanation.value = snapshot.explanation
        _remainingCandidates.clear()
        _remainingCandidates.addAll(snapshot.remainingCandidates)
    }

    private fun buildExplanation(current: String, neighbors: List<String>): String {
        val sb = StringBuilder()
        sb.append("Текущая вершина: '$current'.\n")
        if (neighbors.isEmpty()) {
            sb.append("Нет непосещённых соседей.\nВозвращаемся назад к последнему кандидату.")
        } else {
            sb.append("Добавляем в стек кандидатов: ${neighbors.joinToString(", ")}.\nПереходим к точке ${neighbors.first()}.")
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
}

