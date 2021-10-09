package com.techcareer.mobileapphackathon.common.base

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineExceptionHandler

/**
 * @author: Hasan Küçük on 9.10.2021
 */
abstract class BaseViewModel : ViewModel(), LifecycleObserver {

    val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        when (throwable) {
            //Buraya error tipleri gelecek
            else -> throwable.printStackTrace()
        }
    }
}