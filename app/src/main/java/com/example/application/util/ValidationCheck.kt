package com.example.application.util

import android.util.Patterns
import java.util.regex.Pattern

fun validateEmail(email:String):RegisterValidation{
    if(email.isEmpty()) return RegisterValidation.failure("Email can't be empty")
    if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) return RegisterValidation.failure("Wrong email format")

    return RegisterValidation.Success
}

fun validatePassword(password:String):RegisterValidation{
    if(password.isEmpty()) return RegisterValidation.failure("Password can't be Empty")
    if(password.length<6) return RegisterValidation.failure("Password length must be greater than 6")
    return RegisterValidation.Success
}
