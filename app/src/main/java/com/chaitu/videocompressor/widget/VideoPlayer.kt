package com.chaitu.videocompressor.widget

import android.content.Context
import android.net.Uri
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import com.chaitu.videocompressor.R
import kotlinx.android.synthetic.main.view_video_player.view.*

class VideoPlayer @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) :
    FrameLayout(context, attrs), View.OnClickListener {

    init {
        val inflater = context
            .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.view_video_player, this, true)

        videoView.setOnClickListener { handleVideoViewClick() }

        videoView.setOnClickListener(this)

        play.setOnClickListener(this)

        pause.setOnClickListener(this)
    }

    fun setVideoUri(uri: Uri) {
        videoView.setVideoURI(uri)
    }

    fun start() {
        videoView.start()
        showPauseIcon()
        hideControlsWithDelay()
        videoView.setOnCompletionListener { showPlayIcon() }
    }

    private fun handleVideoViewClick() {
        if (areControlsHidden()) {
            if (videoView.isPlaying) {
                showPauseIcon()
                hideControlsWithDelay()
            } else {
                showPlayIcon()
            }
        } else {
            hideControls()
        }
    }

    private fun hideControls() {
        play.visibility = View.GONE
        pause.visibility = View.GONE
    }

    private fun areControlsHidden() = play.visibility == View.GONE && pause.visibility == View.GONE

    private fun showPlayIcon() {
        play.visibility = View.VISIBLE
        pause.visibility = View.GONE
    }

    private fun showPauseIcon() {
        play.visibility = View.GONE
        pause.visibility = View.VISIBLE
    }

    private fun hideControlsWithDelay() {
        removeControlCallback()
        handler?.postDelayed({ hideControls() }, 3000)
    }

    private fun removeControlCallback() {
        handler?.removeCallbacksAndMessages(null)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        removeControlCallback()
    }

    override fun onClick(view: View?) {
        view?.let { v ->
            when (v.id) {
                R.id.videoView -> handleVideoViewClick()
                R.id.play -> {
                    videoView.start()
                    showPauseIcon()
                    handler?.removeCallbacksAndMessages(null)
                    hideControlsWithDelay()
                }
                R.id.pause -> {
                    removeControlCallback()
                    videoView.pause()
                    showPlayIcon()
                }
            }
        }
    }
}