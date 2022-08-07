package com.numq.firebasechat.error

import androidx.compose.material.ScaffoldState
import androidx.compose.material.SnackbarDuration
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun ShowError(
    scaffoldState: ScaffoldState,
    exception: Exception,
    cleanUpError: () -> Unit,
    coroutineScope: CoroutineScope = rememberCoroutineScope()
) {
    coroutineScope.launch {
        scaffoldState.snackbarHostState.showSnackbar(
            exception.localizedMessage ?: exception.javaClass.simpleName.toString(),
            duration = SnackbarDuration.Short
        )
    }.invokeOnCompletion {
        cleanUpError()
    }
}