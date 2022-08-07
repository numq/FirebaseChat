package com.numq.firebasechat.splash

import android.util.Log
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.ScaffoldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Star
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.numq.firebasechat.error.ShowError

@Composable
fun SplashScreen(
    scaffoldState: ScaffoldState,
    navigateToAuth: () -> Unit,
    navigateToHome: () -> Unit,
    vm: SplashViewModel = hiltViewModel()
) {

    var currentRotation by remember {
        mutableStateOf(0f)
    }

    val state by vm.state.collectAsState()

    LaunchedEffect(Unit) {
        Log.e("SPLASH", state.toString())
    }

    state.exception?.let {
        ShowError(scaffoldState, it, vm.cleanUpError)
    }

    state.authenticated?.let { authenticated ->
        if (authenticated) LaunchedEffect(Unit) { navigateToHome() }
        else LaunchedEffect(Unit) { navigateToAuth() }
    }

    val rotation = remember { Animatable(currentRotation) }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            Icons.Rounded.Star,
            "",
            modifier = Modifier
                .size(128.dp)
                .rotate(rotation.value),
            Color.Yellow
        )
    }

    LaunchedEffect(Unit) {
        rotation.animateTo(
            targetValue = currentRotation + 360f,
            animationSpec = infiniteRepeatable(
                animation = tween(3000, easing = LinearEasing),
                repeatMode = RepeatMode.Restart
            )
        ) {
            currentRotation = value
        }
    }

}