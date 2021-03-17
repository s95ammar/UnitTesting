package com.s95ammar.unittesting.ui.registration

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.s95ammar.unittesting.*
import com.s95ammar.unittesting.ui.ValidationResult
import com.s95ammar.unittesting.ui.ValidationResult.Companion.ERROR_CODE_NONE
import com.s95ammar.unittesting.ui.ValidationResult.Companion.VIEW_KEY_NONE
import com.s95ammar.unittesting.ui.registration.data.UserRegistrationBundle
import com.s95ammar.unittesting.ui.registration.data.UserValidationBundle
import com.s95ammar.unittesting.ui.registration.validation.IntRegistrationErrorCode
import com.s95ammar.unittesting.ui.registration.validation.RegistrationValidationUtil
import com.s95ammar.unittesting.ui.registration.validation.StringRegistrationViewKey
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.util.regex.Pattern

class RegistrationViewModel(handle: SavedStateHandle) : ViewModel() {

    companion object {
        const val KEY_EMAIL = "KEY_EMAIL"
        const val KEY_PHONE_NUMBER = "KEY_PHONE_NUMBER"
        const val KEY_PASSWORD = "KEY_PASSWORD"
        const val KEY_PASSWORD_CONFIRMATION = "KEY_PASSWORD_CONFIRMATION"
        const val KEY_VALIDATION_DATA = "KEY_VALIDATION_DATA"
    }

    private val _email = handle.getLiveData<String?>(KEY_EMAIL)
    private val _phoneNumber = handle.getLiveData<String?>(KEY_PHONE_NUMBER)
    private val _password = handle.getLiveData<String?>(KEY_PASSWORD)
    private val _passwordConfirmation = handle.getLiveData<String?>(KEY_PASSWORD_CONFIRMATION)
    private val _validationData = handle.getLiveData<Map<@StringRegistrationViewKey String, @IntRegistrationErrorCode Int>>(KEY_VALIDATION_DATA)

    private val _registrationSuccessfulEventChannel = Channel<Unit>()

    val email = _email.asLiveData()
    val phoneNumber = _phoneNumber.asLiveData()
    val password = _password.asLiveData()
    val passwordConfirmation = _passwordConfirmation.asLiveData()
    val validationData = _validationData.asLiveData()

    val registrationSuccessfulEvent = _registrationSuccessfulEventChannel.receiveAsFlow()

    fun setEmail(email: String?) {
        _email.value = email
    }

    fun setPhoneNumber(phoneNumber: String?) {
        _phoneNumber.value = phoneNumber
    }

    fun setPassword(password: String?) {
        _password.value = password
    }

    fun setPasswordConfirmation(passwordConfirmation: String?) {
        _passwordConfirmation.value = passwordConfirmation
    }

    fun onRegister() {
        val fullValidationResult = validateAll(
            UserValidationBundle(
                email = _email.value.orEmpty(),
                phoneNumber = _phoneNumber.value.orEmpty(),
                password = _password.value.orEmpty(),
                passwordConfirmation = _passwordConfirmation.value.orEmpty()
            )
        )

        if (fullValidationResult.errorCode == ERROR_CODE_NONE) {
            register(fullValidationResult.outputData)
        }
    }

    fun validateAll(userValidationBundle: UserValidationBundle): ValidationResult<UserRegistrationBundle> {
        val emailValidation = validateEmail(userValidationBundle.email)
        val phoneValidation = validatePhoneNumber(userValidationBundle.phoneNumber)
        val passwordValidation = validatePassword(userValidationBundle.password)
        val passwordConfirmationValidation = validatePasswordConfirmation(
            userValidationBundle.password,
            userValidationBundle.passwordConfirmation
        )

        _validationData.value = mapOf(
            emailValidation.viewKey to emailValidation.errorCode,
            phoneValidation.viewKey to phoneValidation.errorCode,
            passwordValidation.viewKey to passwordValidation.errorCode,
            passwordConfirmationValidation.viewKey to passwordConfirmationValidation.errorCode,
        )

        val errorCodesNonNone = listOf(emailValidation, phoneValidation, passwordValidation, passwordConfirmationValidation)
            .map { it.errorCode }
            .filter { it != ERROR_CODE_NONE }

        return ValidationResult(
            outputData = UserRegistrationBundle(
                email = emailValidation.outputData,
                phoneNumber = phoneValidation.outputData,
                password = passwordValidation.outputData
            ),
            viewKey = VIEW_KEY_NONE,
            errorCode = if (errorCodesNonNone.isEmpty()) ERROR_CODE_NONE else errorCodesNonNone.first()
        )

    }

