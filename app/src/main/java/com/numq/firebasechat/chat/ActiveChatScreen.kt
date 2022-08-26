package com.numq.firebasechat.chat

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.Send
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.numq.firebasechat.list.isReachedTheEnd
import com.numq.firebasechat.message.MessageChatItem
import com.numq.firebasechat.user.User

@Composable
fun ActiveChatScreen(
    chatVisible: Boolean,
    setChatVisible: (Boolean) -> Unit,
    currentUser: User,
    chat: Chat,
    offset: Dp,
    maxWidth: Dp,
    vm: ActiveChatViewModel = hiltViewModel()
) {

    val state by vm.state.collectAsState()

    LaunchedEffect(Unit) {
        Log.e("CHAT", state.toString())
        vm.observeLastMessages(chat.id, 15L)
    }

    BackHandler(chatVisible) {
        setChatVisible(false)
    }

    val collapseAnimation: Dp by animateDpAsState(
        if (chatVisible) 0.dp else (maxWidth - offset)
    )

    val (messageText, setMessageText) = remember {
        mutableStateOf("")
    }

    val messagesState = rememberLazyListState()

    messagesState.isReachedTheEnd(3) {
        state.messages.lastOrNull()?.let {
            vm.loadMore(chat.id, it.id, 15L)
        }
    }

    BoxWithConstraints {
        Card(
            modifier = Modifier
                .fillMaxSize()
                .clipToBounds()
                .absoluteOffset(x = collapseAnimation)
        ) {
            BoxWithConstraints(modifier = if (chatVisible) Modifier else Modifier.clickable {
                setChatVisible(true)
            }) {
                Scaffold(
                    topBar = {
                        TopAppBar(title = {
                            Text(chat.name)
                        }, navigationIcon = {
                            IconButton(onClick = {
                                if (chatVisible) {
                                    setChatVisible(false)
                                }
                            }, enabled = chatVisible) {
                                Icon(
                                    Icons.Rounded.ArrowBack,
                                    "close chat"
                                )
                            }
                        })
                    }) { paddingValues ->
                    Column(
                        Modifier
                            .fillMaxSize()
                            .padding(paddingValues),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Bottom
                    ) {
                        LazyColumn(
                            Modifier.weight(1f),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Bottom,
                            state = messagesState,
                            reverseLayout = true
                        ) {
                            items(state.messages) { message ->
                                LaunchedEffect(!message.read && message.senderId != currentUser.id) {
                                    vm.readMessage(message.id)
                                }
                                MessageChatItem(currentUser, message, maxWidth)
                            }
                        }
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .padding(4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            TextField(
                                value = messageText, onValueChange = {
                                    setMessageText(it)
                                }, enabled = chatVisible,
                                modifier = Modifier.weight(1f),
                                trailingIcon = {
                                    if (messageText.isNotBlank()) IconButton(onClick = {
                                        setMessageText("")
                                    }) {
                                        Icon(Icons.Rounded.Clear, "clear input")
                                    }
                                }
                            )
                            IconButton(onClick = {
                                vm.sendMessage(chat.id, currentUser.id, messageText) {
                                    setMessageText("")
                                }
                            }, enabled = messageText.isNotEmpty() && chatVisible) {
                                Icon(Icons.Rounded.Send, "send message")
                            }
                        }
                    }
                }
            }
        }
    }
}