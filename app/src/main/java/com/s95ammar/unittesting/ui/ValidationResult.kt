package com.s95ammar.unittesting.ui

data class ValidationResult<T>(val outputData: T, val viewKey: String, val errorCode: Int) {

    companion object {
        const val ERROR_CODE_NONE = 0

        const val VIEW_KEY_NONE = "VIEW_KEY_NONE"
    }

}
