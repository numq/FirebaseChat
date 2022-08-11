package com.numq.firebasechat.message

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MessageService @Inject constructor(
    firestore: FirebaseFirestore
) : MessageApi {

    private val coroutineContext = Dispatchers.Main + Job()
    private val coroutineScope = CoroutineScope(coroutineContext)

    companion object {
        const val MESSAGES = "messages"
    }

    private val collection = firestore.collection(MESSAGES)

    override fun getMessages(chatId: String, limit: Long) = callbackFlow {
        val subscription = collection.whereEqualTo("chatId", chatId)
            .orderBy("sentAt", Query.Direction.DESCENDING)
            .limit(limit).addSnapshotListener { value, error ->
                error?.let { throw error }
                coroutineScope.launch {
                    value?.documents?.forEach {
                        send(it)
                    }
                }
            }
        awaitClose {
            subscription.remove()
        }
    }

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
            ).continueWithTask {
                collection.document(id).get()
            }
        }

    override fun updateMessage(id: String, text: String) =
        collection.document(id).update("text", text)

    override fun deleteMessage(id: String) = collection.document("id").delete()

}