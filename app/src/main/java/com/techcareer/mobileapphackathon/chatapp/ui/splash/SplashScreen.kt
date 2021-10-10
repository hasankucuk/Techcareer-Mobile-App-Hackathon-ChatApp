package com.techcareer.mobileapphackathon.chatapp.ui.splash

import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.techcareer.mobileapphackathon.chatapp.R
import com.techcareer.mobileapphackathon.chatapp.databinding.ActivitySplashScreenBinding
import com.techcareer.mobileapphackathon.chatapp.ui.home.MainActivity
import com.techcareer.mobileapphackathon.common.base.BaseActivity
import com.techcareer.mobileapphackathon.common.util.exteinsion.launchActivity
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class SplashScreen : BaseActivity<ActivitySplashScreenBinding>() {

    override fun getLayoutId() = R.layout.activity_splash_screen

    private val viewModel: SplashScreenViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        lifecycleScope.launch {
            viewModel.splashState.collect {
                when (it) {
                    SplashState.NavigateToMainActivity -> {
                        launchActivity<MainActivity>()
                        finish()
                    }
                    null -> {
                    }
                }
            }
        }
    }
}