package com.example.algorithms.viewmodels.step_math

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.algorithms.viewmodels.profile.ProgressViewModel
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

data class StepPair(
    val explanation: String,
    val visual: FibonacciVisualState
)

data class FibonacciVisualState(
    val tree: List<FibonacciNode>,
    val highlightResult: Boolean = false
)

data class FibonacciNode(
    val id: String,
    val value: Int?, // может быть null, если ещё не посчитано
    val calculated: Boolean = false, // флаг, что значение было посчитано и должно оставаться
    val children: List<String>,
    val highlighted: Boolean = false,
    val cached: Boolean = false
)

class FibonacciStepViewModel : ViewModel(), KoinComponent {
    private val progressViewModel: ProgressViewModel by inject()
    private var isCompleted = false
    private var userToken: String? = null

    fun setToken(token: String) {
        userToken = token
    }

    private val _steps = mutableStateListOf<StepPair>()
    val steps: List<StepPair> get() = _steps

    private val _currentStepIndex = mutableStateOf(0)
    val currentStepIndex: State<Int> get() = _currentStepIndex



    private val fullFib5Tree = listOf(
        FibonacciNode("fib5", null, calculated = false, listOf("fib4", "fib3")),
        FibonacciNode("fib4", null, calculated = false, listOf("fib3", "fib2")),
        FibonacciNode("fib3", null, calculated = false, listOf("fib2", "fib1")),
        FibonacciNode("fib2", null, calculated = false, listOf("fib1", "fib0")),
        FibonacciNode("fib1", null, calculated = false, emptyList()),
        FibonacciNode("fib0", null, calculated = false, emptyList())
    )

    private var fullTree = fullFib5Tree

    init {
        generateSteps()
    }

    fun nextStep() {
        if (_currentStepIndex.value < _steps.lastIndex) {
            _currentStepIndex.value++
            
            // Проверяем, достигли ли мы последнего шага
            if (_currentStepIndex.value == _steps.lastIndex && !isCompleted) {
                isCompleted = true
                userToken?.let { token ->
                    Log.d("Fibonacci", "Saving progress for fibonacci")
                    progressViewModel.updateProgress(
                        token = token,
                        algorithm = "fibonacci",
                        completed = true
                    )
                }
            }
        }
    }

    fun previousStep() {
        if (_currentStepIndex.value > 0) {
            _currentStepIndex.value--
        }
    }



    private fun generateSteps() {
        val steps = mutableListOf<StepPair>()

        // Шаг 0
        steps += StepPair(
            "Числа Фибоначчи — элементы числовой последовательности, в которой первые два числа равны 0 и 1, а каждое последующее число равно сумме двух предыдущих чисел.",
            FibonacciVisualState(tree = fullTree)
        )

        // Шаг 1: базовые случаи
        fullTree = updateTree(fullTree, mapOf(
            "fib0" to Triple(0, false, false),
            "fib1" to Triple(1, false, false)
        ))
        steps += StepPair(
            "Обозначение: fib(n) — n-е число Фибоначчи.\nfib(0) = 0, fib(1) = 1",
            FibonacciVisualState(tree = fullTree)
        )

        // Шаг 2: пример
        steps += StepPair(
            "Пример: fib(5) = fib(4) + fib(3), а те, в свою очередь, тоже разбиваются далее...",
            FibonacciVisualState(tree = fullTree)
        )

        // Шаг 3: начинаем вычисление fib(4)
        fullTree = highlightNodes(fullTree, "fib5", "fib4")
        steps += StepPair(
            "Рассмотрим рекурсивный вызов fib(5). Сначала считаем fib(4).",
            FibonacciVisualState(tree = fullTree)
        )

        // Шаг 4: fib(4) → fib(3)
        fullTree = highlightNodes(fullTree, "fib4", "fib3")
        steps += StepPair(
            "fib(4) = fib(3) + fib(2). Сначала считаем fib(3)...",
            FibonacciVisualState(tree = fullTree)
        )

        // Шаг 5: fib(3) → fib(2)
        fullTree = highlightNodes(fullTree, "fib3", "fib2")
        steps += StepPair(
            "fib(3) = fib(2) + fib(1). Продолжаем...",
            FibonacciVisualState(tree = fullTree)
        )

        // Шаг 6: fib(2) = fib(1) + fib(0)
        fullTree = highlightNodes(fullTree, "fib2", "fib1", "fib0")
        steps += StepPair(
            "fib(2) = fib(1) + fib(0) = 1 + 0 = 1",
            FibonacciVisualState(tree = updateTree(fullTree, mapOf(
                "fib2" to Triple(1, true, false)
            )))
        )

        // Шаг 7: fib(3) = 1 + 1 = 2
        fullTree = updateTree(fullTree, mapOf(
            "fib2" to Triple(1, false, false),
            "fib1" to Triple(1, false, false),
            "fib0" to Triple(0, false, false),
            "fib3" to Triple(2, true, false)
        ))
        steps += StepPair(
            "fib(3) = fib(2) + fib(1) = 1 + 1 = 2",
            FibonacciVisualState(tree = fullTree)
        )

        // Шаг 8: fib(4) = 2 + 1 = 3
        fullTree = updateTree(fullTree, mapOf("fib4" to Triple(3, true, false)))
        steps += StepPair(
            "fib(4) = fib(3) + fib(2) = 2 + 1 = 3",
            FibonacciVisualState(tree = fullTree)
        )

        // Шаг 9: fib(3) взят из кэша
        fullTree = updateTree(fullTree, mapOf("fib3" to Triple(2, false, true)))
        steps += StepPair(
            "Теперь считаем вторую ветку от fib(5): fib(3), который уже известен = 2",
            FibonacciVisualState(tree = fullTree)
        )

        // Шаг 10: fib(5) = 3 + 2 = 5
        fullTree = updateTree(fullTree, mapOf(
            "fib5" to Triple(5, true, false),
            "fib3" to Triple(2, true, false))
        )
        steps += StepPair(
            "fib(5) = fib(4) + fib(3) = 3 + 2 = 5",
            FibonacciVisualState(tree = fullTree)
        )

        // Шаг 11: завершение — снимаем подсветку и кэш
        val clearedFinalTree = fullTree.map { node ->
            node.copy(highlighted = false, cached = false)
        }
        steps += StepPair(
            "Ответ: fib(5) = 5. Мы прошли весь путь от базовых случаев до финального результата.",
            FibonacciVisualState(tree = clearedFinalTree)
        )

        _steps.addAll(steps)
    }

    private fun highlightNodes(
        tree: List<FibonacciNode>,
        vararg ids: String
    ): List<FibonacciNode> {
        val set = ids.toSet()
        return tree.map { node ->
            node.copy(highlighted = set.contains(node.id))
        }
    }

    private fun updateTree(
        currentTree: List<FibonacciNode>,
        updates: Map<String, Triple<Int?, Boolean, Boolean>> // value, highlighted, cached
    ): List<FibonacciNode> {
        return currentTree.map { node ->
            val update = updates[node.id]
            if (update != null) {
                val newValue = update.first ?: node.value
                val isCalculatedNow = newValue != null
                node.copy(
                    value = newValue,
                    calculated = node.calculated || isCalculatedNow,
                    highlighted = update.second,
                    cached = update.third
                )
            } else {
                node
            }
        }
    }

    fun reset() {
        _currentStepIndex.value = 0
    }
}