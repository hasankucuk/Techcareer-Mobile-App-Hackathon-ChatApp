package com.techcareer.mobileapphackathon.chatapp.repository.home

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.techcareer.mobileapphackathon.chatapp.repository.login.UserModel
import com.techcareer.mobileapphackathon.common.base.BaseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await
import java.util.*
import javax.inject.Inject


/**
 * @author: Hasan Küçük on 10.10.2021
 */
interface FirebaseDaoRepository {

    fun createUser(userModel: UserModel)

    suspend fun searchUser(text: String): Flow<List<UserModel>>
}

class FirebaseDaoRepositoryImpl @Inject constructor(
    private val firebaseFirestore: FirebaseFirestore,
    private val firebaseRemoteConfig: FirebaseRemoteConfig
) : BaseRepository(), FirebaseDaoRepository {

    private val dbRefUsers by lazy { firebaseFirestore.collection("users") }

    private val dbRefUserNames by lazy { firebaseFirestore.collection("userNames") }

    override fun createUser(userModel: UserModel) {
        val userNameLowercase = userModel.displayName?.lowercase(Locale.ROOT)

        userModel.userNameForSearch = userModel.mapStringToList(userNameLowercase)

        dbRefUsers.document(userModel.uid!!).set(userModel)
            .addOnSuccessListener {

            }
//        dbRefUserNames.document(userNameLowercase).set(userModel).addOnSuccessListener {
//            dbRefUsers.document()
//
//        }.addOnFailureListener { }
    }

    override suspend fun searchUser(text: String): Flow<List<UserModel>> = request {
        val querySnapshot =
            dbRefUsers.whereArrayContains("userNameForSearch", text).get().await()

        querySnapshot.toObjects(UserModel::class.java)
    }

}