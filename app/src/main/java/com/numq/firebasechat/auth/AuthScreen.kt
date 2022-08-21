package com.numq.firebasechat.auth

import androidx.compose.foundation.layout.*
import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.numq.firebasechat.error.ShowError

@Composable
fun AuthScreen(
    scaffoldState: ScaffoldState,
    navigateToHome: (String) -> Unit,
    vm: AuthViewModel = hiltViewModel()
) {

    val (isSignUp, setIsSignUp) = rememberSaveable {
        mutableStateOf(false)
    }

    val onSignIn: (String, String) -> Unit = { email, password ->
        vm.signIn(email, password)
    }
    val onSignUp: (String, String, String) -> Unit = { name, email, password ->
        vm.signUp(name, email, password)
    }
    val onCancelAuthentication: () -> Unit = vm::cancelAuth

    val state by vm.state.collectAsState()

    state.exception?.let {
        when (it) {
            is FirebaseAuthInvalidUserException -> Unit
            is FirebaseAuthInvalidCredentialsException -> Unit
            else -> ShowError(scaffoldState, it, vm.cleanUpError)
        }
    }

    state.userId?.let { userId ->
        LaunchedEffect(Unit) {
            navigateToHome(userId)
        }
    }

    BoxWithConstraints(
        Modifier
            .fillMaxSize()
            .padding(8.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        val offset = maxHeight.div(3)
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            SingleAuthScreen(isSignUp, onSignIn, onSignUp, onCancelAuthentication)
            ToggleAuthMode(isSignUp, setIsSignUp)
            Spacer(Modifier.height(offset))
        }
    }
}