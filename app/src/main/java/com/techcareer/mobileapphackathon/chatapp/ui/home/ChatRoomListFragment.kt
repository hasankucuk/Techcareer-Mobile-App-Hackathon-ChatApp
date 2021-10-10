package com.techcareer.mobileapphackathon.chatapp.ui.home

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
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

    override fun getMenuId(): Int = R.menu.menu_home

    private val chatRoomListViewModel: ChatRoomListViewModel by viewModels()

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        val searchItem = menu.findItem(R.id.search)
        val searchView = searchItem.actionView as SearchView
    }
//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        val inflater = menuInflater
//        inflater.inflate(R.menu.menu_home, menu)
//
//
//        return true
//    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.sign_out_menu -> {
                Log.i("SIGN_", "çıkış yapılıyor.")
                chatRoomListViewModel.signOut()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

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
                    is AuthenticationState.SignOut -> {
                        Log.i("SIGN_","sign out")
                        navigate(ChatRoomListFragmentDirections.actionChatRoomListFragmentToLoginFragment())
                    }
                    null -> {
                    }
                }
            }
        }
    }

}