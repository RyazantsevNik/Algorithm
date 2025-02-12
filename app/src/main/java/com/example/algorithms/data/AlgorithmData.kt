package com.example.algorithms.data

data class AlgorithmCategory(
    val title: String,
    val items: List<AlgorithmItem>
)

data class AlgorithmItem(
    val title: String
)

val algorithmCategories = listOf(
    AlgorithmCategory(
        title = "Сортировка",
        items = listOf(
            AlgorithmItem("Сортировка пузырьком"),
            AlgorithmItem("Сортировка выбором"),
            AlgorithmItem("Быстрая сортировка")
        )
    ),
    AlgorithmCategory(
        title = "Поиск",
        items = listOf(
            AlgorithmItem("Линейный поиск"),
            AlgorithmItem("Бинарный поиск")
        )
    ),
    AlgorithmCategory(
        title = "Графы",
        items = listOf(
            AlgorithmItem("Обход в глубину"),
            AlgorithmItem("Обход в ширину")
        )
    ),
    AlgorithmCategory(
        title = "Математика",
        items = listOf(
            AlgorithmItem("Факториал"),
            AlgorithmItem("Числа Фибоначчи")
        )
    )
)