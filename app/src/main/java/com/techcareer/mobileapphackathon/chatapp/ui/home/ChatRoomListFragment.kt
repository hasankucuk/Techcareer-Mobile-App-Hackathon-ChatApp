package com.techcareer.mobileapphackathon.chatapp.ui.home

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.techcareer.mobileapphackathon.chatapp.R
import com.techcareer.mobileapphackathon.chatapp.databinding.FragmentChatRoomListBinding
import com.techcareer.mobileapphackathon.chatapp.ui.chat.ChatViewModel
import com.techcareer.mobileapphackathon.chatapp.ui.search.SearchNavigateState
import com.techcareer.mobileapphackathon.chatapp.ui.search.SearchViewModel
import com.techcareer.mobileapphackathon.chatapp.util.binding.updateList
import com.techcareer.mobileapphackathon.common.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ChatRoomListFragment : BaseFragment<FragmentChatRoomListBinding>() {

    override fun getLayoutId(): Int = R.layout.fragment_chat_room_list

    override fun getMenuId(): Int = R.menu.menu_home

    private val chatRoomListViewModel: ChatRoomListViewModel by viewModels()
    private val searchViewModel: SearchViewModel by activityViewModels()
    private val chatViewModel: ChatViewModel by activityViewModels()

    lateinit var chatRoomListAdapter: ChatRoomListAdapter

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        val searchItem = menu.findItem(R.id.search)
        (searchItem.actionView as SearchView).apply {
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean = true

                override fun onQueryTextChange(newText: String?): Boolean {
                    newText?.let {
                        chatRoomListViewModel.searchUser(it)
                    }
                    return true
                }
            })
        }
    }

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

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        chatRoomListAdapter = ChatRoomListAdapter(ChatRoomListAdapter.OnClickListener { chatModel ->
            chatViewModel.sendChatModel(chatModel)
            navigate(ChatRoomListFragmentDirections.actionChatRoomListFragmentToChatFragment())

        })
        binding.viewModel= chatRoomListViewModel
        binding.chatRoomListRecycler.adapter = chatRoomListAdapter

        lifecycleScope.launch {
            chatRoomListViewModel.authenticationState.collect { it ->
                when (it) {
                    is AuthenticationState.Authenticated -> {

                        lifecycleScope.launch {
                            chatRoomListViewModel.getChatRoomList().collect {
                                if (it.isNullOrEmpty() ) {
                                    chatRoomListViewModel.onScreenState(ChatRoomViewState.ChatRoomListEmpty)
                                }else{
                                    chatRoomListViewModel.onScreenState(ChatRoomViewState.DefaultState)
                                }

                                it?.let {
                                    chatRoomListAdapter.updateList(it)
                                }
                            }
                        }

                        lifecycleScope.launch {
                            chatRoomListViewModel.userList.collect {
                                it?.let {
                                    searchViewModel.sendUserList(it)
                                }
                            }
                        }

                        lifecycleScope.launch {
                            searchViewModel.searchNavigateState.collect {
                                when (it) {
                                    is SearchNavigateState.NavigateChatRoom -> {
                                        chatViewModel.sendChatModel(it.chatModel)
                                        searchViewModel.changeSearchNavigateState(
                                            SearchNavigateState.DefaultState
                                        )
                                        navigate(ChatRoomListFragmentDirections.actionChatRoomListFragmentToChatFragment())
                                    }
                                    SearchNavigateState.DefaultState -> {
                                    }
                                    null -> {
                                    }
                                }
                            }
                        }
                    }
                    AuthenticationState.Unauthenticated -> {
                        navigate(ChatRoomListFragmentDirections.actionChatRoomListFragmentToSignUpFragment())
                    }
                    is AuthenticationState.SignOut -> {
                        Log.i("SIGN_", "sign out")
                        navigate(ChatRoomListFragmentDirections.actionChatRoomListFragmentToLoginFragment())
                    }
                    null -> {
                    }
                }
            }
        }


    }


    override fun onStart() {
        super.onStart()
        requireActivity()?.let {
            it.title = "Sohbet Listesi"
        }
    }

}