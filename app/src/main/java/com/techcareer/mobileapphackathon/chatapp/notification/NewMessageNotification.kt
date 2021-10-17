package com.techcareer.mobileapphackathon.chatapp.notification

data class NewMessageNotification(
    val notificationId: Int,
//    val userString: String,
    val senderName: String?,
    val message: String?
)