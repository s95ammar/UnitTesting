package com.s95ammar.unittesting.ui.registration

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.lifecycleScope
import com.s95ammar.unittesting.R
import com.s95ammar.unittesting.databinding.ActivityMainBinding
import com.s95ammar.unittesting.ui.registration.validation.IntRegistrationErrorCode
import com.s95ammar.unittesting.ui.registration.validation.StringRegistrationViewKey
import com.s95ammar.unittesting.setTextIfNotEquals
import kotlinx.coroutines.flow.collect

class RegistrationActivity : AppCompatActivity() {

    private val viewModel: RegistrationViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpViews()
        setUpObservers()
    }

    private fun setUpViews() {
        binding.inputLayoutEmail.editText?.doAfterTextChanged {
            viewModel.setEmail(it?.toString())
        }
        binding.inputLayoutPhoneNumber.editText?.doAfterTextChanged {
            viewModel.setPhoneNumber(it?.toString())
        }
        binding.inputLayoutPassword.editText?.doAfterTextChanged {
            viewModel.setPassword(it?.toString())
        }
        binding.inputLayoutPasswordConfirmation.editText?.doAfterTextChanged {
            viewModel.setPasswordConfirmation(it?.toString())
        }
        binding.buttonRegister.setOnClickListener { viewModel.onRegister() }
    }

    private fun setUpObservers() {
        viewModel.email.observe(this) { setEmail(it) }
        viewModel.phoneNumber.observe(this) { setPhoneNumber(it) }
        viewModel.password.observe(this) { setPassword(it) }
        viewModel.passwordConfirmation.observe(this) { setPasswordConfirmation(it) }
        viewModel.validationData.observe(this) { setValidationData(it) }

        lifecycleScope.launchWhenStarted {
            viewModel.registrationSuccessfulEvent.collect {
                onRegistrationSuccessful()
            }
        }
    }

    private fun setEmail(email: String?) {
        binding.inputLayoutEmail.editText?.setTextIfNotEquals(email)
    }

    private fun setPhoneNumber(phoneNumber: String?) {
        binding.inputLayoutPhoneNumber.editText?.setTextIfNotEquals(phoneNumber)
    }

    private fun setPassword(password: String?) {
        binding.inputLayoutPassword.editText?.setTextIfNotEquals(password)
    }

    private fun setPasswordConfirmation(passwordConfirmation: String?) {
        binding.inputLayoutPasswordConfirmation.editText?.setTextIfNotEquals(passwordConfirmation)
    }

    private fun setValidationData(validationData: Map<@StringRegistrationViewKey String, @IntRegistrationErrorCode Int>) {
        for ((viewKey, errorCode) in validationData) {

            val errorString = getErrorString(errorCode)

            when (viewKey) {
                StringRegistrationViewKey.VIEW_EMAIL -> setEmailError(errorString)
                StringRegistrationViewKey.VIEW_PHONE -> setPhoneNumberError(errorString)
                StringRegistrationViewKey.VIEW_PASSWORD -> setPasswordError(errorString)
                StringRegistrationViewKey.VIEW_PASSWORD_CONFIRMATION -> setPasswordConfirmationError(errorString)
            }
        }
    }

    private fun getErrorString(@IntRegistrationErrorCode errorCode: Int) = when(errorCode) {
        IntRegistrationErrorCode.EMPTY_EMAIL -> getString(R.string.required_field)
        IntRegistrationErrorCode.INVALID_EMAIL -> getString(R.string.invalid_email)
        IntRegistrationErrorCode.EMPTY_PHONE_NUMBER -> getString(R.string.required_field)
        IntRegistrationErrorCode.INVALID_PHONE_NUMBER -> getString(R.string.invalid_phone_number)
        IntRegistrationErrorCode.EMPTY_PASSWORD -> getString(R.string.required_field)
        IntRegistrationErrorCode.PASSWORD_TOO_SHORT -> getString(R.string.password_too_short)
        IntRegistrationErrorCode.INVALID_PASSWORD -> getString(R.string.invalid_password)
        IntRegistrationErrorCode.EMPTY_PASSWORD_CONFIRMATION -> getString(R.string.required_field)
        IntRegistrationErrorCode.PASSWORDS_DO_NOT_MATCH -> getString(R.string.passwords_do_not_match)
        else -> null
    }

    private fun setEmailError(errorString: String?) {
        binding.inputLayoutEmail.error = errorString
    }

    private fun setPhoneNumberError(errorString: String?) {
        binding.inputLayoutPhoneNumber.error = errorString
    }

    private fun setPasswordError(errorString: String?) {
        binding.inputLayoutPassword.error = errorString
    }

    private fun setPasswordConfirmationError(errorString: String?) {
        binding.inputLayoutPasswordConfirmation.error = errorString
    }

    private fun onRegistrationSuccessful() {
        Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()
    }

}