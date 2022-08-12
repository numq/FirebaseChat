package com.numq.firebasechat.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.numq.firebasechat.message.GetMessages
import com.numq.firebasechat.message.Message
import com.numq.firebasechat.message.MessagePagingSource
import com.numq.firebasechat.message.SendMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ActiveChatViewModel @Inject constructor(
    private val getMessages: GetMessages,
    private val sendMessage: SendMessage
) : ViewModel() {

    private val _state = MutableStateFlow(ChatState())
    val state = _state.asStateFlow()

    private val defaultLimit = 30L

    fun createPagingSource(chatId: String) = Pager(PagingConfig(defaultLimit.toInt())) {
        val result = mutableListOf<Message>()
        MessagePagingSource(defaultLimit.toInt()) { offset, limit ->
            getMessages.invoke(Triple(chatId, offset.toLong(), limit.toLong())) { data ->
                data.fold(onError) { messages ->
                    viewModelScope.launch {
                        result.apply {
                            clear()
                            addAll(messages.toList())
                        }
                    }
                }
            }
            result
        }
    }.flow

    fun sendMessage(chatId: String, userId: String, text: String, onMessageSent: () -> Unit) =
        sendMessage.invoke(Triple(chatId, userId, text)) { data ->
            data.fold(onError) {
                if (it) onMessageSent()
            }
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