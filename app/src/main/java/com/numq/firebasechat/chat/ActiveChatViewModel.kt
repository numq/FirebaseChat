package com.numq.firebasechat.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.numq.firebasechat.message.GetMessages
import com.numq.firebasechat.message.SendMessage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

class ActiveChatViewModel @Inject constructor(
    private val getMessages: GetMessages,
    private val sendMessage: SendMessage
) : ViewModel() {

    private val _state = MutableStateFlow(ChatState())
    val state = _state.asStateFlow()

    private val DEFAULT_LIMIT = 10L

    private fun observeMessages() = state.value.chat?.let { chat ->
        getMessages.invoke(Pair(chat.id, DEFAULT_LIMIT)) { data ->
            data.fold(onError) { messages ->
                viewModelScope.launch {
                    messages.collect { message ->
                        _state.update {
                            it.copy(messages = it.messages.plus(message))
                        }
                    }
                }
            }
        }
    } ?: _state.update {
        it.copy(messages = emptyList())
    }

    init {
        observeMessages()
    }

    fun sendMessage(chatId: String, userId: String, text: String) =
        sendMessage.invoke(Triple(chatId, userId, text)) { data ->
            data.fold(onError) {}
        }

    private val onError: (Exception) -> Unit = { exception ->
        _state.update {
            it.copy(exception = exception)
        }
    }

    val cleanUpError: () -> Unit = {
        _state.update {
            it.copy(exception = null)
        }
    }

}