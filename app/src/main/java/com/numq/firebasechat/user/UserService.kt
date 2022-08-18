package com.numq.firebasechat.user

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserService @Inject constructor(
    firestore: FirebaseFirestore,
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
                        send(it)
                    }
                }
            }
        awaitClose {
            subscription.remove()
        }
    }

    override fun getUserById(id: String) = collection.document(id).get()

    override fun createUser(id: String, email: String) =
        collection.document(id).set(
            User(
                id = id,
                email = email,
                name = null,
                isOnline = true,
                lastSeenAt = System.currentTimeMillis()
            )
        )

    override fun updateUserActivity(id: String, state: Boolean) = collection.document(id).update(
        "isOnline",
        state,
        "lastSeenAt",
        System.currentTimeMillis()
    )

    override fun updateUser(user: User) = with(user) {
        collection.document(user.id).update(
            "name",
            name,
            "email",
            email
        )
    }

    override fun uploadImage(id: String, byteString: String) =
        storage.reference.child(id).putBytes(byteString.toByteArray()).addOnSuccessListener {
            collection.document(id).update(
                "imageUri", it.storage.downloadUrl
            )
        }.continueWithTask {
            collection.document(id).get()
        }

    override fun deleteUser(id: String) = collection.document(id).delete()

}