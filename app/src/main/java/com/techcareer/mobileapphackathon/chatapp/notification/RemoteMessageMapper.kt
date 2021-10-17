package com.techcareer.mobileapphackathon.chatapp.notification

import com.techcareer.mobileapphackathon.chatapp.repository.home.VoiceMessage

object RemoteMessageMapper : Mapper<Map<String, String>, VoiceMessage> {

    override fun map(input: Map<String, String>): VoiceMessage {
        return VoiceMessage(
            id = input["id"],
            senderId = input["senderId"],
            receiverId = input["receiverId"],
            isOwner = input["isOwner"].let { if (it.isNullOrEmpty()) false else it.toBoolean() },
            name = input["name"],
            audioUrl = input["audioUrl"],
            audioFile = input["audioFile"],
            audioDuration = input["audioDuration"].let { if (it.isNullOrEmpty()) 0 else it.toLong() },
            timestamp = input["timestamp"],
            readTimestamp = input["readTimestamp"]
        )
    }
}