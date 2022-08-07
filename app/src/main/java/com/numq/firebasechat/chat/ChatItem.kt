package com.numq.firebasechat.chat

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.numq.firebasechat.message.MessageItem

@Composable
fun ChatItem(chat: Chat, onItemClick: (Chat) -> Unit) {
    Card {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .clickable { onItemClick(chat) },
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(chat.name)
            Divider()
            chat.lastMessage?.let {
                MessageItem(message = it)
            } ?: Text("There's nothing here")
        }
    }

}