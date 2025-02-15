package com.example.algorithms.navigation

object AppRoutes {
    const val HOME = "home"
    const val ALGORITHMS = "algorithms"
    const val AI_CHAT = "ai_chat"
    const val PROFILE = "profile"
    const val AUTH_SCREEN = "auth_screen"

    // Подмаршруты
    private const val ALGORITHM_SELECTION_BASE = "algorithm_selection"
    private const val THEORY_SCREEN_BASE = "theory_screen"
    private const val PRACTISE_SCREEN_BASE = "practise_screen"


    const val ALGORITHM_SELECTION = "$ALGORITHM_SELECTION_BASE/{algorithmTitle}"
    const val THEORY_SCREEN = "$THEORY_SCREEN_BASE/{algorithmTitle}"
    const val PRACTISE_SCREEN = "$PRACTISE_SCREEN_BASE/{algorithmTitle}"

    fun algorithmSelectionRoute(algorithmTitle: String) = "$ALGORITHM_SELECTION_BASE/$algorithmTitle"
    fun theoryScreenRoute(algorithmTitle: String) = "$THEORY_SCREEN_BASE/$algorithmTitle"
    fun practiceScreenRoute(algorithmTitle: String) = "$PRACTISE_SCREEN_BASE/$algorithmTitle"
}