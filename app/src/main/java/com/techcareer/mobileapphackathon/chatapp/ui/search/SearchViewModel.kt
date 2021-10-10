package com.techcareer.mobileapphackathon.chatapp.ui.search

import com.techcareer.mobileapphackathon.chatapp.repository.login.UserModel
import com.techcareer.mobileapphackathon.common.base.BaseViewModel
import com.techcareer.mobileapphackathon.common.util.exteinsion.launch
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

/**
 * @author: Hasan Küçük on 10.10.2021
 */

@HiltViewModel
class SearchViewModel @Inject constructor() : BaseViewModel() {

    private var _userList = MutableStateFlow<List<UserModel>?>(null)
    val userList: StateFlow<List<UserModel>?> = _userList

    fun sendUserList(list: List<UserModel>?) = launch { _userList.emit(list) }
}