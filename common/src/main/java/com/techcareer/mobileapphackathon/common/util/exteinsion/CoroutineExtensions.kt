package com.techcareer.mobileapphackathon.common.util.exteinsion

import androidx.lifecycle.viewModelScope
import com.techcareer.mobileapphackathon.common.base.BaseViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.launch


/**
 * @author: Hasan Küçük on 9.10.2021
 */

fun BaseViewModel.launch(block: suspend CoroutineScope.() -> Unit) =
    viewModelScope.launch(exceptionHandler, CoroutineStart.DEFAULT, block)
