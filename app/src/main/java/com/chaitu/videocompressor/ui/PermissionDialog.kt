package com.chaitu.videocompressor.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.chaitu.videocompressor.R
import com.chaitu.videocompressor.permission.isPermissionAvailable
import com.chaitu.videocompressor.permission.requestPermission
import kotlinx.android.synthetic.main.layout_permission.*

class PermissionDialog : DialogFragment() {

    companion object {
        val TAG: String = PermissionDialog::class.java.simpleName
        const val PERMISSION_REQUEST_CODE: Int = 100
    }

    override fun getTheme(): Int {
        return R.style.DialogTheme
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        isCancelable = false
        return inflater.inflate(R.layout.layout_permission, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        grantPermission.setOnClickListener { requestPermission() }
    }

    override fun onResume() {
        super.onResume()
        if (requireActivity().isPermissionAvailable(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            dismiss()
        }
    }

    private fun requestPermission() {
        requireActivity().requestPermission(
            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
            PERMISSION_REQUEST_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            PERMISSION_REQUEST_CODE -> {
                if ((grantResults.isNotEmpty() &&
                            grantResults[0] == PackageManager.PERMISSION_GRANTED)
                ) {
                    dismiss()
                }
            }
        }
    }
}