package com.chaitu.videocompressor.ui

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.chaitu.videocompressor.R
import kotlinx.android.synthetic.main.fragment_video_viewer.*

private const val URL = "url"

class VideoViewerFragment : Fragment() {

    private lateinit var uri: Uri

    companion object {
        fun newInstance(url: String): Fragment {
            val fragment = VideoViewerFragment()
            val args = Bundle()
            args.putString(URL, url)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            uri = Uri.parse(it.getString(URL))
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_video_viewer, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        videoPlayer.setVideoUri(uri)
        videoPlayer.start()
    }
}