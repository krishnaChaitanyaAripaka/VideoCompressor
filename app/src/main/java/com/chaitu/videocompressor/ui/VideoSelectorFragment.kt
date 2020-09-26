package com.chaitu.videocompressor.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.chaitu.videocompressor.R
import kotlinx.android.synthetic.main.fragment_video_selector.*

private const val VIDEO_REQUEST_CODE = 100

class VideoSelectorFragment : Fragment() {

    companion object {
        fun newInstance() = VideoSelectorFragment()
        val TAG = VideoSelectorFragment::class.java.simpleName
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_video_selector, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        selectVideo.setOnClickListener { chooseVideoFromGallery() }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            VIDEO_REQUEST_CODE ->
                when (resultCode) {
                    Activity.RESULT_OK -> {
                        data?.data?.let {
                            navigate(it.toString())
                        }
                    }
                }
        }
    }

    private fun chooseVideoFromGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(
            intent,
            VIDEO_REQUEST_CODE
        )
    }

    private fun navigate(url: String) {
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(
                R.id.container,
                VideoCompressorFragment.newInstance(
                    url
                )
            )
            .addToBackStack(null)
            .commit()
    }
}