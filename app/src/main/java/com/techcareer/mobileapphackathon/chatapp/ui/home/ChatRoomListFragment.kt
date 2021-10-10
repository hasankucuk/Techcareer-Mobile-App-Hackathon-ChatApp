package com.techcareer.mobileapphackathon.chatapp.ui.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.techcareer.mobileapphackathon.chatapp.R
import com.techcareer.mobileapphackathon.chatapp.databinding.FragmentChatRoomListBinding
import com.techcareer.mobileapphackathon.common.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ChatRoomListFragment : BaseFragment<FragmentChatRoomListBinding>() {

    override fun getLayoutId(): Int = R.layout.fragment_chat_room_list

    private val chatRoomListViewModel: ChatRoomListViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {
            chatRoomListViewModel.authenticationState.collect {
                when (it) {
                    is AuthenticationState.Authenticated -> {
                    }
                    AuthenticationState.Unauthenticated -> {
                        navigate(ChatRoomListFragmentDirections.actionChatRoomListFragmentToSignUpFragment())
                    }
                    null -> {
                    }
                }
            }
        }
    }

}