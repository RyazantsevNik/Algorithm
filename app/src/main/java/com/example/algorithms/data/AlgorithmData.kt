package com.example.algorithms.data

data class AlgorithmCategory(
    val title: String,
    val items: List<AlgorithmItem>
)

data class AlgorithmItem(
    val title: String,
    val isCompleted: Boolean = false,
    val starRating: Int // Количество золотых звёзд (от 0 до 3)
)

val algorithmCategories = listOf(
    AlgorithmCategory(
        title = "Сортировка",
        items = listOf(
            AlgorithmItem("Сортировка пузырьком",
                isCompleted = true,
                starRating = 1),
            AlgorithmItem("Сортировка выбором",
                isCompleted = true,
                starRating = 1),
            AlgorithmItem("Сортировка вставками",
                isCompleted = true,
                starRating = 2),
            AlgorithmItem("Быстрая сортировка",
                isCompleted = true,
                starRating = 3)
        )
    ),
    AlgorithmCategory(
        title = "Поиск",
        items = listOf(
            AlgorithmItem("Линейный поиск",
                isCompleted = false,
                starRating = 1),
            AlgorithmItem("Бинарный поиск",
                isCompleted = true,
                starRating = 2)
        )
    ),
    AlgorithmCategory(
        title = "Графы",
        items = listOf(
            AlgorithmItem("Обход в глубину",
                isCompleted = false,
                starRating = 2),
            AlgorithmItem("Обход в ширину",
                isCompleted = true,
                starRating = 2)
        )
    ),
    AlgorithmCategory(
        title = "Математика",
        items = listOf(
            AlgorithmItem("Факториал",
                isCompleted = false,
                starRating = 1),
            AlgorithmItem("Числа Фибоначчи",
                isCompleted = true,
                starRating = 1)
        )
    )
)