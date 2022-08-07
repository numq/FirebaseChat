package com.numq.firebasechat.permission

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Done
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PermissionsDenied(permissions: List<PermissionState>, grant: () -> Unit) {
    Box(
        Modifier
            .fillMaxSize()
            .padding(8.dp), contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                "You must grant required permissions",
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(16.dp))
            Divider()
            Spacer(Modifier.height(32.dp))
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                permissions.forEach {
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            if (it.status.isGranted) Icons.Rounded.Done else Icons.Rounded.Close,
                            "",
                            tint = if (it.status.isGranted) Color.Green else Color.Red
                        )
                        Text(
                            it.permission.split(".").last(),
                            color = if (it.status.isGranted) Color.Green else Color.Red
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
            Spacer(Modifier.height(64.dp))
            Button(onClick = { grant() }) {
                Text("I agree")
            }
        }
    }
}