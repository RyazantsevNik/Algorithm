package com.example.algorithms.viewmodels.step_math

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.algorithms.viewmodels.profile.ProgressViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject


sealed class FactorialStep {
    data class Explanation(val text: String) : FactorialStep()
    data class Visual(
        val id: Int,
        val value: Int,
        val level: Int,
        val parentId: Int? = null,
        val result: Int? = null
    ) : FactorialStep()
}

class FactorialStepViewModel : ViewModel(), KoinComponent {
    private val progressViewModel: ProgressViewModel by inject()
    private var isCompleted = false
    private var userToken: String? = null

    fun setToken(token: String) {
        Log.d("Factorial", "Setting token: ${token.take(10)}...")
        userToken = token
    }

    private data class StepPair(
        val explanation: FactorialStep.Explanation,
        val visual: FactorialStep.Visual?
    )

    private val stepPairs = mutableListOf<StepPair>()
    private val _visibleSteps = mutableStateListOf<FactorialStep.Visual>()
    val visibleSteps: List<FactorialStep.Visual> get() = _visibleSteps

    private var _currentExplanation = mutableStateOf<String>("")
    val currentExplanation: State<String> get() = _currentExplanation

    private var currentIndex = 0
    private var idCounter = 0

    private val _isAutoMode = mutableStateOf(false)
    val isAutoMode: State<Boolean> get() = _isAutoMode

    private var autoJob: Job? = null

    fun reset() {
        autoJob?.cancel()
        _isAutoMode.value = false
        _visibleSteps.clear()
        currentIndex = 0
        if (stepPairs.isNotEmpty()) {
            _currentExplanation.value = stepPairs[0].explanation.text
            stepPairs[0].visual?.let { _visibleSteps.add(it) }
        }
    }

    fun nextStep() {
        if (currentIndex + 1 < stepPairs.size) {
            currentIndex++
            val step = stepPairs[currentIndex]
            _currentExplanation.value = step.explanation.text
            step.visual?.let { _visibleSteps.add(it) }
            
            if (currentIndex == stepPairs.lastIndex && !isCompleted) {
                isCompleted = true
                Log.d("Factorial", "Token value: $userToken")
                userToken?.let { token ->
                    Log.d("Factorial", "Saving progress for factorial with token: ${token.take(10)}...")
                    progressViewModel.updateProgress(
                        token = token,
                        algorithm = "factorial",
                        completed = true
                    )
                    Log.d("Factorial", "Progress update called for factorial")
                } ?: run {
                    Log.e("Factorial", "Token is null, cannot save progress")
                }
            }
        } else {
            stopAutoMode()
        }
    }

    fun prevStep() {
        if (currentIndex > 0) {
            if (stepPairs[currentIndex].visual != null) {
                _visibleSteps.removeAt(_visibleSteps.lastIndex)
            }
            currentIndex--
            _currentExplanation.value = stepPairs[currentIndex].explanation.text
        }
    }

    fun toggleAutoMode() {
        _isAutoMode.value = !_isAutoMode.value
        if (_isAutoMode.value) startAutoMode() else stopAutoMode()
    }

    private fun startAutoMode() {
        autoJob = viewModelScope.launch {
            while (_isAutoMode.value && currentIndex + 1 < stepPairs.size) {
                delay(1200)
                nextStep()
            }
            _isAutoMode.value = false
        }
    }

    private fun stopAutoMode() {
        autoJob?.cancel()
        _isAutoMode.value = false
    }

