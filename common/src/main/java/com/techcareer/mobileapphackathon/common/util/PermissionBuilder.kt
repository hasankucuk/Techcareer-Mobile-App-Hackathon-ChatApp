package com.techcareer.mobileapphackathon.common.util

class PermissionBuilder {
    var onGranted: () -> Unit = {}
    var onDenied: () -> Unit = {}
}