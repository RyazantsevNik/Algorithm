package com.example.algorithms.viewmodels.simulation_graphs

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.NonCancellable.isActive
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


enum class NodeStatus {
    Default,
    Found,
    Visited,
    Current
}

data class TreeNodeUi(
    val id: Int,
    val value: String,
    val parentId: Int?,
    var status: NodeStatus
)


class BfsGraphSimulationViewModel : ViewModel() {

    private val _nodes = mutableStateListOf<TreeNodeUi>()
    val nodes: List<TreeNodeUi> get() = _nodes

    val _queue = ArrayDeque<TreeNodeUi>()

    private val _isPaused = mutableStateOf(false)
    val isPaused: State<Boolean> = _isPaused

    private var autoJob: Job? = null
    private val _isAutoRunning = mutableStateOf(false)
    val isAutoRunning: State<Boolean> = _isAutoRunning

    private val _isTraversalFinished = mutableStateOf(false)
    val isTraversalFinished: State<Boolean> = _isTraversalFinished

    private val _totalNodes = mutableStateOf(10)
    val totalNodes: State<Int> get() = _totalNodes

    init {
        generateNewTree()
    }

    data class TreeNode(
        val id: Int,
        val value: String,
        val parentId: Int? = null
    )

    private fun calculateMaxLevels(nodeCount: Int): Int {
        return when {
            nodeCount <= 5 -> (2..3).random()
            nodeCount <= 10 -> (3..4).random()
            nodeCount <= 20 -> (4..6).random()
            nodeCount <= 30 -> (5..7).random()
            else -> (5..8).random()
        }
    }

    fun generateTreeGraph(totalNodes: Int): List<TreeNode> {
        if (totalNodes <= 0) return emptyList()

        val nodes = mutableListOf<TreeNode>()
        var currentId = 1
        val maxLevels = calculateMaxLevels(totalNodes)

        nodes.add(TreeNode(id = currentId, value = currentId.toString()))
        currentId++

        val levels = mutableListOf<List<TreeNode>>()
        levels.add(listOf(nodes.first()))

        var remainingNodes = totalNodes - 1

        for (level in 1 until maxLevels) {
            val parents = levels.getOrNull(level - 1) ?: break
            val currentLevelNodes = mutableListOf<TreeNode>()

            for (parent in parents) {
                if (remainingNodes <= 0) break

                val childrenCount = (1..2).random()

                repeat(childrenCount) {
                    if (remainingNodes <= 0) return@repeat

                    val newNode = TreeNode(
                        id = currentId,
                        value = currentId.toString(),
                        parentId = parent.id
                    )
                    nodes.add(newNode)
                    currentLevelNodes.add(newNode)
                    currentId++
                    remainingNodes--
                }
            }

            if (currentLevelNodes.isEmpty()) break
            levels.add(currentLevelNodes)
        }


        while (remainingNodes > 0) {
            val parent = nodes.random()
            val newNode = TreeNode(
                id = currentId,
                value = currentId.toString(),
                parentId = parent.id
            )
            nodes.add(newNode)
            currentId++
            remainingNodes--
        }

        return nodes
    }

    fun generateNewTree() {
        stopTraversal()
        _nodes.clear()

        val tree = generateTreeGraph(_totalNodes.value)
        _nodes.addAll(tree.map { node ->
            TreeNodeUi(
                id = node.id,
                value = node.value,
                parentId = node.parentId,
                status = NodeStatus.Default
            )
        })

        _queue.clear()
        _nodes.firstOrNull()?.let {
            _queue.add(it)
            setCurrent(it.id)
        }
        _isTraversalFinished.value = false
    }

    fun startTraversal() {
        if (_isAutoRunning.value) return
        _isAutoRunning.value = true
        _isTraversalFinished.value = false
        autoJob = viewModelScope.launch {
            runTraversal()
        }
    }

    private suspend fun runTraversal() {
        while (isActive && _queue.isNotEmpty()) {

            while (_isPaused.value) {
                delay(100)
            }

            nextStep()
            delay(800L)
        }
        _isAutoRunning.value = false
        _isTraversalFinished.value = true
    }

    fun repeatTraversal() {

        _nodes.forEachIndexed { index, node ->
            _nodes[index] = node.copy(status = NodeStatus.Default)
        }
        _queue.clear()
        _nodes.firstOrNull()?.let {
            _queue.add(it)
            setCurrent(it.id)
        }
        _isTraversalFinished.value = false
        startTraversal()
    }

    fun togglePause() {
        _isPaused.value = !_isPaused.value
    }

    fun stopTraversal() {
        autoJob?.cancel()
        _isAutoRunning.value = false
        _isPaused.value = false
    }

    fun resetTraversal() {
        generateNewTree()
    }

    fun nextStep() {
        if (_queue.isEmpty()) {
            stopTraversal()
            return
        }

        val current = _queue.removeFirst()
        setVisited(current.id)

        val children = _nodes.filter { it.parentId == current.id && it.status == NodeStatus.Default }
        children.forEach { child ->
            _queue.add(child)
            setFound(child.id)
        }

        if (_queue.isNotEmpty()) {
            setCurrent(_queue.first().id)
        }
    }

    private fun setCurrent(id: Int) {
        val currentIndex = _nodes.indexOfFirst { it.status == NodeStatus.Current }
        if (currentIndex != -1) {
            _nodes[currentIndex] = _nodes[currentIndex].copy(status = NodeStatus.Visited)
        }

        val newIndex = _nodes.indexOfFirst { it.id == id }
        if (newIndex != -1) {
            _nodes[newIndex] = _nodes[newIndex].copy(status = NodeStatus.Current)
        }
    }

    private fun setVisited(id: Int) {
        val index = _nodes.indexOfFirst { it.id == id }
        if (index != -1) {
            _nodes[index] = _nodes[index].copy(status = NodeStatus.Visited)
        }
    }

    private fun setFound(id: Int) {
        val index = _nodes.indexOfFirst { it.id == id }
        if (index != -1) {
            _nodes[index] = _nodes[index].copy(status = NodeStatus.Found)
        }
    }

    fun increaseNodeCount() {
        if (_totalNodes.value < 30) {
            _totalNodes.value++
            generateNewTree()
        }
    }

    fun decreaseNodeCount() {
        if (_totalNodes.value > 3) {
            _totalNodes.value--
            generateNewTree()
        }
    }
}