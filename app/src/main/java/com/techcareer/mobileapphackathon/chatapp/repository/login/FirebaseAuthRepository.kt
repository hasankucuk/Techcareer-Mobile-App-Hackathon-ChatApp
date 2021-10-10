package com.techcareer.mobileapphackathon.chatapp.repository.login

import com.google.firebase.auth.FirebaseAuth
import com.techcareer.mobileapphackathon.common.base.BaseRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


/**
 * @author: Hasan Küçük on 9.10.2021
 */
interface FirebaseAuthRepository {

    fun signUp(): Flow<String>
    fun signIn()
}

class FirebaseAuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) : BaseRepository(), FirebaseAuthRepository {
    override fun signUp(): Flow<String> = request { "" }

    override fun signIn() {
    }


}