    fun generateFactorialSteps(n: Int) {
        stepPairs.clear()
        _visibleSteps.clear()
        currentIndex = 0
        idCounter = 0
        _isAutoMode.value = false

        val intro = listOf(
            """
    Факториал числа n (обозначается как n!) — это фундаментальная математическая операция, 
    представляющая собой произведение всех натуральных чисел от 1 до n включительно. 
    Эта операция чрезвычайно важна в комбинаторике, теории вероятностей, 
    математическом анализе и компьютерных алгоритмах. 
    Например, факториал показывает количество перестановок множества из n элементов. 
    При n > 0 вычисляется как n! = 1 × 2 × 3 × ... × n, 
    а для n = 0 принимается специальное значение.
    """.trimIndent(),

            """
    Рассмотрим конкретный пример вычисления факториала для числа 5:
    5! = 5 × 4 × 3 × 2 × 1 = 120. 
    Это означает, что существует 120 различных способов упорядочить 
    набор из пяти различных предметов. Факториалы растут чрезвычайно быстро: 
    уже 10! = 3 628 800, а 20! превышает 2 квинтиллиона. 
    В программировании вычисление факториала часто используется 
    для демонстрации рекурсивных алгоритмов.
    """.trimIndent(),

            """
    Особое внимание заслуживает базовый случай факториала — 0! = 1. 
    Это определение может показаться неочевидным, но оно критически важно 
    для корректной работы рекурсивных алгоритмов. Такое значение принято потому, что:
    1) Существует ровно одна перестановка пустого множества
    2) Это согласуется с пустым произведением в математике
    3) Обеспечивает работу рекуррентной формулы при n = 1
    Без этого определения рекурсивное вычисление факториала было бы невозможно.
    """.trimIndent(),

            """
    Рекурсивная формула факториала n! = n × (n-1)! демонстрирует 
    принцип решения задачи через её уменьшенную версию. Эта формула:
    1) Определяет факториал через факториал меньшего числа
    2) Позволяет разбить сложную задачу на более простые подзадачи
    3) Требует явного указания базового случая (0! = 1)
    4) Является классическим примером "разделяй и властвуй"
    В программировании это соответствует рекурсивной функции, 
    которая вызывает саму себя с меньшим аргументом.
    """.trimIndent(),

            """
    Теперь приступим к пошаговому вычислению $n! с использованием рекурсивного подхода. 
    Мы будем:
    1) Последовательно углубляться в рекурсию, вычисляя факториалы меньших чисел
    2) Достигнем базового случая 0! = 1
    3) Затем поднимемся обратно, используя полученные значения
    4) На каждом шаге будем видеть текущее состояние вычислений
    Этот процесс наглядно демонстрирует механизм стека вызовов в рекурсии.
    """.trimIndent()
        )

        stepPairs.addAll(intro.map { StepPair(FactorialStep.Explanation(it), null) })

        generateStepsRecursive(n, 0, null)

        reset()
    }

    private fun generateStepsRecursive(n: Int, level: Int, parentId: Int?): Int {
        val currentId = idCounter++

        if (n == 0) {
            stepPairs.add(
                StepPair(
                    FactorialStep.Explanation("Базовый случай: 0! = 1 — возвращаем 1."),
                    FactorialStep.Visual(currentId, 0, level, parentId, 1)
                )
            )
            return 1
        }

        stepPairs.add(
            StepPair(
                FactorialStep.Explanation("Рассматриваем $n! — сначала решим подзадачу ${n - 1}!."),
                FactorialStep.Visual(currentId, n, level, parentId)
            )
        )

        stepPairs.add(
            StepPair(
                FactorialStep.Explanation("Переходим к вычислению ${n - 1}! (углубляемся в рекурсию)."),
                null
            )
        )

        val subResult = generateStepsRecursive(n - 1, level + 1, currentId)
        val result = n * subResult

        stepPairs.add(
            StepPair(
                FactorialStep.Explanation("Подсчитываем: $n! = $n × ${subResult} = $result"),
                FactorialStep.Visual(idCounter++, n, level, currentId, result)
            )
        )

        // Финальный комментарий при завершении
        if (n == 5) {
            stepPairs.add(
                StepPair(
                    FactorialStep.Explanation("Готово! Получили: 5! = 120."),
                    null
                )
            )
        }

        return result
    }
}

