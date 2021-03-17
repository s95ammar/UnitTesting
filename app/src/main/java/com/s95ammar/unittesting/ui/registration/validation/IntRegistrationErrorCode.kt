package com.s95ammar.unittesting.ui.registration.validation

import androidx.annotation.IntDef

@Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.TYPE)
@IntDef(
    IntRegistrationErrorCode.EMPTY_EMAIL,
    IntRegistrationErrorCode.INVALID_EMAIL,
    IntRegistrationErrorCode.EMPTY_PHONE_NUMBER,
    IntRegistrationErrorCode.INVALID_PHONE_NUMBER,
    IntRegistrationErrorCode.EMPTY_PASSWORD,
    IntRegistrationErrorCode.PASSWORD_TOO_SHORT,
    IntRegistrationErrorCode.INVALID_PASSWORD,
    IntRegistrationErrorCode.EMPTY_PASSWORD_CONFIRMATION,
    IntRegistrationErrorCode.PASSWORDS_DO_NOT_MATCH
)
@Retention(AnnotationRetention.SOURCE)
annotation class IntRegistrationErrorCode {
    companion object {
        const val EMPTY_EMAIL = 1
        const val INVALID_EMAIL = 2

        const val EMPTY_PHONE_NUMBER = 3
        const val INVALID_PHONE_NUMBER = 4

        const val EMPTY_PASSWORD = 5
        const val PASSWORD_TOO_SHORT = 6
        const val INVALID_PASSWORD = 7
        const val EMPTY_PASSWORD_CONFIRMATION = 8
        const val PASSWORDS_DO_NOT_MATCH = 9
    }
}