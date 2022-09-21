package com.numq.firebasechat.network

import android.util.Log
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
import kotlinx.coroutines.launch

@Composable
fun NetworkStatusNotification(status: NetworkStatus, onShown: () -> Unit = {}) {

    val (isShown, setIsShown) = remember {
        mutableStateOf(true)
    }

    LaunchedEffect(status) {
        if (status is NetworkStatus.Available) {
            launch {
                setIsShown(false)
                delay(3000)
            }.invokeOnCompletion {
                setIsShown(true)
                onShown()
            }
        }
    }

    LaunchedEffect(status) {
        Log.e(javaClass.simpleName, status.toString())
    }

    Box(Modifier.fillMaxWidth()) {
        AnimatedVisibility(
            status is NetworkStatus.Available && !isShown,
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