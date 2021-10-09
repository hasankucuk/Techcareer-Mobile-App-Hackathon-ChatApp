package com.techcareer.mobileapphackathon.chatapp.ui.splash

import com.techcareer.mobileapphackathon.common.base.BaseViewModel
import com.techcareer.mobileapphackathon.common.util.exteinsion.launch
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

/**
 * @author: Hasan Küçük on 9.10.2021
 */
class SplashScreenViewModel : BaseViewModel() {

    private var _splashState = MutableSharedFlow<SplashState?>()
    var splashState: SharedFlow<SplashState?> = _splashState

    init {
        launch {
            delay(2000)
            _splashState.emit(SplashState.NavigateToMainActivity)
        }
    }
}

sealed class SplashState {
    object NavigateToMainActivity : SplashState()
}