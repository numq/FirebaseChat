package com.numq.firebasechat.mapper

import com.google.firebase.firestore.DocumentSnapshot
import com.numq.firebasechat.chat.Chat
import com.numq.firebasechat.message.Message
import com.numq.firebasechat.user.User

val DocumentSnapshot.chat
    get(): Chat? {
        val id = get("id") as? String
        val name = get("name") as? String
        val userIds = (get("userIds") as? List<*>)?.mapNotNull { it as? String }
        val typingUserIds = (get("typingUserIds") as? List<*>)?.mapNotNull { it as? String }
        val lastMessage = get("lastMessage") as? Message
        val updatedAt = get("updatedAt") as? Long
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
        val id = get("id") as? String
        val chatId = get("chatId") as? String
        val senderId = get("senderId") as? String
        val text = get("text") as? String
        val delivered = get("delivered") as? Boolean
        val read = get("read") as? Boolean
        val sentAt = get("sentAt") as? Long
        val updatedAt = get("updatedAt") as? Long
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
        val id = get("id") as? String
        val email = get("email") as? String
        val name = get("name") as? String
        val lastActiveChatId = get("lastActiveChatId") as? String
        val isOnline = get("online") as? Boolean
        val lastSeenAt = get("lastSeenAt") as? Long
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