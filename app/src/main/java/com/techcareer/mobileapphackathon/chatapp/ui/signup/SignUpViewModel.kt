package com.techcareer.mobileapphackathon.chatapp.ui.signup

import com.techcareer.mobileapphackathon.chatapp.repository.login.FirebaseAuthRepository
import com.techcareer.mobileapphackathon.common.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

/**
 * @author: Hasan Küçük on 9.10.2021
 */

@HiltViewModel
class SignUpViewModel @Inject constructor(val repository: FirebaseAuthRepository) :
    BaseViewModel() {

    var userName = MutableStateFlow<String?>(null)
    var userEmail = MutableStateFlow<String?>(null)
    var userPassword = MutableStateFlow<String?>(null)

    fun signUpUser() {
       // repository.signUp()
    }
}