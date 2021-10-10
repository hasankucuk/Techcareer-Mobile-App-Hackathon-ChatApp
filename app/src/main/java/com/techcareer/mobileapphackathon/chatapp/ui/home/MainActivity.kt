package com.techcareer.mobileapphackathon.chatapp.ui.home

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.techcareer.mobileapphackathon.chatapp.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}