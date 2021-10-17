package com.techcareer.mobileapphackathon.chatapp.repository.home

import android.net.Uri
import android.util.Log
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.storage.FirebaseStorage
import com.techcareer.mobileapphackathon.chatapp.repository.login.UserModel
import com.techcareer.mobileapphackathon.common.base.BaseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.io.File
import java.util.*
import javax.inject.Inject


/**
 * @author: Hasan Küçük on 10.10.2021
 */
interface FirebaseDaoRepository {

    fun createUser(userModel: UserModel, callBack: (createStatus: Boolean) -> Unit)
    suspend fun isUserNameAvailable(userName: String, callBack: (usernameStatus: Boolean) -> Unit)
    fun getChatRoom(roomId: String, sender: ChatModel, receiver: ChatModel)
    fun sendVoiceMessage(
        audioPath: String,
        audioDuration: Long,
        chatModel: ChatModel
    )

    suspend fun searchUser(text: String): Flow<List<UserModel>>

    @ExperimentalCoroutinesApi
    fun getChatRoomMessageList(chatModel: ChatModel): Flow<ArrayList<VoiceMessage>>

    @ExperimentalCoroutinesApi
    fun getChatRoomList(userId: String): Flow<ArrayList<ChatModel>?>
}

class FirebaseDaoRepositoryImpl @Inject constructor(
    private val firebaseFirestore: FirebaseFirestore,
    private val firebaseRemoteConfig: FirebaseRemoteConfig,
    private val firebaseStorage: FirebaseStorage
) : BaseRepository(), FirebaseDaoRepository {

    private val dbRefUsernames by lazy { firebaseFirestore.collection("userNamesList") }

    private val dbRefUsers by lazy { firebaseFirestore.collection("users") }

    private val dbRefUserNames by lazy { firebaseFirestore.collection("userNames") }

    private val storageRefRecords by lazy { firebaseStorage.reference.child("chatRecords") }

    private val dbRefChatRooms by lazy { firebaseFirestore.collection("chatRooms") }


    override suspend fun isUserNameAvailable(
        userName: String,
        callBack: (usernameStatus: Boolean) -> Unit
    ) {
        val userNameLowercase = userName.lowercase(Locale.ROOT)

        GlobalScope.launch(Dispatchers.IO) {
            val document = dbRefUsernames.document(userNameLowercase).get().await()
            if (document.exists()) {
                callBack(false)
            } else {
                callBack(true)
            }
        }
    }

    override fun createUser(
        userModel: UserModel,
        callBack: (createStatus: Boolean) -> Unit
    ) {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("TOKEN_", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }
            val token = task.result

            val userNameLowercase = userModel.displayName?.lowercase(Locale.ROOT) ?: ""
            userModel.userNameForSearch = userModel.mapStringToList(userNameLowercase)
            userModel.fcmToken = token


            dbRefUsernames.document(userNameLowercase).set(userModel)
                .addOnSuccessListener {
                    //username yoksa ekle
                    dbRefUsers.document(userModel.uid!!).set(userModel)
                        .addOnSuccessListener {
                            callBack(true)
                        }
                }
                .addOnFailureListener {
                    callBack(false)
                }
        })
    }

    override suspend fun searchUser(text: String): Flow<List<UserModel>> = request {
        val querySnapshot =
            dbRefUsers.whereArrayContains("userNameForSearch", text).get().await()

        querySnapshot.toObjects(UserModel::class.java)
    }

    override fun getChatRoom(roomId: String, sender: ChatModel, receiver: ChatModel) {
        dbRefUsers.document(sender.senderId!!).collection("chatRooms").document(roomId)
            .get()
            .addOnSuccessListener { documentSnapshot ->
                if (!documentSnapshot.exists()) {
                    addSenderChatRoom(sender)
                }
            }
            .addOnFailureListener {
            }

        dbRefUsers.document(receiver.receiverId!!).collection("chatRooms")
            .document(roomId).get()
            .addOnSuccessListener { documentSnapshot ->
                if (!documentSnapshot.exists()) {

                    addReceiverChatRoom(receiver)
                }
            }
            .addOnFailureListener {
            }
    }

    private fun addSenderChatRoom(chatModel: ChatModel) {
        chatModel.chatId?.let {
            dbRefUsers.document(chatModel.senderId!!).collection("chatRooms").document(it)
                .set(chatModel)
                .addOnSuccessListener {
                }
                .addOnFailureListener {
                }
        }
    }

    private fun addReceiverChatRoom(receiver: ChatModel) {
        receiver.chatId?.let {
            dbRefUsers.document(receiver.senderId!!).collection("chatRooms").document(it)
                .set(receiver)
                .addOnSuccessListener {
                }
                .addOnFailureListener {
                }
        }
    }

    override fun sendVoiceMessage(
        audioPath: String,
        audioDuration: Long,
        chatModel: ChatModel
    ) {
        val voiceMessageUri = Uri.fromFile(File(audioPath))
        //send voice storage
        storageRefRecords.child(voiceMessageUri.lastPathSegment!!).putFile(voiceMessageUri)
            .addOnSuccessListener { taskSnapshot ->
                val urlTask = taskSnapshot.storage.downloadUrl
                urlTask.addOnSuccessListener { uri ->
                    sendVoiceMessageForChatRoom(
                        audioUrl = uri.toString(),
                        audioFile = audioPath,
                        audioDuration = audioDuration,
                        chatModel = chatModel
                    )


                }.addOnFailureListener {
                }
            }.addOnFailureListener { }
    }


    fun sendVoiceMessageForChatRoom(
        audioUrl: String? = null,
        audioFile: String? = null,
        audioDuration: Long? = null,
        chatModel: ChatModel
    ) {
        chatModel.chatId?.let {
            val friendlyMessage = VoiceMessage(
                senderId = chatModel.senderId,
                receiverId = chatModel.receiverId,
//                name = user?.username,
                audioUrl = audioUrl,
                audioFile = audioFile,
                audioDuration = audioDuration,
                timestamp = FieldValue.serverTimestamp()
            )
            dbRefChatRooms.document(chatModel.chatId).collection("chatRoom").document()
                .set(friendlyMessage)
                .addOnSuccessListener {
//                isNewSenderChatRoom?.let { if (it) addSenderChatRoom() }
//                isNewReceiverChatRoom?.let { if (it) addReceiverChatRoom() }
                }
                .addOnFailureListener {
                }

        }

    }


    private fun getChatRoomRef(chatRoomId: String): Query {
        return dbRefChatRooms.document(chatRoomId).collection("chatRoom")
            .orderBy("timestamp", Query.Direction.ASCENDING)
    }

    private fun getChatRoomListRef(userId: String): Query =
        dbRefUsers.document(userId).collection("chatRooms")

    @ExperimentalCoroutinesApi
    fun getChatRoomMessageListx(chatModel: ChatModel): Flow<ArrayList<VoiceMessage>> =
        request {
            val voiceMessageList = arrayListOf<VoiceMessage>()

            chatModel.chatId?.let {
                getChatRoomRef(it).addSnapshotListener { value, error ->

                    value?.let {
                        for (documentSnapshot in value.documents) {
                            val msg =
                                documentSnapshot.toObject(VoiceMessage::class.java) ?: continue
                            msg.setMessageId()
                            msg.isOwner = msg.senderId!! == chatModel.senderId
                            voiceMessageList.add(msg)
                            if (documentSnapshot.metadata.isFromCache) continue
                            if (documentSnapshot.metadata.hasPendingWrites()) continue
                            if (msg.readTimestamp != null) continue
                            if (msg.isOwner!!.not()) {
                                val map = mutableMapOf<String, Any>()
                                map["readTimestamp"] = FieldValue.serverTimestamp()
                                documentSnapshot.reference.update(map)
                            }
                        }

                    }
                }
            }
            voiceMessageList
        }

    @ExperimentalCoroutinesApi
    override fun getChatRoomMessageList(chatModel: ChatModel): Flow<ArrayList<VoiceMessage>> {
        return callbackFlow {

            chatModel.chatId?.let {
                val xx = getChatRoomRef(it).addSnapshotListener { value, error ->
                    val voiceMessageList = arrayListOf<VoiceMessage>()

                    value?.let {
                        for (documentSnapshot in value.documents) {
                            val msg =
                                documentSnapshot.toObject(VoiceMessage::class.java) ?: continue
                            msg.setMessageId()
                            msg.isOwner = msg.senderId!! == chatModel.senderId
                            voiceMessageList.add(msg)
                            if (documentSnapshot.metadata.isFromCache) continue
                            if (documentSnapshot.metadata.hasPendingWrites()) continue
                            if (msg.readTimestamp != null) continue
                            if (msg.isOwner!!.not()) {
                                val map = mutableMapOf<String, Any>()
                                map["readTimestamp"] = FieldValue.serverTimestamp()
                                documentSnapshot.reference.update(map)
                            }
                        }
                    }
                    trySend(voiceMessageList)
                }
                awaitClose {
                    xx.remove()
                }
            }
        }
    }


    @ExperimentalCoroutinesApi
    override fun getChatRoomList(userId: String): Flow<ArrayList<ChatModel>> {
        return callbackFlow {
            val xx = getChatRoomListRef(userId).addSnapshotListener { value, error ->
                val chatRoomList = arrayListOf<ChatModel>()
                value?.let {
                    chatRoomList.addAll(it.toObjects(ChatModel::class.java))
                }
                trySend(chatRoomList)
            }
            awaitClose {
                xx.remove()
            }

        }
    }

}