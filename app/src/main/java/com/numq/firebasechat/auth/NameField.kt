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
fun NameField(
    placeholder: String,
    maxLength: Int,
    name: String,
    setName: (String) -> Unit,
    validateInput: (String) -> String,
    isNameValid: (String) -> Boolean
) {
    val emptyString = ""
    TextField(
        value = name,
        onValueChange = { if (it.length <= maxLength) setName(validateInput(it)) },
        singleLine = true,
        placeholder = { Text(placeholder) },
        leadingIcon = { Icon(Icons.Rounded.Person, "person icon") },
        trailingIcon = {
            if (name.isNotBlank())
                IconButton(onClick = {
                    setName(emptyString)
                }) {
                    Icon(Icons.Rounded.Clear, "clear icon")
                }
        }, isError = isNameValid(name)
    )
}