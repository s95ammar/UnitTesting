package com.s95ammar.unittesting.ui.registration

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.SavedStateHandle
import com.s95ammar.unittesting.ui.ValidationResult
import com.s95ammar.unittesting.ui.registration.data.UserValidationBundle
import com.s95ammar.unittesting.ui.registration.validation.IntRegistrationErrorCode
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

class RegistrationViewModelTest {

    private lateinit var viewModel: RegistrationViewModel

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Before
    fun setupViewModel() {
        viewModel = RegistrationViewModel(SavedStateHandle())
    }

    // Email

    @Test
    fun validateEmail_emailIsEmpty_returnsValidationResultWitCorrectErrorCode() {
        val emailInput = ""

        val (_, _, errorCode) = viewModel.validateEmail(emailInput)

        assertEquals(IntRegistrationErrorCode.EMPTY_EMAIL, errorCode)
    }

    @Test
    fun validateEmail_emailIsInvalid_returnsValidationResultWithCorrectErrorCode() {
        val emailInput = "some.invalid@email"

        val (_, _, errorCode) = viewModel.validateEmail(emailInput)

        assertEquals(IntRegistrationErrorCode.INVALID_EMAIL, errorCode)
    }

    @Test
    fun validateEmail_emailIsValid_returnsValidationResultWithCorrectErrorCode() {
        val emailInput = "some@valid.email"

        val (_, _, errorCode) = viewModel.validateEmail(emailInput)

        assertEquals(ValidationResult.ERROR_CODE_NONE, errorCode)
    }

    // Phone number

    @Test
    fun validatePhoneNumber_phoneNumberIsEmpty_returnsValidationResultWithCorrectErrorCode() {
        val phoneNumber = ""

        val (_, _, errorCode) = viewModel.validatePhoneNumber(phoneNumber)

        assertEquals(IntRegistrationErrorCode.EMPTY_PHONE_NUMBER, errorCode)
    }

    @Test
    fun validatePhoneNumber_phoneNumberIsInvalid_returnsValidationResultWithCorrectErrorCode() {
        val phoneNumber = "(012) 345 - 67 89 abc"

        val (_, _, errorCode) = viewModel.validatePhoneNumber(phoneNumber)

        assertEquals(IntRegistrationErrorCode.INVALID_PHONE_NUMBER, errorCode)
    }

    @Test
    fun validatePhoneNumber_phoneNumberIsValid_returnsValidationResultWithCorrectErrorCode() {
        val phoneNumber = "(012) 345 - 67 89"

        val (_, _, errorCode) = viewModel.validatePhoneNumber(phoneNumber)

        assertEquals(ValidationResult.ERROR_CODE_NONE, errorCode)
    }

    // Password

    @Test
    fun validatePassword_passwordIsEmpty_returnsValidationResultWithCorrectErrorCode() {
        val password = ""

        val (_, _, errorCode) = viewModel.validatePassword(password)

        assertEquals(IntRegistrationErrorCode.EMPTY_PASSWORD, errorCode)
    }

    @Test
    fun validatePassword_passwordIsTooShort_returnsValidationResultWithCorrectErrorCode() {
        val password = "qwerty"

        val (_, _, errorCode) = viewModel.validatePassword(password)

        assertEquals(IntRegistrationErrorCode.PASSWORD_TOO_SHORT, errorCode)
    }

    @Test
    fun validatePassword_passwordIsInvalid_returnsValidationResultWithCorrectErrorCode() {
        val password = "@qwerty123456"

        val (_, _, errorCode) = viewModel.validatePassword(password)

        assertEquals(IntRegistrationErrorCode.INVALID_PASSWORD, errorCode)
    }

    // Password confirmation

    @Test
    fun validatePasswordConfirmation_confirmationIsEmpty_returnsValidationResultWithCorrectErrorCode() {
        val password = "qwerty123456"
        val passwordConfirmation = ""

        val (_, _, errorCode) = viewModel.validatePasswordConfirmation(password, passwordConfirmation)

        assertEquals(IntRegistrationErrorCode.EMPTY_PASSWORD_CONFIRMATION, errorCode)
    }

    @Test
    fun validatePasswordConfirmation_passwordsDoNotMatch_returnsValidationResultWithCorrectErrorCode() {
        val password = "qwerty123456"
        val passwordConfirmation = "qwerty123455"

        val (_, _, errorCode) = viewModel.validatePasswordConfirmation(password, passwordConfirmation)

        assertEquals(IntRegistrationErrorCode.PASSWORDS_DO_NOT_MATCH, errorCode)
    }

    @Test
    fun validatePasswordConfirmation_passwordsMatch_returnsValidationResultWithCorrectErrorCode() {
        val password = "qwerty123456"
        val passwordConfirmation = "qwerty123456"

        val (_, _, errorCode) = viewModel.validatePasswordConfirmation(password, passwordConfirmation)

        assertEquals(ValidationResult.ERROR_CODE_NONE, errorCode)
    }

    // All

    @Test
    fun validateAll_someInputIsNotValid_returnsValidationResultWithErrorCodeNone() {
        val userRegistrationBundle = UserValidationBundle(
            email = "some@valid.email",
            phoneNumber = "0123456789",
            password = "qwerty123456",
            passwordConfirmation = "qwerty1234567"
        )

        val (_, _, errorCode) = viewModel.validateAll(userRegistrationBundle)

        assertNotEquals(ValidationResult.ERROR_CODE_NONE, errorCode)
    }

    @Test
    fun validateAll_allInputIsValid_returnsValidationResultWithErrorCodeNone() {
        val userRegistrationBundle = UserValidationBundle(
            email = "some@valid.email",
            phoneNumber = "0123456789",
            password = "qwerty123456",
            passwordConfirmation = "qwerty123456"
        )

        val (_, _, errorCode) = viewModel.validateAll(userRegistrationBundle)

        assertEquals(ValidationResult.ERROR_CODE_NONE, errorCode)
    }

}