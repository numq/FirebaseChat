package com.numq.firebasechat.auth

object InputValidator {

    val emailPattern = Regex("^[\\w.]+@[\\w]+.[\\w]{2,4}\$")
    val nameConstraints = 3..16
    val emailConstraints = 3..32
    val passwordConstraints = 8..32

    fun validateString(input: String) =
        input.filter { it in arrayOf('.', '@') || it.isLetterOrDigit() }

    fun validateName(name: String) = name.length in nameConstraints
    fun validateEmail(email: String) = email.length in emailConstraints
    fun validatePassword(password: String) = password.length in passwordConstraints

}