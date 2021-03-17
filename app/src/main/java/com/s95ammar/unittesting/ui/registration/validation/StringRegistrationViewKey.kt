package com.s95ammar.unittesting.ui.registration.validation

import androidx.annotation.StringDef

@Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.TYPE)
@StringDef(
    StringRegistrationViewKey.VIEW_EMAIL,
    StringRegistrationViewKey.VIEW_PHONE,
    StringRegistrationViewKey.VIEW_PASSWORD,
    StringRegistrationViewKey.VIEW_PASSWORD_CONFIRMATION
)
@Retention(AnnotationRetention.SOURCE)
annotation class StringRegistrationViewKey {
    companion object {
        const val VIEW_EMAIL = "VIEW_EMAIL"
        const val VIEW_PHONE = "VIEW_PHONE"
        const val VIEW_PASSWORD = "VIEW_PASSWORD"
        const val VIEW_PASSWORD_CONFIRMATION = "VIEW_PASSWORD_CONFIRMATION"
    }
}