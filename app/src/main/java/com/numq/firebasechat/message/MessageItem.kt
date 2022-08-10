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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun MessageItem(message: Message) {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(4.dp),
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.Center
    ) {
        with(message) {
            Card {
                Text(text)
            }
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
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
                Text(
                    SimpleDateFormat.getInstance().format(Date(sentAt)),
                    fontWeight = FontWeight.Thin
                )
            }
        }
    }
}