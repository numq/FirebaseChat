package com.numq.firebasechat.search

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.numq.firebasechat.mapper.user
import com.numq.firebasechat.user.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

interface SearchApi {

    fun getUsersByName(name: String, limit: Long): Flow<User>
    fun getUsersByEmail(email: String, limit: Long): Flow<User>

    class Implementation @Inject constructor(
        firestore: FirebaseFirestore
    ) : SearchApi {

        private val coroutineContext = Dispatchers.Default + Job()
        private val coroutineScope = CoroutineScope(coroutineContext)

        companion object {
            const val USERS = "users"
        }

        private val collection = firestore.collection(USERS)

        override fun getUsersByName(name: String, limit: Long) = callbackFlow {
            val subscription = collection.whereEqualTo("name", name)
                .orderBy("name", Query.Direction.DESCENDING)
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

        override fun getUsersByEmail(email: String, limit: Long) = callbackFlow {
            val subscription = collection.whereEqualTo("email", email)
                .orderBy("email", Query.Direction.DESCENDING)
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

    }

}