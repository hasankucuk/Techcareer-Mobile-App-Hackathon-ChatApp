package com.techcareer.mobileapphackathon.chatapp.notification

interface Mapper<I, O> {
    fun map(input: I): O
}