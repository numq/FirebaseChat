package com.numq.firebasechat.message

import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Done
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun MessageItem(message: Message) {
    Card {
        Column(Modifier.fillMaxWidth()) {
            with(message) {
                Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.BottomEnd) {
                    Text(text)
                }
                Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.BottomEnd) {
                    Icon(
                        Icons.Rounded.Done,
                        "delivered",
                        tint = if (delivered) Color.LightGray else Color.Blue
                    )
                    Icon(
                        Icons.Rounded.Done,
                        "read",
                        tint = if (read) Color.LightGray else Color.Blue
                    )
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(SimpleDateFormat.getInstance().format(Date(sentAt)))
                    }
                }
            }
        }
    }
}