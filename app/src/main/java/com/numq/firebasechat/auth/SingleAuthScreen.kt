package com.numq.firebasechat.auth

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Done
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SingleAuthScreen(
    isSignUp: Boolean,
    onSignIn: (String, String) -> Unit,
    onSignUp: (String, String, String) -> Unit,
    onCancelAuthentication: () -> Unit,
    validator: InputValidator = InputValidator
) {

    val emptyString = ""
    val (name, setName) = rememberSaveable {
        mutableStateOf(emptyString)
    }
    val (email, setEmail) = rememberSaveable {
        mutableStateOf(emptyString)
    }
    val (password, setPassword) = rememberSaveable {
        mutableStateOf(emptyString)
    }
    val (confirmedPassword, setConfirmedPassword) = rememberSaveable {
        mutableStateOf(emptyString)
    }
    val (fieldsAreFilled, setFieldsAreFilled) = rememberSaveable {
        mutableStateOf(false)
    }
    val (authenticating, setAuthenticating) = rememberSaveable {
        mutableStateOf(false)
    }

    LaunchedEffect(isSignUp) {
        setName(emptyString)
        setConfirmedPassword(emptyString)
    }

    LaunchedEffect(name, email, password, confirmedPassword) {
        val nameFilled = listOf(
            name.isNotBlank(),
            (name.length in validator.nameConstraints)
        ).all { true }
        val emailFilled = listOf(
            email.isNotBlank(),
            (email.length in validator.emailConstraints),
            validator.emailPattern.matches(email)
        ).all { true }
        val passwordFilled = listOf(
            password.isNotBlank(),
            (password.length in validator.passwordConstraints)
        ).all { true }
        if (isSignUp) {
            val confirmedPasswordFilled = listOf(
                confirmedPassword.isNotBlank(),
                (confirmedPassword.length in validator.passwordConstraints)
            ).all { true }
            setFieldsAreFilled(nameFilled && emailFilled && passwordFilled && confirmedPasswordFilled)
        } else {
            setFieldsAreFilled(emailFilled && passwordFilled)
        }
    }

    Box(Modifier.padding(8.dp), contentAlignment = Alignment.Center) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(4.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            AnimatedVisibility(visible = isSignUp) {
                NameField(
                    placeholder = "Name",
                    maxLength = validator.nameConstraints.last,
                    name,
                    setName,
                    validator::validateString,
                    validator::validateName
                )
                Spacer(modifier = Modifier.height(4.dp))
            }
            EmailField(
                placeholder = "Email",
                maxLength = validator.emailConstraints.last,
                email,
                setEmail,
                validator::validateString,
                validator::validateEmail
            )
            Spacer(modifier = Modifier.height(4.dp))
            PasswordField(
                placeholder = "Password",
                maxLength = validator.passwordConstraints.last,
                password,
                setPassword,
                validator::validateString,
                validator::validatePassword
            )
            Spacer(modifier = Modifier.height(4.dp))
            AnimatedVisibility(visible = isSignUp) {
                PasswordField(
                    placeholder = "Confirmed password",
                    maxLength = validator.passwordConstraints.last,
                    confirmedPassword,
                    setConfirmedPassword,
                    validator::validateString,
                    validator::validatePassword
                )
            }
            if (authenticating) {
                CircularProgressIndicator(Modifier.clickable {
                    onCancelAuthentication()
                    setAuthenticating(false)
                })
            } else {
                IconButton(onClick = {
                    setAuthenticating(true)
                    if (isSignUp) {
                        onSignUp(name, email, password)
                    } else {
                        onSignIn(email, password)
                    }
                }, enabled = fieldsAreFilled) {
                    Icon(Icons.Rounded.Done, "Confirm icon")
                }
            }
        }
    }
}