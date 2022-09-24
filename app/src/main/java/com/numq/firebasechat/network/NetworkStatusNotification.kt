package com.numq.firebasechat.network

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.delay

@Composable
fun NetworkStatusNotification(status: NetworkStatus) {

    val (previousStatus, setPreviousStatus) = remember {
        mutableStateOf<NetworkStatus?>(null)
    }

    val connectionRestored =
        previousStatus is NetworkStatus.Unavailable && status is NetworkStatus.Available

    LaunchedEffect(status) {
        if (connectionRestored) delay(3000)
        setPreviousStatus(status)
    }

    Box(Modifier.fillMaxWidth()) {
        AnimatedVisibility(
            connectionRestored,
            enter = slideInVertically(),
            exit = slideOutVertically()
        ) {
            Box(
                Modifier
                    .fillMaxWidth()
                    .background(Color(0, 150, 100)),
                contentAlignment = Alignment.Center
            ) {
                Text("Back online!")
            }
        }
        AnimatedVisibility(
            status is NetworkStatus.Unavailable,
            enter = slideInVertically(),
            exit = slideOutVertically()
        ) {
            Box(
                Modifier
                    .fillMaxWidth()
                    .background(Color(200, 0, 50)),
                contentAlignment = Alignment.Center
            ) {
                Text("No internet connection.")
            }
        }
    }
}