package com.s95ammar.unittesting.ui.registration.validation

object RegistrationValidationUtil {
    const val PASSWORD_MIN_LENGTH = 8

    const val PATTERN_EMAIL = "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
            "\\@" +
            "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
            "(" +
            "\\." +
            "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
            ")+"
    const val PATTERN_PHONE = "(\\+[0-9]+[\\- \\.]*)?" +
            "(\\([0-9]+\\)[\\- \\.]*)?" +
            "([0-9][0-9\\- \\.]+[0-9])"
    const val PATTERN_PASSWORD = "(^[a-zA-Z0-9]{$PASSWORD_MIN_LENGTH,})$"
}