package com.techcareer.mobileapphackathon.chatapp.ui.chat

import com.techcareer.mobileapphackathon.chatapp.repository.home.ChatModel
import com.techcareer.mobileapphackathon.chatapp.repository.home.FirebaseDaoRepository
import com.techcareer.mobileapphackathon.chatapp.repository.home.VoiceMessage
import com.techcareer.mobileapphackathon.common.base.BaseViewModel
import com.techcareer.mobileapphackathon.common.util.exteinsion.launch
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

/**
 * @author: Hasan Küçük on 11.10.2021
 */

@HiltViewModel
class ChatViewModel @Inject constructor(private val firebaseDaoRepository: FirebaseDaoRepository) :
    BaseViewModel() {

    private var _viewState = MutableStateFlow<ChatStateModel?>(null)
    val viewState: StateFlow<ChatStateModel?> = _viewState

    private var _chatModel = MutableStateFlow<ChatModel?>(null)
    val chatModel: StateFlow<ChatModel?> = _chatModel

    private var _voiceMessageList = MutableStateFlow<ArrayList<VoiceMessage>?>(null)
    val voiceMessageList: StateFlow<ArrayList<VoiceMessage>?> = _voiceMessageList

    fun onScreenState(state: ChatStateModel) = launch {
        _viewState.emit(state)
    }

    fun sendChatModel(chatModel: ChatModel) = launch {
        _chatModel.emit(chatModel)
    }

    fun sendVoiceMessage(recordFileName: String, recorderDuration: Long) = launch {

        chatModel.collect {
            it?.let {
                firebaseDaoRepository.sendVoiceMessage(recordFileName, recorderDuration, it)
            }

        }
    }

    @ExperimentalCoroutinesApi
    fun getChatRoomMessageList() = firebaseDaoRepository.getChatRoomMessageList(chatModel.value!!)
}

sealed class ChatStateModel(val title: String = "",  val desc: String = "") {
    object PermissionGranted : ChatStateModel()
    object PermissionDenied :
        ChatStateModel("İzin Ver", "Sesli mesaj gönderebilmen için izin vermen gerekiyor.")

    object ChatListEmpty :
        ChatStateModel("Sesli mesaj bulunmuyor", "Sesli mesaj gönder eğlenceye katıl.")

    fun isPermissionGranted() = this is PermissionGranted || this is ChatListEmpty
    fun isPermissionDenied() = this is PermissionDenied
    fun isPermissionDeniedOrListEmpty() = this is PermissionDenied || this is ChatListEmpty
}






