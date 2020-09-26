package com.chaitu.videocompressor.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.chaitu.videocompressor.standard.getBitrate
import com.chaitu.videocompressor.standard.getSize
import com.chaitu.videocompressor.util.VideoCompressState
import com.github.hiteshsondhi88.libffmpeg.FFmpeg
import com.github.hiteshsondhi88.libffmpeg.FFmpegExecuteResponseHandler

class VideoViewModel constructor(app: Application) : AndroidViewModel(app) {

    val compressState = MutableLiveData<String>()
    val compressedVideoUri = MutableLiveData<String>()
    val compressLog = MutableLiveData<String>()

    fun compressVideo(bitrate: String, sourcePath: String, targetPath: String) {
        var compressed = false
        val ffmpeg = FFmpeg.getInstance(getApplication())
        Log.d("TAG", "source Path : $sourcePath")
        Log.d("TAG", "Target Path : $targetPath")
        ffmpeg.killRunningProcesses()
        execute(ffmpeg, bitrate, sourcePath, targetPath, compressed)
    }

    private fun execute(
        ffmpeg: FFmpeg,
        bitrate: String,
        sourcePath: String,
        targetPath: String,
        compressed: Boolean
    ) {
        var compressed1 = compressed
        ffmpeg.execute(
            arrayOf(
                "-i",
                sourcePath,
                "-b",
                "${bitrate}k",
                targetPath
            ), object : FFmpegExecuteResponseHandler {
                override fun onFinish() {
                    compressState.value = VideoCompressState.FINISH
                    if (compressed1) {
                        compressedVideoUri.value = targetPath
                    }
                }

                override fun onSuccess(message: String?) {
                    compressState.value = VideoCompressState.SUCCESS
                    compressed1 = true
                }

                override fun onFailure(message: String?) {
                    compressState.value = VideoCompressState.FAILED
                }

                override fun onProgress(message: String?) {

                    message?.run {
                        val size = getSize()
                        val bitrate = getBitrate()

                        if (!size.isNullOrEmpty() && !bitrate.isNullOrEmpty()) {
                            compressLog.value =
                                "size ${message?.getSize()}, Bitrate ${message?.getBitrate()}"
                        }
                    }

                    compressState.value = VideoCompressState.PROGRESS
                }

                override fun onStart() {
                    compressed1 = false
                    compressState.value = VideoCompressState.START
                }
            })
    }
}