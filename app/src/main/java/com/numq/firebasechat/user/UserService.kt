package com.numq.firebasechat.user

import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import com.numq.firebasechat.auth.AuthException
import com.numq.firebasechat.mapper.user
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class UserService @Inject constructor(
    firestore: FirebaseFirestore,
    private val auth: FirebaseAuth,
    private val storage: FirebaseStorage
) : UserApi {

    private val coroutineContext = Dispatchers.Default + Job()
    private val coroutineScope = CoroutineScope(coroutineContext)

    companion object {
        const val USERS = "users"
    }

    private val collection = firestore.collection(USERS)

    override fun getUsersByQuery(query: String, limit: Long) = callbackFlow {
        val subscription = collection.whereEqualTo("email", query)
            .orderBy("lastSeenAt", Query.Direction.DESCENDING)
            .limit(limit).addSnapshotListener { value, error ->
                error?.let { close(error) }
                coroutineScope.launch {
                    value?.documents?.forEach {
                        it.user?.let { user ->
                            send(user)
                        }
                    }
                }
            }
        awaitClose {
            subscription.remove()
        }
    }

    override fun getUserById(id: String) = callbackFlow {
        val subscription = collection.document(id).addSnapshotListener { value, error ->
            error?.let { close(error) }
            coroutineScope.launch {
                value?.let {
                    it.user?.let { user ->
                        send(user)
                    }
                }
            }
        }
        awaitClose {
            subscription.remove()
        }
    }

    override fun createUser(id: String, name: String, email: String) =
        collection.document(id).set(
            User(
                id = id,
                email = email,
                name = name,
                isOnline = true,
                lastSeenAt = System.currentTimeMillis()
            )
        ).continueWithTask {
            collection.document(id).get().onSuccessTask {
                it.user?.let { user ->
                    Tasks.forResult(user)
                } ?: Tasks.forException(Exception(AuthException))
            }
        }

    override fun updateUserActivity(id: String, state: Boolean) = collection.document(id).update(
        "isOnline",
        state,
        "lastSeenAt",
        System.currentTimeMillis()
    )

    override fun uploadImage(id: String, bytes: ByteArray) =
        storage.reference.child(id).putBytes(bytes).addOnSuccessListener {
            it.storage.downloadUrl.addOnSuccessListener { uri ->
                collection.document(id).update("imageUri", uri.toString())
            }
        }.onSuccessTask {
            collection.document(id).get().onSuccessTask {
                Tasks.forResult(it.user)
            }
        }

    override fun updateName(id: String, name: String) = collection.document(id).update(
        "name",
        name
    ).onSuccessTask {
        collection.document(id).get().onSuccessTask {
            Tasks.forResult(it.user)
        }
    }

    override fun updateEmail(id: String, email: String) = collection.document(id).update(
        "email",
        email
    ).onSuccessTask {
        collection.document(id).get().onSuccessTask {
            Tasks.forResult(it.user)
        }
    }

    override fun changePassword(id: String, password: String) =
        auth.currentUser?.updatePassword(password)?.onSuccessTask {
            collection.document(id).get().onSuccessTask {
                Tasks.forResult(it.user)
            }
        }

    override fun deleteUser(id: String) =
        collection.document(id).delete().onSuccessTask { Tasks.forResult(id) }

}