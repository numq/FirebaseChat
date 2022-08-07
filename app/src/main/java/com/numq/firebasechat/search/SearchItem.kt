package com.numq.firebasechat.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.numq.firebasechat.user.User
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun SearchItem(user: User, onItemClick: (User) -> Unit) {
    Card {
        Column(
            Modifier
                .fillMaxWidth()
                .clickable { onItemClick(user) },
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Box(
                Modifier
                    .fillMaxWidth()
                    .padding(4.dp), contentAlignment = Alignment.CenterStart
            ) {
                Text(text = user.name ?: user.email)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Box(
                Modifier
                    .fillMaxWidth()
                    .padding(4.dp), contentAlignment = Alignment.CenterEnd
            ) {
                Text(text = SimpleDateFormat.getInstance().format(Date(user.lastSeenAt)))
            }
        }
    }
}