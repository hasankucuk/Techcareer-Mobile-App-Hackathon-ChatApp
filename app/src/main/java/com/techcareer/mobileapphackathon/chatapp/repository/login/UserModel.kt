package com.techcareer.mobileapphackathon.chatapp.repository.login

data class UserModel(
    var email: String? = null,
    var displayName: String? = null,
    var uid: String? = null,
    var isOnline: Boolean? = null,
    var userNameForSearch: List<String>? = listOf()
) {
    fun mapStringToList(name:String?): List<String> {
        val list = arrayListOf<String>()
        name?.let {
            for (i in 1..it.length) list.add(it.take(i))
        }
        return list
    }

}
