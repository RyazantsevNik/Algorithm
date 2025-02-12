package com.example.algorithms.navigation

object AppRoutes {
    //Меню
    const val MENU = "menu"

    //Выбор теория/практика алгоритма
    const val ALGORITHM_SELECTION = "algorithm_selection/{algorithmTitle}"
    const val THEORY_SCREEN = "theory_screen/{algorithmTitle}"
    const val PRACTISE_SCREEN = "practice_screen/{algorithmTitle}"

    // Функции для создания маршрутов с параметрами
    fun algorithmSelectionRoute(algorithmTitle: String): String {
        return "algorithm_selection/$algorithmTitle"
    }

    fun theoryScreenRoute(algorithmTitle: String): String {
        return "theory_screen/$algorithmTitle"
    }

    fun practiceScreenRoute(algorithmTitle: String): String {
        return "practice_screen/$algorithmTitle"
    }
}