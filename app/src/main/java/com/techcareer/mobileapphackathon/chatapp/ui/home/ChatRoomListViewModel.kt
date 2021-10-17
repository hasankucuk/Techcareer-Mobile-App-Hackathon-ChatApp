package com.techcareer.mobileapphackathon.chatapp.ui.home

import com.techcareer.mobileapphackathon.chatapp.repository.home.FirebaseDaoRepository
import com.techcareer.mobileapphackathon.chatapp.repository.login.FirebaseAuthRepository
import com.techcareer.mobileapphackathon.chatapp.repository.login.UserModel
import com.techcareer.mobileapphackathon.common.base.BaseViewModel
import com.techcareer.mobileapphackathon.common.util.exteinsion.launch
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

/**
 * @author: Hasan Küçük on 10.10.2021
 */

@HiltViewModel
class ChatRoomListViewModel @Inject constructor(
    private val repository: FirebaseAuthRepository,
    private val firebaseDaoRepository: FirebaseDaoRepository
) :
    BaseViewModel() {
    private var _viewState = MutableStateFlow<ChatRoomViewState?>(null)
    val viewState: StateFlow<ChatRoomViewState?> = _viewState

    private var _authenticationState = MutableStateFlow<AuthenticationState?>(null)
    val authenticationState: StateFlow<AuthenticationState?> = _authenticationState

    private var _userList = MutableStateFlow<List<UserModel>?>(null)
    val userList: StateFlow<List<UserModel>?> = _userList

    private var _currentUser = MutableStateFlow<UserModel>(UserModel())
    val currentUser: StateFlow<UserModel> = _currentUser

    private var searchForUserJob: Job? = null

    init {
        launch {
            repository.getCurrentUser().collect {
                if (it == null) {
                    _authenticationState.emit(AuthenticationState.Unauthenticated)
                } else {
                    val currentUser = UserModel(
                        displayName = it.displayName,
                        email = it.email,
                        uid = it.uid
                    )
                    _authenticationState.emit(
                        AuthenticationState.Authenticated(
                            currentUser
                        )
                    )
                    _currentUser.emit(currentUser)
                }
            }
        }
    }

    fun onScreenState(state: ChatRoomViewState) = launch {
        _viewState.emit(state)
    }

    fun searchUser(text: String) {
        searchForUserJob?.cancel()
        searchForUserJob = launch {
            firebaseDaoRepository.searchUser(text).collect {
                _userList.emit(it)
            }
        }
    }

    @ExperimentalCoroutinesApi
    fun getChatRoomList() = firebaseDaoRepository.getChatRoomList(currentUser.value.uid!!)

    fun signOut() = launch {
        repository.signOut()
        delay(500)
        _authenticationState.emit(AuthenticationState.SignOut)
    }
}

sealed class AuthenticationState {
    class Authenticated(val user: UserModel) : AuthenticationState()
    object Unauthenticated : AuthenticationState()
    object SignOut : AuthenticationState()
}

sealed class ChatRoomViewState(val title: String = "", val desc: String = "") {
    object ChatRoomListEmpty : ChatRoomViewState("Sesli mesajlaştığın kimse yok", "Arama yaparak kullanıcılar ile sesli mesajlaş! Elğenceye katıl.")
    object DefaultState : ChatRoomViewState()

    fun isChatRoomListEmpty() = this is ChatRoomListEmpty
    fun isDefaultState() = this is DefaultState
}