package com.chaitu.videocompressor.ui

import android.Manifest
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.chaitu.videocompressor.R
import com.chaitu.videocompressor.interfaces.DefaultFFmpegResponseHandler
import com.chaitu.videocompressor.permission.isPermissionAvailable
import com.github.hiteshsondhi88.libffmpeg.FFmpeg

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(
                    R.id.container,
                    VideoSelectorFragment.newInstance()
                )
                .addToBackStack(VideoSelectorFragment.TAG)
                .commit()
        }

        loadFFMpegLibrary()
    }

    override fun onResume() {
        super.onResume()
        if (!isPermissionDialogShown() || !isPermissionAvailable(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            showPermissionDialog()
        }
    }

    private fun loadFFMpegLibrary() {
        FFmpeg.getInstance(applicationContext).loadBinary(DefaultFFmpegResponseHandler())
    }

    private fun isPermissionDialogShown() =
        supportFragmentManager.findFragmentByTag(PermissionDialog.TAG) != null

    private fun showPermissionDialog() {
        PermissionDialog().show(supportFragmentManager, PermissionDialog.TAG)
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount == 1) {
            finish()
        } else {
            supportFragmentManager.popBackStack(VideoSelectorFragment.TAG, 0)
        }
    }
}