package com.techcareer.mobileapphackathon.common.util.exteinsion

import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.techcareer.mobileapphackathon.common.base.BaseFragment


/**
 * @author: Hasan Küçük on 10.10.2021
 */

fun View.showSnackBar(message: String?) {
    message?.let {
        Snackbar.make(this, message, Snackbar.LENGTH_LONG).show()
    }
}
