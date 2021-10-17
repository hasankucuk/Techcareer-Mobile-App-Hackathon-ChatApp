package com.techcareer.mobileapphackathon.common.util.exteinsion

import android.content.pm.PackageManager
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.techcareer.mobileapphackathon.common.util.PermissionBuilder


/**
 * @author: Hasan Küçük on 10.10.2021
 */

fun View.showSnackBar(message: String?) {
    message?.let {
        Snackbar.make(this, message, Snackbar.LENGTH_LONG).show()
    }
}

fun Fragment.isPermissionGranted(permission: String): Boolean = ContextCompat.checkSelfPermission(
    requireContext(),
    permission
) == PackageManager.PERMISSION_GRANTED

inline fun Fragment.permissionResult(crossinline permissionBuilder: PermissionBuilder.() -> Unit): ActivityResultLauncher<String> {
    return registerForActivityResult(ActivityResultContracts.RequestPermission()) {
        val builder = PermissionBuilder()
        builder.permissionBuilder()
        if (it) {
            builder.onGranted.invoke()
        } else {
            builder.onDenied.invoke()
        }
    }
}

