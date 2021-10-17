package com.techcareer.mobileapphackathon.chatapp.util

sealed class VoiceEffectType {
    object NORMAL: VoiceEffectType()
    object ROBOTIC : VoiceEffectType()
    object REVERSE : VoiceEffectType()
    object CAVE : VoiceEffectType()
    object VIBRATION : VoiceEffectType()
}
