package com.techcareer.mobileapphackathon.common.view

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Window
import com.techcareer.mobileapphackathon.common.R

/**
 * @author: Hasan Küçük on 9.10.2021
 */
class LoadingView(context: Context) : Dialog(context) {
    init {
        window?.run {
            requestFeature(Window.FEATURE_NO_TITLE)
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
        setContentView(R.layout.view_loading)
        setCancelable(true)
    }
}