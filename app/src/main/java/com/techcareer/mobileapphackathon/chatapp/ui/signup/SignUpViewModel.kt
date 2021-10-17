package com.techcareer.mobileapphackathon.chatapp.ui.signup

import com.google.firebase.auth.*
import com.techcareer.mobileapphackathon.chatapp.repository.home.FirebaseDaoRepository
import com.techcareer.mobileapphackathon.chatapp.repository.login.FirebaseAuthRepository
import com.techcareer.mobileapphackathon.chatapp.repository.login.UserModel
import com.techcareer.mobileapphackathon.common.base.BaseViewModel
import com.techcareer.mobileapphackathon.common.util.exteinsion.launch
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collect
import javax.inject.Inject


/**
 * @author: Hasan Küçük on 9.10.2021
 */

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val repository: FirebaseAuthRepository,
    private val firebaseDaoRepository: FirebaseDaoRepository
) :
    BaseViewModel() {

    var userName = MutableStateFlow<String?>(null)
    var userEmail = MutableStateFlow<String?>(null)
    var userPassword = MutableStateFlow<String?>(null)

    private var _signUpState = MutableSharedFlow<SignUpState?>()
    val signUpState: SharedFlow<SignUpState?> = _signUpState

    fun alreadyExistUser() = launch { _signUpState.emit(SignUpState.AlreadyExistUser) }

    fun signUpUser() = launch {
        _signUpState.emit(SignUpState.LoadingState)

        firebaseDaoRepository.isUserNameAvailable(userName.value.toString()) {
            if (it) {
                createUser()
            } else {
                launch { _signUpState.emit(SignUpState.Fail("Bu kullanıcı adına sahip bir hesap zaten var. Lütfen başka bir kullanıcı adı seçin.")) }
            }
        }
    }

    private fun createUser() = launch {
        repository.signUp(userEmail.value.toString(), userPassword.value.toString()).collect {
            it.addOnCompleteListener {
                when (it.isSuccessful) {
                    true -> {
                        launch {
                            val profileUpdates = UserProfileChangeRequest.Builder()
                                .setDisplayName(userName.value)
                                .build()
                            repository.getCurrentUser().collect {
                                it?.let {
                                    it.updateProfile(profileUpdates)
                                    val userModel = UserModel(
                                        displayName = userName.value.toString(), uid = it.uid,
                                        email = it.email,
                                        isOnline = false
                                    )
                                    firebaseDaoRepository.createUser(userModel) {
                                        if (it) {
                                            launch { _signUpState.emit(SignUpState.Success) }
                                        } else {

                                        }
                                    }
                                }
                            }

                        }
                    }
                    false -> {
                        try {
                            throw it.exception!!
                        } catch (error: FirebaseAuthException) {
                            val message = when (error) {
                                is FirebaseAuthWeakPasswordException -> {
                                    //The given password is invalid. [ Password should be at least 6 characters ]
                                    "Şifre geçersiz. Şifre en az 6 karakter olmalıdır."
                                }
                                is FirebaseAuthInvalidCredentialsException -> {
                                    //The email address is badly formatted.
                                    "Lütfen geçerli bir email adresi girin."

                                }
                                is FirebaseAuthUserCollisionException -> {
                                    //The email address is already in use by another account.
                                    "E-posta adresi zaten başka bir hesap tarafından kullanılıyor."
                                }
                                else -> {
                                    it.exception?.message
                                }
                            }
                            launch { _signUpState.emit(SignUpState.Fail(message)) }
                        }
                    }
                }
            }
        }
    }
}

sealed class SignUpState {
    object Success : SignUpState()
    class Fail(val message: String?) : SignUpState()
    object AlreadyExistUser : SignUpState()
    object LoadingState : SignUpState()
}