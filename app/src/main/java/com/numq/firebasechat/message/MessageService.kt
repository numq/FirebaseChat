package com.numq.firebasechat.message

import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MessageService @Inject constructor(
    firestore: FirebaseFirestore
) : MessageApi {

    companion object {
        const val MESSAGES = "messages"
    }

    private val collection = firestore.collection(MESSAGES)

    override fun getMessages(chatId: String, limit: Long) =
        collection.whereEqualTo("chatId", chatId).limit(limit).get()

    override fun getMessageById(id: String) = collection.document(id).get()

    override fun createMessage(chatId: String, userId: String, text: String) =
        with(collection.document()) {
            collection.document(id).set(
                Message(
                    id = id,
                    chatId = chatId,
                    senderId = userId,
                    text = text,
                    delivered = false,
                    read = false,
                    sentAt = System.currentTimeMillis(),
                    updatedAt = null
                )
            )
        }

    override fun updateMessage(id: String, text: String) =
        collection.document(id).update("text", text)

    override fun deleteMessage(id: String) = collection.document("id").delete()

}