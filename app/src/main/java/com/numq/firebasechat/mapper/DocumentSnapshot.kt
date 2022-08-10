package com.numq.firebasechat.mapper

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.getField
import com.numq.firebasechat.chat.Chat
import com.numq.firebasechat.message.Message
import com.numq.firebasechat.user.User

val DocumentSnapshot.chat
    get(): Chat? {
        val id = getString("id")
        val name = getString("name")
        val userIds = (get("userIds") as? List<*>)?.mapNotNull { it as? String }
        val typingUserIds = (get("typingUserIds") as? List<*>)?.mapNotNull { it as? String }
        val lastMessage = getField("lastMessage") as? Message
        val updatedAt = getLong("updatedAt")
        return if (id != null && name != null && userIds != null && updatedAt != null) {
            Chat(
                id = id,
                name = name,
                userIds = userIds,
                typingUserIds = typingUserIds,
                lastMessage = lastMessage,
                updatedAt = updatedAt
            )
        } else null
    }

val DocumentSnapshot.message
    get(): Message? {
        val id = getString("id")
        val chatId = getString("chatId")
        val senderId = getString("senderId")
        val text = getString("text")
        val delivered = getBoolean("delivered")
        val read = getBoolean("read")
        val sentAt = getLong("sentAt")
        val updatedAt = getLong("updatedAt")
        return if (
            id != null &&
            chatId != null &&
            senderId != null &&
            text != null &&
            sentAt != null
        ) {
            Message(
                id = id,
                chatId = chatId,
                senderId = senderId,
                text = text,
                delivered = delivered ?: false,
                read = read ?: false,
                sentAt = sentAt,
                updatedAt = updatedAt
            )
        } else null
    }

val DocumentSnapshot.user
    get(): User? {
        val id = getString("id")
        val email = getString("email")
        val name = getString("name")
        val lastActiveChatId = getString("lastActiveChatId")
        val isOnline = getBoolean("online")
        val lastSeenAt = getLong("lastSeenAt")
        return if (id != null && email != null && isOnline != null && lastSeenAt != null) {
            User(
                id = id,
                email = email,
                name = name,
                lastActiveChatId = lastActiveChatId,
                isOnline = isOnline,
                lastSeenAt = lastSeenAt
            )
        } else null
    }