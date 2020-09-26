package com.chaitu.videocompressor.ui

import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.chaitu.videocompressor.interfaces.DefaultTextWatcher
import com.chaitu.videocompressor.R
import com.chaitu.videocompressor.standard.getAbsolutePathFromUri
import com.chaitu.videocompressor.standard.getFileNameFromAbsolutePath
import com.chaitu.videocompressor.standard.getTargetPath
import com.chaitu.videocompressor.standard.hideKeyboard
import com.chaitu.videocompressor.util.VideoCompressState
import com.chaitu.videocompressor.viewmodel.VideoViewModel
import kotlinx.android.synthetic.main.fragment_video_compressor.*

private const val URL = "url"
private const val COMPRESSED_VIDEOS_FOLDER_NAME = "CompressedVideos"

class VideoCompressorFragment : Fragment(), View.OnClickListener {

    private lateinit var uri: Uri
    private lateinit var videoViewModel: VideoViewModel

    companion object {

        fun newInstance(url: String): Fragment {
            val fragment =
                VideoCompressorFragment()
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
        return inflater.inflate(R.layout.fragment_video_compressor, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        videoPlayer.apply {
            setVideoUri(uri)
            start()
        }

        compressVideo.setOnClickListener(this)

        bitRate.addTextChangedListener(object : DefaultTextWatcher() {
            override fun afterTextChanged(s: Editable?) {
                super.afterTextChanged(s)
                compressVideo.isEnabled = (s?.length ?: 0) > 0
            }
        })
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        videoViewModel = ViewModelProviders.of(this).get(VideoViewModel::class.java)

        videoViewModel.compressState.observe(
            viewLifecycleOwner,
            Observer { handleCompressState(it) })

        videoViewModel.compressedVideoUri.observe(viewLifecycleOwner,
            Observer { navigateToVideoViewer(it) })

        videoViewModel.compressLog.observe(viewLifecycleOwner,
            Observer { log.text = it })
    }

    private fun handleCompressState(state: String) {
        when (state) {
            VideoCompressState.START -> setCompressVideoNotInProgress()
            VideoCompressState.FAILED -> {
                Toast.makeText(context, "Unable to compress video", Toast.LENGTH_SHORT)
                    .show()
                setCompressVideoNotInProgress()
            }
            VideoCompressState.FINISH -> setCompressVideoNotInProgress()
            VideoCompressState.PROGRESS -> setCompressVideoInProgress()
        }
    }

    private fun setCompressVideoNotInProgress() {
        compressVideo.text = getString(R.string.compress_video)
        compressVideo.isEnabled = true
    }

    private fun setCompressVideoInProgress() {
        compressVideo.text = getString(R.string.compressing)
        compressVideo.isEnabled = false
    }

    override fun onClick(view: View?) {
        view?.let { v ->
            when (v.id) {
                R.id.compressVideo -> {
                    v.hideKeyboard()
                    val sourcePath = requireContext().getAbsolutePathFromUri(uri)
                    val targetPath = requireContext().getTargetPath(
                        COMPRESSED_VIDEOS_FOLDER_NAME,
                        sourcePath.getFileNameFromAbsolutePath()
                    )
                    videoViewModel.compressVideo(
                        bitRate.editableText.toString(),
                        sourcePath,
                        targetPath
                    )
                }
            }
        }
    }

    private fun navigateToVideoViewer(url: String?) {
        url?.let {
            Toast.makeText(context, "Video Compressed", Toast.LENGTH_SHORT).show()
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.container, VideoViewerFragment.newInstance(url))
                .addToBackStack(null)
                .commit()
        }
    }
}