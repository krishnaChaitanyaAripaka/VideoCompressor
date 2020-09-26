package com.chaitu.videocompressor.permission

import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity

fun FragmentActivity.requestPermission(permission: Array<String>, requestCode: Int) {
    ActivityCompat.requestPermissions(
        this,
        permission,
        requestCode
    )
}

fun FragmentActivity.isPermissionAvailable(permission: String): Boolean {
    val permissionCheck =
        ContextCompat.checkSelfPermission(
            this,
            permission
        )
    return permissionCheck == PackageManager.PERMISSION_GRANTED
}
