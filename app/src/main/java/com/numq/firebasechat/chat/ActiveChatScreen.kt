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

    val (justOpened, setJustOpened) = remember {
        mutableStateOf(true)
    }

    val justOpenedAnimation: Dp by animateDpAsState(
        if (justOpened) maxWidth else 0.dp
    )

    LaunchedEffect(Unit) {
        Log.e("CHAT", state.toString())
        setJustOpened(false)
    }

    LaunchedEffect(chat.id) {
        vm.observeMessages(chat.id)
    }

    val (isCollapsed, setIsCollapsed) = remember {
        mutableStateOf(justOpened)
    }

    BackHandler(!isCollapsed) {
        setIsCollapsed(true)
    }

    val collapseAnimation: Dp by animateDpAsState(
        if (isCollapsed) (maxWidth - 50.dp) else 0.dp
    )

    val (messageText, setMessageText) = remember {
        mutableStateOf("")
    }

    val messagesState = rememberLazyListState()

    BoxWithConstraints {
        Card(
            modifier = Modifier
                .fillMaxSize()
                .clipToBounds()
                .absoluteOffset(x = if (justOpened) justOpenedAnimation else collapseAnimation)
        ) {
            BoxWithConstraints(modifier = if (isCollapsed) Modifier.clickable {
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
                            }, enabled = !isCollapsed) {
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
                        verticalArrangement = Arrangement.Bottom
                    ) {
                        LazyColumn(
                            Modifier.weight(1f),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Bottom,
                            state = messagesState
                        ) {
                            items(state.messages) { message ->
                                MessageItem(message)
                            }
                            item {
                                LaunchedEffect(Unit) {
                                    messagesState.animateScrollToItem(state.messages.size)
                                }
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
                                }, enabled = !isCollapsed,
                                modifier = Modifier.weight(1f)
                            )
                            IconButton(onClick = {
                                vm.sendMessage(chat.id, currentUser.id, messageText) {
                                    setMessageText("")
                                }
                            }, enabled = messageText.isNotEmpty() && !isCollapsed) {
                                Icon(Icons.Rounded.Send, "send message")
                            }
                        }
                    }
                }
            }
        }
    }
}