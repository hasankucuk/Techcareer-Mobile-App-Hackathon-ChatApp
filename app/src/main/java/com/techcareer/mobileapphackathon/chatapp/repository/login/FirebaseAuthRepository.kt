package com.techcareer.mobileapphackathon.chatapp.repository.login

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.techcareer.mobileapphackathon.common.base.BaseRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


/**
 * @author: Hasan Küçük on 9.10.2021
 */
interface FirebaseAuthRepository {

    fun signUp(email: String, password: String): Flow<Task<AuthResult>>
    fun signIn(email: String, password: String): Flow<Task<AuthResult>>
    fun getCurrentUser(): Flow<FirebaseUser?>
    fun signOut()
}

class FirebaseAuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) : BaseRepository(), FirebaseAuthRepository {

    override fun signUp(email: String, password: String): Flow<Task<AuthResult>> = request {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
    }

    override fun signIn(email: String, password: String) = request {
        firebaseAuth.signInWithEmailAndPassword(email, password)
    }

    override fun signOut() {
        firebaseAuth.signOut()
    }

    override fun getCurrentUser(): Flow<FirebaseUser?> = request { firebaseAuth.currentUser }


}