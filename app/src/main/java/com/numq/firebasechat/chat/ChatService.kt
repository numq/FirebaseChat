package com.numq.firebasechat.chat

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChatService @Inject constructor(
    firestore: FirebaseFirestore
) : ChatApi {

    companion object {
        const val CHATS = "chats"
    }

    private val collection = firestore.collection(CHATS)

    override fun getChats(userId: String, limit: Long) =
        collection.whereArrayContains("userIds", userId).limit(limit).get()

    override fun getChatById(id: String) = collection.document(id).get()

    override fun createChat(userId: String, anotherId: String) =
        with(collection.document()) {
            collection.document(id).set(
                Chat(
                    id = id,
                    name = anotherId,
                    userIds = listOf(userId, anotherId).sortedDescending(),
                    typingUserIds = null,
                    lastMessage = null,
                    updatedAt = System.currentTimeMillis()
                )
            )
            collection.document(id).get()
        }

    override fun updateChat(chat: Chat): Task<DocumentSnapshot> {
        collection.document(chat.id).set(chat)
        return collection.document(chat.id).get()
    }

    override fun deleteChat(id: String) = collection.document(id).delete()

}