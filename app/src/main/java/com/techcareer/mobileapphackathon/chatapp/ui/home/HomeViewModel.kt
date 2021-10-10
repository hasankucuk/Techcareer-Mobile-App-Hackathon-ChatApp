package com.techcareer.mobileapphackathon.chatapp.ui.home

import com.techcareer.mobileapphackathon.chatapp.repository.login.FirebaseAuthRepository
import com.techcareer.mobileapphackathon.common.base.BaseViewModel
import com.techcareer.mobileapphackathon.common.util.exteinsion.launch
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * @author: Hasan Küçük on 10.10.2021
 */

@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: FirebaseAuthRepository) :
    BaseViewModel() {


}