package com.numq.firebasechat.auth

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.Password
import androidx.compose.material.icons.rounded.Visibility
import androidx.compose.material.icons.rounded.VisibilityOff
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation

@Composable
fun PasswordField(
    placeholder: String,
    maxLength: Int,
    password: String,
    setPassword: (String) -> Unit,
    validateInput: (String) -> String
) {
    val emptyString = ""
    val (passwordVisible, setPasswordVisible) = rememberSaveable {
        mutableStateOf(false)
    }
    TextField(
        value = password,
        onValueChange = { if (it.length <= maxLength) setPassword(validateInput(it)) },
        singleLine = true,
        placeholder = { Text(placeholder) },
        leadingIcon = {
            if (password.isNotBlank()) {
                IconButton(onClick = {
                    setPasswordVisible(!passwordVisible)
                }) {
                    if (passwordVisible) Icon(
                        Icons.Rounded.Visibility,
                        "visible password icon"
                    )
                    else Icon(Icons.Rounded.VisibilityOff, "hidden password icon")
                }
            } else Icon(Icons.Rounded.Password, "password icon")
        },
        trailingIcon = {
            if (password.isNotBlank())
                IconButton(onClick = {
                    setPassword(emptyString)
                    setPasswordVisible(false)
                }) {
                    Icon(Icons.Rounded.Clear, "clear icon")
                }
        },
        keyboardOptions = KeyboardOptions(
            autoCorrect = false,
            keyboardType = KeyboardType.Password
        ),
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation()
    )
}