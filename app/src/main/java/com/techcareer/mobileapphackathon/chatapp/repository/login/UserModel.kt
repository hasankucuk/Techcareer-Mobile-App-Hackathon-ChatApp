package com.techcareer.mobileapphackathon.chatapp.repository.login

import com.techcareer.mobileapphackathon.common.base.ListAdapterItem

data class UserModel(
    var email: String? = null,
    var displayName: String? = null,
    var uid: String? = null,
    var isOnline: Boolean? = null,
    var userNameForSearch: List<String>? = listOf(),
    override val id: Long = 0,
    var fcmToken: String? = null
) : ListAdapterItem {
    fun mapStringToList(name: String?): List<String> {
        val list = arrayListOf<String>()
        name?.let {
            for (i in 1..it.length) list.add(it.take(i))
        }
        return list
    }


}
