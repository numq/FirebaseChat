package com.numq.firebasechat.user

import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserService @Inject constructor(
    firestore: FirebaseFirestore
) : UserApi {

    companion object {
        const val USERS = "users"
    }

    private val collection = firestore.collection(USERS)

    override fun getUsersByQuery(query: String, limit: Long) =
        collection.whereEqualTo("email", query).limit(limit).get()

    override fun getUserById(id: String) = collection.document(id).get()

    override fun createUser(id: String, email: String) =
        collection.document(id).set(
            User(
                id = id,
                email = email,
                name = null,
                lastActiveChatId = null,
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
            email,
            "lastActiveChatId",
            lastActiveChatId
        )
    }

    override fun deleteUser(id: String) = collection.document(id).delete()

}