    fun validateEmail(emailInput: String): ValidationResult<String> {

        return when {
            emailInput.isEmpty() -> ValidationResult(
                outputData = emailInput,
                viewKey = StringRegistrationViewKey.VIEW_EMAIL,
                errorCode = IntRegistrationErrorCode.EMPTY_EMAIL
            )
            !Pattern.compile(RegistrationValidationUtil.PATTERN_EMAIL).matcher(emailInput).matches() -> ValidationResult(
                outputData = emailInput,
                viewKey = StringRegistrationViewKey.VIEW_EMAIL,
                errorCode = IntRegistrationErrorCode.INVALID_EMAIL
            )
            else -> ValidationResult(
                outputData = emailInput,
                viewKey = StringRegistrationViewKey.VIEW_EMAIL,
                errorCode = ERROR_CODE_NONE
            )
        }
    }

    fun validatePhoneNumber(phoneNumber: String): ValidationResult<String> {

        return when {
            phoneNumber.isEmpty() -> ValidationResult(
                outputData = "",
                viewKey = StringRegistrationViewKey.VIEW_PHONE,
                errorCode = IntRegistrationErrorCode.EMPTY_PHONE_NUMBER
            )
            !Pattern.compile(RegistrationValidationUtil.PATTERN_PHONE).matcher(phoneNumber).matches() -> ValidationResult(
                outputData = "",
                viewKey = StringRegistrationViewKey.VIEW_PHONE,
                errorCode = IntRegistrationErrorCode.INVALID_PHONE_NUMBER
            )
            else -> ValidationResult(
                outputData = phoneNumber.filter { it.isDigit() },
                viewKey = StringRegistrationViewKey.VIEW_PHONE,
                errorCode = ERROR_CODE_NONE
            )
        }
    }

    fun validatePassword(password: String): ValidationResult<String> {

        return when {
            password.isEmpty() -> ValidationResult(
                outputData = password,
                viewKey = StringRegistrationViewKey.VIEW_PASSWORD,
                errorCode = IntRegistrationErrorCode.EMPTY_PASSWORD
            )
            password.length < RegistrationValidationUtil.PASSWORD_MIN_LENGTH -> ValidationResult(
                outputData = password,
                viewKey = StringRegistrationViewKey.VIEW_PASSWORD,
                errorCode = IntRegistrationErrorCode.PASSWORD_TOO_SHORT
            )
            !Pattern.compile(RegistrationValidationUtil.PATTERN_PASSWORD).matcher(password).matches() -> ValidationResult(
                outputData = password,
                viewKey = StringRegistrationViewKey.VIEW_PASSWORD,
                errorCode = IntRegistrationErrorCode.INVALID_PASSWORD
            )
            else -> ValidationResult(
                outputData = password,
                viewKey = StringRegistrationViewKey.VIEW_PASSWORD,
                errorCode = ERROR_CODE_NONE
            )
        }

    }

    fun validatePasswordConfirmation(password: String, passwordConfirmation: String): ValidationResult<String> {

        return when {
            passwordConfirmation.isEmpty() -> ValidationResult(
                outputData = password,
                viewKey = StringRegistrationViewKey.VIEW_PASSWORD_CONFIRMATION,
                errorCode = IntRegistrationErrorCode.EMPTY_PASSWORD_CONFIRMATION
            )
            password != passwordConfirmation -> ValidationResult(
                outputData = password,
                viewKey = StringRegistrationViewKey.VIEW_PASSWORD_CONFIRMATION,
                errorCode = IntRegistrationErrorCode.PASSWORDS_DO_NOT_MATCH
            )
            else -> ValidationResult(
                outputData = password,
                viewKey = StringRegistrationViewKey.VIEW_PASSWORD_CONFIRMATION,
                errorCode = ERROR_CODE_NONE
            )
        }

    }

    private fun register(userRegistrationBundle: UserRegistrationBundle) {

        viewModelScope.launch {
            // fake registration process
            _registrationSuccessfulEventChannel.send(Unit)
        }
    }

}