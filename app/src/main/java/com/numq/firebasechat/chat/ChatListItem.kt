package com.numq.firebasechat.chat

import android.util.Log
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.numq.firebasechat.message.MessageItem

@Composable
fun ChatListItem(chat: Chat, maxWidth: Dp, onItemClick: (Chat) -> Unit) {
    LaunchedEffect(Unit) {
        Log.e("chat", chat.toString())
    }
    Card {
        Column(
            Modifier
                .width(maxWidth)
                .padding(8.dp)
                .clickable { onItemClick(chat) },
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Box(Modifier.fillMaxWidth()) {
                Text(chat.name)
            }
            chat.lastMessage?.let { message ->
                Spacer(Modifier.height(8.dp))
                MessageItem(message)
            } ?: Text("There's nothing here")
        }
    }

}