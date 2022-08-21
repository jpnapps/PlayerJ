package com.jpndev.player.presentation.ui.video

import android.app.Activity
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.DefaultMediaSourceFactory
import com.google.android.exoplayer2.source.MediaSourceFactory
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.jpndev.player.R
import com.jpndev.player.databinding.ActivityVideoPlayerBinding
import java.io.File
import com.google.android.exoplayer2.upstream.FileDataSource

import com.google.android.exoplayer2.upstream.DataSpec
import com.google.android.exoplayer2.upstream.FileDataSource.FileDataSourceException
import com.google.android.exoplayer2.source.MediaSource
import com.jpndev.player.data.repository.dataSourceImpl.LogSourceImpl
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


const val STREAM_URL = "http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4"
@AndroidEntryPoint
class VideoPlayerActivity : AppCompatActivity() {

    @Inject
    lateinit var logSourceImpl: LogSourceImpl
    private lateinit var simpleExoPlayer: SimpleExoPlayer
    private lateinit var mediaDataSourceFactory: DataSource.Factory
    private lateinit var binding : ActivityVideoPlayerBinding
    lateinit var path:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        path= intent.getStringExtra("path")?: STREAM_URL
        logSourceImpl.addLog("VPA path = "+path)
        binding = ActivityVideoPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
       // initLocalPlayer()
    }

/*    private fun initLocalPlayer() {
        val videoUrl=Uri.fromFile( File(path))
        simpleExoPlayer = SimpleExoPlayer.Builder(this).build()
        val dataSpec = DataSpec(videoUrl)
        val fileDataSource = FileDataSource()
        try {
            fileDataSource.open(dataSpec)
        } catch (e: FileDataSourceException) {
            e.printStackTrace()
        }
        val factory = DataSource.Factory { fileDataSource }
        simpleExoPlayer.prepare(buildMediaSourceNew(videoUrl))
        binding.playerView.setPlayer(simpleExoPlayer)
        simpleExoPlayer.playWhenReady = true
    }
    private fun buildMediaSourceNew(uri: Uri): MediaSource? {
        val datasourceFactroy: DataSource.Factory = DefaultDataSourceFactory(
            this,
            Util.getUserAgent(this, "Your App Name")
        )
        return Factory(datasourceFactroy).createMediaSource(uri)
    }*/
    private fun initializePlayer() {
        val videoUrl=Uri.fromFile( File(path))
      logSourceImpl.addLog("VPA videoUrl = "+videoUrl)
        mediaDataSourceFactory = DefaultDataSourceFactory(this, Util.getUserAgent(this, getString(R.string.app_name)))
        //val mediaSource = ProgressiveMediaSource.Factory(mediaDataSourceFactory).createMediaSource(videoUrl)
     val mediaSource = ProgressiveMediaSource.Factory(mediaDataSourceFactory).createMediaSource(MediaItem.fromUri(videoUrl))

        val mediaSourceFactory: MediaSourceFactory = DefaultMediaSourceFactory(mediaDataSourceFactory)

        simpleExoPlayer = SimpleExoPlayer.Builder(this)
                .setMediaSourceFactory(mediaSourceFactory)
                .build()

        simpleExoPlayer.prepare(mediaSource)



   // binding.playerView.keepScreenOn = true
        binding.playerView.setShutterBackgroundColor(Color.BLACK)
        binding.playerView.player = simpleExoPlayer
  //  simpleExoPlayer.play()
    simpleExoPlayer.playWhenReady = true
        binding.playerView.requestFocus()
    }

    private fun releasePlayer() {
        simpleExoPlayer.release()
    }

    public override fun onStart() {
        super.onStart()

        if (Util.SDK_INT > 23) initializePlayer()
    }

    public override fun onResume() {
        super.onResume()

        if (Util.SDK_INT <= 23) initializePlayer()
    }

    public override fun onPause() {
        super.onPause()

        if (Util.SDK_INT <= 23) releasePlayer()
    }

    public override fun onStop() {
        super.onStop()

        if (Util.SDK_INT > 23) releasePlayer()
    }

   /* companion object {
        const val STREAM_URL = "http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4"
    }*/
}