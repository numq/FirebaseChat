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

    private val coroutineContext = Dispatchers.Default + Job()
    private val coroutineScope = CoroutineScope(coroutineContext)

    companion object {
        const val MESSAGES = "messages"
    }

    private val collection = firestore.collection(MESSAGES)

    override fun getLatestMessages(chatId: String, limit: Long) = callbackFlow {
        val subscription = collection.whereEqualTo("chatId", chatId)
            .orderBy("sentAt", Query.Direction.DESCENDING)
            .limit(limit)
            .addSnapshotListener { value, error ->
                error?.let { close(error) }
                coroutineScope.launch {
                    value?.documents?.forEach() {
                        send(it)
                    }
                }
            }
        awaitClose {
            subscription.remove()
        }
    }

    override fun getMessages(chatId: String, lastMessageId: String, limit: Long) =
        collection.document(lastMessageId).get().onSuccessTask {
            collection.whereEqualTo("chatId", chatId)
                .orderBy("sentAt", Query.Direction.DESCENDING)
                .limit(limit)
                .startAfter(it)
                .get()
        }

    override fun createMessage(chatId: String, userId: String, text: String) =
        with(collection.document()) {
            collection.document(id).set(
                Message(
                    id = id,
                    chatId = chatId,
                    senderId = userId,
                    text = text,
                    delivered = true,
                    read = false,
                    sentAt = System.currentTimeMillis(),
                    updatedAt = null
                )
            ).onSuccessTask {
                collection.document(id).get()
            }
        }

    override fun readMessage(id: String) =
        collection.document(id).update("read", true).onSuccessTask {
            collection.document(id).get()
        }

    override fun deleteMessage(id: String) = collection.document("id").delete()

}