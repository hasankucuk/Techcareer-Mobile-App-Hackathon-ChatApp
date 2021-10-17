package com.techcareer.mobileapphackathon.chatapp.ui.home

import android.os.Bundle
import com.techcareer.mobileapphackathon.chatapp.R
import com.techcareer.mobileapphackathon.chatapp.databinding.ActivityMainBinding
import com.techcareer.mobileapphackathon.common.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : BaseActivity<ActivityMainBinding>() {

    override fun getLayoutId(): Int = R.layout.activity_main

    private val navController by lazy {
        findNavHostFragment(R.id.nav_host_fragment)?.navController
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // navController?.navigate(R.id.loginFragment)
    }


}