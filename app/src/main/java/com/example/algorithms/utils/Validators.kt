package com.example.algorithms.utils

object Validators {
    private val USERNAME_PATTERN = "^[a-zA-Z0-9]{3,}$".toRegex()
    private val EMAIL_PATTERN = "[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}".toRegex()
    private val PASSWORD_PATTERN = "^[a-zA-Z0-9]+$".toRegex()

    fun validateUsername(username: String): ValidationResult {
        return when {
            username.isEmpty() -> ValidationResult(false, "Имя пользователя не может быть пустым")
            username.length < 3 -> ValidationResult(false, "Минимум 3 символа")
            !username.matches(USERNAME_PATTERN) -> ValidationResult(false, "Только буквы и цифры")
            else -> ValidationResult(true)
        }
    }

    fun validateEmail(email: String): ValidationResult {
        return when {
            email.isEmpty() -> ValidationResult(false, "Email не может быть пустым")
            !email.matches(EMAIL_PATTERN) -> ValidationResult(false, "Неверный формат email")
            else -> ValidationResult(true)
        }
    }

    fun validatePassword(password: String): ValidationResult {
        return when {
            password.isEmpty() -> ValidationResult(false, "Пароль не может быть пустым")
            password.length < 6 -> ValidationResult(false, "Минимум 6 символов")
            !password.matches(PASSWORD_PATTERN) -> ValidationResult(false, "Только буквы и цифры")
            else -> ValidationResult(true)
        }
    }
}

data class ValidationResult(
    val isValid: Boolean,
    val errorMessage: String? = null
) 