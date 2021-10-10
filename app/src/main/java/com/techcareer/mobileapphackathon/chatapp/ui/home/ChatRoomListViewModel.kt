package com.techcareer.mobileapphackathon.chatapp.ui.home

import android.util.Log
import com.techcareer.mobileapphackathon.chatapp.repository.home.FirebaseDaoRepository
import com.techcareer.mobileapphackathon.chatapp.repository.login.FirebaseAuthRepository
import com.techcareer.mobileapphackathon.chatapp.repository.login.UserModel
import com.techcareer.mobileapphackathon.common.base.BaseViewModel
import com.techcareer.mobileapphackathon.common.util.exteinsion.launch
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
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
//    @Inject
//    lateinit var searchViewModel: SearchViewModel

    private var _authenticationState = MutableStateFlow<AuthenticationState?>(null)
    val authenticationState: StateFlow<AuthenticationState?> = _authenticationState

    private var _userList = MutableStateFlow<List<UserModel>?>(null)
    val userList: StateFlow<List<UserModel>?> = _userList

    private var searchForUserJob: Job? = null

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

    fun searchUser(text: String) {
        searchForUserJob?.cancel()
        Log.i("SIGN_", "search -> $text")

        searchForUserJob = launch {
            firebaseDaoRepository.searchUser(text).collect {
                _userList.emit(it)
//                it.forEach {
//                    Log.i("SIGN_", it.displayName!!)
//                }
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