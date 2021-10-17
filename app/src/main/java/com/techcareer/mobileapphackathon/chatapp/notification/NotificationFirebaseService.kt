package com.techcareer.mobileapphackathon.chatapp.notification

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.techcareer.mobileapphackathon.chatapp.App
import com.techcareer.mobileapphackathon.chatapp.repository.home.VoiceMessage
import com.techcareer.mobileapphackathon.chatapp.repository.login.UserModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

/**
 * @author: Hasan Küçük on 13.10.2021
 */
class NotificationFirebaseService : FirebaseMessagingService() {

    private val firebaseFirestore by lazy { FirebaseFirestore.getInstance() }
    private val dbRefUsers by lazy { firebaseFirestore.collection("users") }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        remoteMessage.data.isNotEmpty().let {
            val notificationVoiceMessage = RemoteMessageMapper.map(remoteMessage.data)
            if (notificationVoiceMessage.senderId == App.lastReceiverId) return
            GlobalScope.launch(Dispatchers.IO) { createNotifications(notificationVoiceMessage) }
        }
    }

    override fun onNewToken(newToken: String) {
        super.onNewToken(newToken)

    }

    private suspend fun createNotifications(notificationVoiceMessage: VoiceMessage) {
        val senderId = notificationVoiceMessage.senderId ?: return

        val documentSnapshot = dbRefUsers.document(senderId).get().await()
        val user = documentSnapshot.toObject(UserModel::class.java) ?: return

        val newMessageNotification = NewMessageNotification(
            notificationId = senderId.hashCode(),
            senderName = user.displayName,
            message =  "\uD83C\uDFA4 Sesli bir mesajınız var."
        )
        NotificationUtils.makeStatusNotification(applicationContext, newMessageNotification)
    }


}