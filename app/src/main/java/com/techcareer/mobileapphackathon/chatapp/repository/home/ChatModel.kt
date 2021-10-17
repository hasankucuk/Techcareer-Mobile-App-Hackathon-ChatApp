package com.techcareer.mobileapphackathon.chatapp.repository.home

import com.techcareer.mobileapphackathon.chatapp.repository.login.UserModel

/**
 * @author: Hasan Küçük on 11.10.2021
 */
data class ChatModel(
    val chatId: String? = null,
    val senderId: String? = null,
    val receiverId: String? = null,
    val sender: UserModel? = null,
    val receiver: UserModel? = null
)
