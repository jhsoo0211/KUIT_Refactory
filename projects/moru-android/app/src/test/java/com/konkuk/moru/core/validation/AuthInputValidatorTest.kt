package com.konkuk.moru.core.validation

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class AuthInputValidatorTest {
    @Test
    fun emailRequiresLocalPartDomainAndTopLevelDomain() {
        assertTrue(AuthInputValidator.isEmailValid("moru@example.com"))
        assertFalse(AuthInputValidator.isEmailValid("moru"))
        assertFalse(AuthInputValidator.isEmailValid("@example.com"))
        assertFalse(AuthInputValidator.isEmailValid("moru@example"))
    }

    @Test
    fun passwordRequiresEightCharactersDigitAndSpecialCharacter() {
        assertTrue(AuthInputValidator.isPasswordValid("routine1!"))
        assertFalse(AuthInputValidator.isPasswordValid("short1!"))
        assertFalse(AuthInputValidator.isPasswordValid("routine!"))
        assertFalse(AuthInputValidator.isPasswordValid("routine1"))
    }

    @Test
    fun loginRequiresBothFields() {
        assertTrue(AuthInputValidator.isLoginFormValid("moru@example.com", "password"))
        assertFalse(AuthInputValidator.isLoginFormValid("", "password"))
        assertFalse(AuthInputValidator.isLoginFormValid("moru@example.com", ""))
    }
}
