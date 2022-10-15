package com.numq.firebasechat.chat

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.numq.firebasechat.divider.MessageDateDivider
import com.numq.firebasechat.error.ShowError
import com.numq.firebasechat.list.isReachedTheEnd
import com.numq.firebasechat.message.Message
import com.numq.firebasechat.message.MessageChatItem
import com.numq.firebasechat.user.User
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun ChatScreen(
    scaffoldState: ScaffoldState,
    userId: String,
    chatId: String,
    vm: ChatViewModel = hiltViewModel(),
    navigateUp: () -> Unit
) {

    BackHandler {
        navigateUp()
    }

    val state by vm.state.collectAsState()

    LaunchedEffect(Unit) {
        Log.e("CHAT", state.toString())
        vm.observeUser(userId)
        vm.observeChat(chatId)
        vm.observeLastMessages(chatId, 15L)
    }

    state.exception?.let {
        ShowError(scaffoldState, exception = it, cleanUpError = vm.cleanUpError)
    }

    state.user?.let { user ->
        state.chat?.let { chat ->
            BuildChat(
                user = user,
                chat = chat,
                messages = state.messages,
                sendMessage = vm::sendMessage,
                readMessage = vm::readMessage,
                loadMore = vm::loadMore,
                navigateUp = navigateUp
            )
        } ?: navigateUp()
    } ?: navigateUp()

}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BuildChat(
    user: User,
    chat: Chat,
    messages: List<Message>,
    sendMessage: (String, String, String, () -> Unit) -> Unit,
    readMessage: (String) -> Unit,
    loadMore: (String, String, Long) -> Unit,
    navigateUp: () -> Unit
) {
    val (messageText, setMessageText) = remember {
        mutableStateOf("")
    }

    val messagesState = rememberLazyListState()

    messagesState.isReachedTheEnd(3) {
        messages.lastOrNull()?.let { message ->
            loadMore(chat.id, message.id, 15L)
        }
    }
    Scaffold(
        topBar = {
            TopAppBar(title = {
                Text(chat.name)
            }, navigationIcon = {
                IconButton(onClick = {
                    navigateUp()
                }) {
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
                messages.groupBy {
                    SimpleDateFormat("dd/MM/YY").format(
                        Date(it.updatedAt)
                    )
                }.forEach { (_, list) ->
                    items(list) { message ->
                        SideEffect {
                            if (!message.read && message.senderId != user.id) {
                                readMessage(message.id)
                            }
                        }
                        MessageChatItem(user, message)
                    }
                    list.firstOrNull()?.also {
                        stickyHeader {
                            MessageDateDivider(it.updatedAt)
                        }
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
                    },
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
                    sendMessage(chat.id, user.id, messageText) {
                        setMessageText("")
                    }
                }, enabled = messageText.isNotEmpty()) {
                    Icon(Icons.Rounded.Send, "send message")
                }
            }
        }
    }
}