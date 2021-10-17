package com.techcareer.mobileapphackathon.chatapp

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.google.firebase.FirebaseApp
import com.techcareer.mobileapphackathon.chatapp.notification.NotificationUtils
import dagger.hilt.android.HiltAndroidApp

/**
 * @author: Hasan Küçük on 9.10.2021
 */
@HiltAndroidApp
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
//        FirebaseDatabase.getInstance().setPersistenceEnabled(true)
        NotificationUtils.createNotificationChannel(this)

    }
    companion object {
        var tempReceiverId: String? = null
        var lastReceiverId: String? = null
    }
}