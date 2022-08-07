package com.numq.firebasechat.chat

import android.util.Log
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Send
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.numq.firebasechat.message.MessageItem
import com.numq.firebasechat.user.User

@Composable
fun ActiveChatScreen(
    currentUser: User,
    chat: Chat,
    maxWidth: Dp,
    vm: ActiveChatViewModel = hiltViewModel()
) {

    val state by vm.state.collectAsState()

    LaunchedEffect(Unit) {
        Log.e("CHAT", state.toString())
    }

    val (isCollapsed, setIsCollapsed) = remember {
        mutableStateOf(false)
    }

    val offsetAnimation: Dp by animateDpAsState(
        if (isCollapsed) (maxWidth - 50.dp) else 0.dp
    )

    val (messageText, setMessageText) = remember {
        mutableStateOf("")
    }

    BoxWithConstraints {
        Card(
            modifier = Modifier
                .fillMaxSize()
                .clipToBounds()
                .absoluteOffset(x = offsetAnimation)
        ) {
            BoxWithConstraints(
                modifier = if (isCollapsed) Modifier.clickable {
                    setIsCollapsed(false)
                } else Modifier) {
                Scaffold(
                    topBar = {
                        TopAppBar(title = {
                            Text(chat.name)
                        }, navigationIcon = {
                            IconButton(onClick = {
                                if (!isCollapsed) {
                                    setIsCollapsed(true)
                                }
                            }) {
                                Icon(
                                    Icons.Rounded.ArrowBack,
                                    "collapse chat"
                                )
                            }
                        })
                    }) { paddingValues ->
                    Column(
                        Modifier
                            .fillMaxSize()
                            .padding(paddingValues),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Box(
                            modifier = Modifier.weight(1f),
                            contentAlignment = Alignment.BottomCenter
                        ) {
                            LazyColumn(Modifier.fillMaxWidth(), reverseLayout = true) {
                                items(state.messages) { message ->
                                    MessageItem(message)
                                }
                            }
                        }
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .padding(4.dp)
                        ) {
                            TextField(value = messageText, onValueChange = {
                                setMessageText(it)
                            }, Modifier.weight(1f))
                            IconButton(onClick = {
                                vm.sendMessage(chat.id, currentUser.id, messageText)
                            }) {
                                Icon(Icons.Rounded.Send, "send message")
                            }
                        }
                    }
                }
            }
        }
    }
}