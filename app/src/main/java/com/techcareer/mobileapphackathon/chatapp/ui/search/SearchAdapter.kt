package com.techcareer.mobileapphackathon.chatapp.ui.search

import com.techcareer.mobileapphackathon.chatapp.R
import com.techcareer.mobileapphackathon.chatapp.databinding.ItemSearchBinding
import com.techcareer.mobileapphackathon.chatapp.repository.login.UserModel
import com.techcareer.mobileapphackathon.common.base.BaseAdapter

/**
 * @author: Hasan Küçük on 10.10.2021
 */

class SearchAdapter(private val list: List<UserModel>, private val searchListener: SearchListener) :
    BaseAdapter<ItemSearchBinding, UserModel>(list) {

    override val layoutId: Int = R.layout.item_search

    override fun bind(binding: ItemSearchBinding, user: UserModel) {
        binding.apply {
            item = user
            clickListener = searchListener
            executePendingBindings()
        }
    }
}

interface SearchListener {
    fun onUserClicked(user: UserModel)
}