package com.techcareer.mobileapphackathon.chatapp.ui.home

import com.techcareer.mobileapphackathon.chatapp.repository.login.FirebaseAuthRepository
import com.techcareer.mobileapphackathon.chatapp.repository.login.UserModel
import com.techcareer.mobileapphackathon.common.base.BaseViewModel
import com.techcareer.mobileapphackathon.common.util.exteinsion.launch
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

/**
 * @author: Hasan Küçük on 10.10.2021
 */

@HiltViewModel
class ChatRoomListViewModel @Inject constructor(private val repository: FirebaseAuthRepository) :
    BaseViewModel() {

    private var _authenticationState = MutableStateFlow<AuthenticationState?>(null)
    val authenticationState: StateFlow<AuthenticationState?> = _authenticationState

    init {
        launch {
            repository.getCurrentUser().collect {
                if (it == null) {
                    _authenticationState.emit(AuthenticationState.Unauthenticated)
                } else {
                    _authenticationState.emit(
                        AuthenticationState.Authenticated(
                            UserModel(
                                displayName = it.displayName,
                                email = it.email,
                                uid = it.uid
                            )
                        )
                    )
                }
            }
        }
    }

    fun signOut() = launch {
        repository.signOut()
        _authenticationState.emit(AuthenticationState.SignOut)
    }
}

sealed class AuthenticationState {
    class Authenticated(val user: UserModel) : AuthenticationState()
    object Unauthenticated : AuthenticationState()
    object SignOut : AuthenticationState()
}