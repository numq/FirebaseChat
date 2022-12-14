package com.numq.firebasechat.auth

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

interface AuthApi {

    fun getAuthenticationState(): Flow<AuthenticationState>
    fun signInByEmail(email: String, password: String): Boolean
    fun signUpByEmail(
        name: String,
        email: String,
        password: String,
        onSignUp: (String) -> Boolean
    ): Boolean

    fun signOut(): Boolean

    class Implementation @Inject constructor(
        private val auth: FirebaseAuth
    ) : AuthApi {

        private val coroutineContext = Dispatchers.Default + Job()
        private val coroutineScope = CoroutineScope(coroutineContext)

        override fun getAuthenticationState() = callbackFlow {
            coroutineScope.launch { send(AuthenticationState.Authenticating) }
            val listener = FirebaseAuth.AuthStateListener {
                coroutineScope.launch {
                    it.uid?.let { uid ->
                        send(AuthenticationState.Authenticated(uid))
                    } ?: send(AuthenticationState.Unauthenticated)
                }
            }
            auth.addAuthStateListener(listener)
            awaitClose {
                auth.removeAuthStateListener(listener)
            }
        }

        override fun signInByEmail(email: String, password: String) =
            with(auth.signInWithEmailAndPassword(email, password)) {
                isSuccessful
            }

        override fun signUpByEmail(
            name: String,
            email: String,
            password: String,
            onSignUp: (String) -> Boolean
        ) = with(auth.createUserWithEmailAndPassword(email, password).addOnSuccessListener {
            it.user?.let { user ->
                if (!onSignUp(user.uid)) {
                    user.delete()
                    it.credential?.let { credential -> user.reauthenticate(credential) }
                    throw AuthException.Default
                }
            } ?: throw AuthException.Default
        }) {
            isSuccessful
        }

        override fun signOut() = auth.signOut().let { auth.currentUser == null }

    }

}