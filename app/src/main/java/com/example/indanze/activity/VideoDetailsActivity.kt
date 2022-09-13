package com.example.indanze.activity

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.example.indanze.databinding.ActivityVideoDetailsBinding
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.ui.StyledPlayerView

class VideoDetailsActivity : AppCompatActivity() {

    private lateinit var binding : ActivityVideoDetailsBinding

    private lateinit var player: ExoPlayer
    private lateinit var playerView : StyledPlayerView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityVideoDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Video Details"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val bundle:Bundle?=intent.extras
        val videoUserID = bundle!!.getString("firebase_videoUserID")
        val videoImage = bundle!!.getString("firebase_videoImage")
        val videoName = bundle!!.getString("firebase_videoName")
        val videoDescription = bundle!!.getString("firebase_videoDescription")
        val videoVideo = bundle!!.getString("firebase_videoVideo")

        val videoDetailName = binding.txtVideoDetailName
        val videoDetailDescription = binding.txtVideoDetailDescription

        setupPlayer(videoVideo.toString())
        videoDetailName.setText(videoName)
        videoDetailDescription.setText(videoDescription)


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        onBackPressed()

        return true
    }

    private fun setupPlayer(videoUrl : String){

        player = ExoPlayer.Builder(this)
            .setSeekBackIncrementMs(2000)
            .setSeekForwardIncrementMs(2000)
            .build()

        playerView = binding.videoDetailPlayer
        playerView.player = player

        val videoURI: Uri = Uri.parse(videoUrl)
        val mediaItem = MediaItem.fromUri(videoURI)

        player.addMediaItem(mediaItem)
        player.prepare()
        player.playWhenReady = false
    }

}