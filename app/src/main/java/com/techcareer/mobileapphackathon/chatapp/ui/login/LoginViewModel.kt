package com.techcareer.mobileapphackathon.chatapp.ui.login

import com.techcareer.mobileapphackathon.chatapp.repository.login.FirebaseAuthRepository
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
class LoginViewModel @Inject constructor(private val repository: FirebaseAuthRepository) :
    BaseViewModel() {

    var userEmail = MutableStateFlow<String?>(null)
    var userPassword = MutableStateFlow<String?>(null)

    private var _loginState = MutableStateFlow<LoginState?>(null)
    val loginState: StateFlow<LoginState?> = _loginState

    fun signIn() = launch {
        _loginState.emit(LoginState.LoadingState)
        repository.signIn(userEmail.value.toString(), userPassword.value.toString()).collect {
            it.addOnCompleteListener {
                when (it.isSuccessful) {
                    true -> {
                        launch { _loginState.emit(LoginState.Success) }
                    }
                    false -> {
                        launch { _loginState.emit(LoginState.Fail(it.exception?.message)) }
                    }
                }
            }
        }
    }

    fun dontHaveAnyUser() = launch { _loginState.emit(LoginState.DontHaveAnyUser) }

}

sealed class LoginState {
    object Success : LoginState()
    class Fail(val message: String?) : LoginState()
    object DontHaveAnyUser : LoginState()
    object LoadingState : LoginState()
}