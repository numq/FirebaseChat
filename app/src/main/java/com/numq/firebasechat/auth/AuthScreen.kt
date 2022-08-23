package com.numq.firebasechat.auth

import androidx.compose.foundation.layout.*
import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.numq.firebasechat.error.ShowError

@Composable
fun AuthScreen(
    scaffoldState: ScaffoldState,
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
        ShowError(scaffoldState, it, vm.cleanUpError)
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