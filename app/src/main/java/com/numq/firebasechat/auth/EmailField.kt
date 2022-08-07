package com.numq.firebasechat.auth

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.Person
import androidx.compose.runtime.Composable

@Composable
fun EmailField(
    placeholder: String,
    maxLength: Int,
    email: String,
    setEmail: (String) -> Unit,
    validateInput: (String) -> String
) {
    val emptyString = ""
    TextField(
        value = email,
        onValueChange = { if (it.length <= maxLength) setEmail(validateInput(it)) },
        singleLine = true,
        placeholder = { Text(placeholder) },
        leadingIcon = { Icon(Icons.Rounded.Person, "person icon") },
        trailingIcon = {
            if (email.isNotBlank())
                IconButton(onClick = {
                    setEmail(emptyString)
                }) {
                    Icon(Icons.Rounded.Clear, "clear icon")
                }
        }
    )
}