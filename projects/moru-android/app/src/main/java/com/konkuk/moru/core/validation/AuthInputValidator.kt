package com.konkuk.moru.core.validation

object AuthInputValidator {
    private val emailPattern =
        Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")
    private const val specialCharacters = "!@#\$%^&*(),.?\":{}|<>"

    fun isEmailValid(email: String): Boolean =
        email.length <= 254 && emailPattern.matches(email)

    fun isPasswordValid(password: String): Boolean =
        password.length >= 8 &&
            password.any(Char::isDigit) &&
            password.any(specialCharacters::contains)

    fun isLoginFormValid(email: String, password: String): Boolean =
        email.isNotBlank() && password.isNotBlank()
}
