package com.techcareer.mobileapphackathon.common.util.exteinsion

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.core.app.ActivityOptionsCompat


/**
 * @author: Hasan Küçük on 9.10.2021
 */
inline fun <reified T : Any> Activity.launchActivity(
    resultLauncher: ActivityResultLauncher<Intent>? = null,
    activityOptionsCompat: ActivityOptionsCompat? = null,
    noinline init: Intent.() -> Unit = {}
) {
    val intent = newIntent<T>(this).apply {
        init()
    }
    resultLauncher?.let {
        it.launch(intent, activityOptionsCompat)
    } ?: startActivity(intent)
}

inline fun <reified T : Any> newIntent(context: Context): Intent = Intent(context, T::class.java)