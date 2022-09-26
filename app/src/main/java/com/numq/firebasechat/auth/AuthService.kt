package com.numq.firebasechat.auth

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class AuthService @Inject constructor(
    private val auth: FirebaseAuth
) : AuthApi {

    private val coroutineContext = Dispatchers.Default + Job()
    private val coroutineScope = CoroutineScope(coroutineContext)

    override fun getAuthenticationState() = callbackFlow {
        coroutineScope.launch { send(AuthenticationState.Authenticating) }
        val listener = FirebaseAuth.AuthStateListener {
            try {
                it.currentUser?.let { user ->
                    coroutineScope.launch { send(AuthenticationState.Authenticated(user.uid)) }
                } ?: coroutineScope.launch { send(AuthenticationState.Unauthenticated) }
            } catch (e: Exception) {
                coroutineScope.launch { send(AuthenticationState.Failure(e)) }
                close(e)
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
                throw AuthException
            }
        } ?: throw AuthException
    }) {
        isSuccessful
    }

    override fun signOut() = auth.signOut().let { auth.currentUser == null }

}