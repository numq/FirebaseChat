package com.numq.firebasechat.chat

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.numq.firebasechat.message.MessageChatItem
import com.numq.firebasechat.user.User

@Composable
fun ActiveChatScreen(
    currentUser: User,
    chat: Chat,
    maxWidth: Dp,
    vm: ActiveChatViewModel = hiltViewModel()
) {

    val state by vm.state.collectAsState()

    val messages = vm.createPagingSource(chat.id).collectAsLazyPagingItems()

    val (justOpened, setJustOpened) = remember {
        mutableStateOf(true)
    }

    val justOpenedAnimation: Dp by animateDpAsState(
        if (justOpened) maxWidth else 0.dp
    )

    val (chatVisible, setChatVisible) = remember {
        mutableStateOf(justOpened)
    }

    LaunchedEffect(Unit) {
        Log.e("CHAT", state.toString())
        setJustOpened(false)
    }

    BackHandler(!chatVisible) {
        setChatVisible(false)
    }

    val offset = 50.dp

    val collapseAnimation: Dp by animateDpAsState(
        if (chatVisible) 0.dp else (maxWidth - offset)
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
                            state = messagesState
                        ) {
                            items(messages) { message ->
                                if (message != null) {
                                    MessageChatItem(currentUser, message, maxWidth)
                                }
                            }
                            item {
                                LaunchedEffect(Unit) {
                                    messagesState.animateScrollToItem(messages.itemCount)
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
                                }, enabled = chatVisible,
                                modifier = Modifier.weight(1f),
                                trailingIcon = {
                                    IconButton(onClick = {
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