package com.numq.firebasechat.message

import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Done
import androidx.compose.material.icons.rounded.Error
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.numq.firebasechat.user.User
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun MessageChatItem(user: User, message: Message) {
    val isOutgoing = message.senderId == user.id
    val tint = if (isOutgoing) MessageColors.senderColor else MessageColors.receiverColor
    Column(
        Modifier
            .fillMaxWidth()
            .padding(4.dp),
        horizontalAlignment = if (isOutgoing) Alignment.End else Alignment.Start,
        verticalArrangement = Arrangement.Center
    ) {
        with(message) {
            Card(
                backgroundColor = tint,
                contentColor = Color.Black
            ) {
                Box(
                    Modifier
                        .fillMaxWidth(.7f)
                        .padding(8.dp),
                    contentAlignment = if (isOutgoing) Alignment.CenterEnd else Alignment.CenterStart
                ) {
                    Text(text)
                }
            }
            Row(
                Modifier.padding(4.dp),
                horizontalArrangement = if (isOutgoing) Arrangement.End else Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (delivered) {
                    Icon(
                        Icons.Rounded.Done,
                        "read",
                        tint = if (read) Color.Blue else Color.LightGray
                    )
                } else {
                    Icon(Icons.Rounded.Error, "not delivered", tint = Color.Red)
                }
                Spacer(Modifier.width(4.dp))
                Text(
                    SimpleDateFormat.getTimeInstance().format(Date(sentAt)),
                    fontWeight = FontWeight.Thin
                )
            }
        }
    }
}