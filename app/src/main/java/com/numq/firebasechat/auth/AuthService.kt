package com.numq.firebasechat.auth

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthService @Inject constructor(
    private val auth: FirebaseAuth
) : AuthApi {

    private val coroutineContext = Dispatchers.Default + Job()
    private val coroutineScope = CoroutineScope(coroutineContext)

    override val authenticationId = callbackFlow {
        val listener = FirebaseAuth.AuthStateListener {
            coroutineScope.launch { it.currentUser?.uid?.let { id -> send(id) } }
        }
        auth.addAuthStateListener(listener)
        awaitClose {
            auth.removeAuthStateListener(listener)
        }
    }

    override fun signInByEmail(email: String, password: String) = flow {
        emit(AuthResult.Authenticating)
        auth.signInWithEmailAndPassword(email, password).addOnSuccessListener {
            coroutineScope.launch { emit(AuthResult.Success) }
        }.addOnFailureListener {
            coroutineScope.launch { emit(AuthResult.Failure) }
        }
    }

    override fun signUpByEmail(email: String, password: String, onSignUp: (FirebaseUser?) -> Unit) =
        flow {
            emit(AuthResult.Authenticating)
            auth.createUserWithEmailAndPassword(email, password).addOnSuccessListener { result ->
                coroutineScope.launch { emit(AuthResult.Success) }
                    .invokeOnCompletion { onSignUp(result.user) }
            }.addOnFailureListener {
                coroutineScope.launch { emit(AuthResult.Failure) }
            }
        }

    override fun signOut() = flow {
        emit(AuthResult.Authenticating)
        coroutineScope.launch { auth.signOut() }.invokeOnCompletion {
            if (auth.currentUser != null) {
                coroutineScope.launch { emit(AuthResult.Failure) }
            } else {
                coroutineScope.launch { emit(AuthResult.Success) }
            }
        }
    }

}