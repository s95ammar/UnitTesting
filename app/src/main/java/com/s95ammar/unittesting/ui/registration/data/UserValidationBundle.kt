package com.s95ammar.unittesting.ui.registration.data

data class UserValidationBundle(
    val email: String,
    val phoneNumber: String,
    val password: String,
    val passwordConfirmation: String
)
