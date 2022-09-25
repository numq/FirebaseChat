package com.numq.firebasechat.chat

import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.numq.firebasechat.mapper.chat
import com.numq.firebasechat.message.Message
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ChatService @Inject constructor(
    firestore: FirebaseFirestore
) : ChatApi {

    private val coroutineContext = Dispatchers.Default + Job()
    private val coroutineScope = CoroutineScope(coroutineContext)

    companion object {
        const val CHATS = "chats"
    }

    private val collection = firestore.collection(CHATS)

    override fun getChats(userId: String, lastChatId: String?, limit: Long) = callbackFlow {
        val subscription = collection.whereArrayContains("userIds", userId)
            .orderBy("updatedAt", Query.Direction.DESCENDING)
            .limit(limit)
            .apply {
                lastChatId?.let {
                    startAfter(collection.document(it).get().await())
                }
            }
            .addSnapshotListener { value, error ->
                error?.let { close(error) }
                coroutineScope.launch {
                    value?.documents?.forEach {
                        it.chat?.let { chat ->
                            send(chat)
                        }
                    }
                }
            }
        awaitClose {
            subscription.remove()
        }
    }

    override fun getChatById(id: String) = callbackFlow {
        val subscription = collection.document(id).addSnapshotListener { value, error ->
            error?.let { close(error) }
            coroutineScope.launch {
                value?.let {
                    it.chat?.let { chat ->
                        send(chat)
                    }
                }
            }
        }
        awaitClose {
            subscription.remove()
        }
    }

    override fun createChat(userId: String, anotherId: String): Task<Chat?> {
        val ids = arrayOf(userId, anotherId).sorted()
        val id = ids.joinToString("")
        collection.document(id).get().addOnCompleteListener {
            if (it.isSuccessful) {
                if (!it.result.exists()) collection.document(id).set(
                    Chat(
                        id = id,
                        name = anotherId,
                        userIds = ids,
                        typingUserIds = null,
                        lastMessage = null,
                        updatedAt = System.currentTimeMillis()
                    )
                )
            }
        }
        return collection.document(id).get().onSuccessTask {
            Tasks.forResult(it.chat)
        }
    }

    override fun updateLastMessage(chatId: String, message: Message) =
        collection.document(chatId).update("lastMessage", message).continueWithTask {
            collection.document(chatId).get().onSuccessTask {
                Tasks.forResult(it.chat)
            }
        }

    override fun updateChat(chat: Chat) =
        collection.document(chat.id).set(chat).continueWithTask {
            collection.document(chat.id).get().onSuccessTask {
                Tasks.forResult(it.chat)
            }
        }

    override fun deleteChat(id: String) = collection.document(id).delete()

}