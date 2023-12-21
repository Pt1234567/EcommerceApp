package com.example.application.util

sealed class RegisterValidation{
    data object Success:RegisterValidation()
    data class failure(val message:String):RegisterValidation()
}

data class RegisterFieldState(
    val email:RegisterValidation,
    val password:RegisterValidation
)
