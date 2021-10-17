package com.techcareer.mobileapphackathon.chatapp.ui.search

import android.util.Log
import com.techcareer.mobileapphackathon.chatapp.repository.home.ChatModel
import com.techcareer.mobileapphackathon.chatapp.repository.home.FirebaseDaoRepository
import com.techcareer.mobileapphackathon.chatapp.repository.login.FirebaseAuthRepository
import com.techcareer.mobileapphackathon.chatapp.repository.login.UserModel
import com.techcareer.mobileapphackathon.common.base.BaseViewModel
import com.techcareer.mobileapphackathon.common.util.exteinsion.launch
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

/**
 * @author: Hasan Küçük on 10.10.2021
 */

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val firebaseAuthRepository: FirebaseAuthRepository,
    private val firebaseDaoRepository: FirebaseDaoRepository
) : BaseViewModel() {

    private var _searchNavigateState = MutableStateFlow<SearchNavigateState?>(null)
    val searchNavigateState: StateFlow<SearchNavigateState?> = _searchNavigateState

    private var _userList = MutableStateFlow<List<UserModel>?>(null)
    val userList: StateFlow<List<UserModel>?> = _userList

    fun sendUserList(list: List<UserModel>?) = launch { _userList.emit(list) }

    fun getChatRoom(receiverUserModel: UserModel) = launch {
        firebaseAuthRepository.getCurrentUser().collect {
            it?.let {
                val roomId =
                    if (receiverUserModel.uid!! > it.uid) "${receiverUserModel.uid}_${it.uid}" else "${it.uid}_$receiverUserModel"
                Log.i("SIGN_", "roomId $roomId")
                val senderUserModel = UserModel(
                    email = it.email,
                    displayName = it.displayName,
                    uid = it.uid,
                    isOnline = true
                )
                val senderChatRoom = ChatModel(
                    roomId,
                    senderId = senderUserModel.uid,
                    receiverId = receiverUserModel.uid,
                    sender = senderUserModel,
                    receiver = receiverUserModel
                )
                val receiverChatRoom = ChatModel(
                    roomId, senderId = receiverUserModel.uid,
                    receiverId = senderUserModel.uid,
                    sender = receiverUserModel,
                    receiver = senderUserModel
                )
                firebaseDaoRepository.getChatRoom(roomId, senderChatRoom, receiverChatRoom)
                delay(400)
                _searchNavigateState.emit(SearchNavigateState.NavigateChatRoom(senderChatRoom))
            }
        }
    }

    fun changeSearchNavigateState(state: SearchNavigateState) = launch {
        _searchNavigateState.emit(state)
    }

}

sealed class SearchNavigateState {
    class NavigateChatRoom(val chatModel: ChatModel) : SearchNavigateState()
    object DefaultState : SearchNavigateState()
}