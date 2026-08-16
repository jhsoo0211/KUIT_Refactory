package com.konkuk.moru.presentation.onboarding.model

enum class NickNameValid {
    VALID,
    INVALID,
    EMPTY;

    fun isValid(): Boolean = this == VALID

    fun isInvalid(): Boolean = this == INVALID

    fun isEmpty(): Boolean = this == EMPTY
}