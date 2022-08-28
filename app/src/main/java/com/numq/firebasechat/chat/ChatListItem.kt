package com.numq.firebasechat.chat

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Done
import androidx.compose.material.icons.rounded.Error
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.numq.firebasechat.message.MessageColors
import com.numq.firebasechat.user.User
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun ChatListItem(
    user: User,
    chat: Chat,
    maxWidth: Dp,
    onItemClick: (Chat) -> Unit
) {

    val isOutgoing = chat.lastMessage?.senderId == user.id
    val tint = if (isOutgoing) MessageColors.senderColor else MessageColors.receiverColor

    LaunchedEffect(Unit) {
        Log.e("chat", chat.toString())
    }

    Card(
        Modifier
            .width(maxWidth)
            .padding(8.dp)
            .clickable { onItemClick(chat) },
        backgroundColor = tint
    ) {
        Column(
            Modifier.padding(4.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(chat.name, color = Color.Black)
                Divider(Modifier.fillMaxWidth(), color = Color.Black)
            }
            chat.lastMessage?.also { message ->
                Log.e("lastMessage", message.toString())
                with(message) {
                    Column(
                        Modifier
                            .width(maxWidth)
                            .background(tint),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Box(
                            Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            Text(text, color = Color.Black, maxLines = 1)
                        }
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .padding(4.dp),
                            horizontalArrangement = Arrangement.End,
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
                                fontWeight = FontWeight.Thin,
                                color = Color.Black
                            )
                        }
                    }
                }
            } ?: Text("No messages...", color = Color.Black)
        }
    }
}