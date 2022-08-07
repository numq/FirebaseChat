package com.numq.firebasechat.permission

import androidx.compose.runtime.Composable
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import kotlinx.coroutines.runBlocking

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PermissionsRequired(permissions: List<String>, content: @Composable () -> Unit) {

    val permissionsState = rememberMultiplePermissionsState(permissions)

    if (permissionsState.allPermissionsGranted) {
        content()
    } else {
        PermissionsDenied(permissionsState.permissions) {
            runBlocking {
                permissionsState.launchMultiplePermissionRequest()
            }
        }
    }
}