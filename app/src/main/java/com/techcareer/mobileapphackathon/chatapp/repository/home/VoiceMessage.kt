package com.techcareer.mobileapphackathon.chatapp.repository.home

import com.google.firebase.Timestamp
import com.google.firebase.firestore.Exclude

data class VoiceMessage(
    var id: String? = null,
    var senderId: String? = null,
    var receiverId: String? = null,
    var isOwner: Boolean? = null,
    var name: String? = null,
    var audioUrl: String? = null,
    var audioFile: String? = null,
    var audioDuration: Long? = null,
    var timestamp: Any? = null,
    var readTimestamp: Any? = null
) {

    @get:Exclude
    var audioDownloaded = false

    fun setMessageId() {
        val timestamp = this.timestamp
        if (timestamp is Timestamp) {
            id = senderId + "_" + timestamp.toDate().time
        }
    }
}