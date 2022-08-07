package com.numq.firebasechat.auth

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthService @Inject constructor(
    private val auth: FirebaseAuth
) : AuthApi {

    private val coroutineContext = Dispatchers.Default + Job()
    private val coroutineScope = CoroutineScope(coroutineContext)

    override val authenticationState = auth.currentUser != null

    override val authenticationId = callbackFlow {
        val listener = FirebaseAuth.AuthStateListener {
            coroutineScope.launch {
                send(it.currentUser?.uid)
            }
        }
        auth.addAuthStateListener(listener)
        awaitClose {
            auth.removeAuthStateListener(listener)
        }
    }

    override fun signInByEmail(email: String, password: String) =
        auth.signInWithEmailAndPassword(email, password)

    override fun signUpByEmail(email: String, password: String) =
        auth.createUserWithEmailAndPassword(email, password)

    override fun signOut() = auth.signOut()

}