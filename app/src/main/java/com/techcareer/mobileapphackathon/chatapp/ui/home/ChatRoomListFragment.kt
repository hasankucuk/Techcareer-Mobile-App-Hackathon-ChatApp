package com.techcareer.mobileapphackathon.chatapp.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.techcareer.mobileapphackathon.chatapp.R
import com.techcareer.mobileapphackathon.chatapp.databinding.FragmentChatRoomListBinding
import com.techcareer.mobileapphackathon.common.base.BaseFragment


class ChatRoomListFragment : BaseFragment<FragmentChatRoomListBinding>() {

    override fun getLayoutId(): Int = R.layout.fragment_chat_room_list

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        navigate(ChatRoomListFragmentDirections.actionChatRoomListFragmentToSignUpFragment())
        return super.onCreateView(inflater, container, savedInstanceState)
    }
}