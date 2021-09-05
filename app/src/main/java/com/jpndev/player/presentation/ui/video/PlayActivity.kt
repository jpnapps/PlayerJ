package com.jpndev.player.presentation.ui.video

import android.content.Context
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowInsets
import android.view.WindowManager
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util

import com.jpndev.player.R
import com.jpndev.player.data.repository.dataSourceImpl.LogSourceImpl
import com.jpndev.player.utils.ClassA
import com.jpndev.player.utils.ClassC
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import javax.inject.Inject

@AndroidEntryPoint
class PlayActivity : AppCompatActivity() {
    @Inject
    lateinit var logSourceImpl: LogSourceImpl
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Llamamos el metodo fullScreen
        setFullScreen()

        setContentView(R.layout.activity_play)
        // Ocutaltamos el ActionBar.
        supportActionBar?.hide()
       // CastButtonFactory
        // Creamos el PlayerView
        val playerView: PlayerView = findViewById(R.id.exoplayer_video)
        // Obtenemos el path del intent
        val path = intent.getStringExtra("path")
        // Si el path no es nulo, crearemos el productor de ExoPlayer.
        logSourceImpl.addLog("PA path = "+path)
        if(path != null) {
            //val uri = Uri.parse(path)
            val uri=Uri.fromFile( File(path))
            val simpleExoPlayer = SimpleExoPlayer.Builder(this).setSeekForwardIncrementMs(3000)
                .setSeekBackIncrementMs(1000)
                .build()
            val factory = DefaultDataSourceFactory(this,
                Util.getUserAgent(this, getString(R.string.app_name)))
            logSourceImpl.addLog("PA app name = "+getString(R.string.app_name))
            val mediaSource: MediaSource = ProgressiveMediaSource.Factory(factory).createMediaSource(
                MediaItem.fromUri(uri))
            playerView.player = simpleExoPlayer
            playerView.keepScreenOn = true

            simpleExoPlayer.prepare(mediaSource)

          //  simpleExoPlayer.addMediaSource(mediaSource)
            simpleExoPlayer.playWhenReady = true
        }
    }

    // Metodo para crear el reproductor en FullScreen (Si se gira la pantalla).
    private fun setFullScreen() {

        requestWindowFeature(Window.FEATURE_NO_TITLE)
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
       /* window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN)*/
    }
    fun enableFullScreen(isEnabled: Boolean) {
        if (isEnabled) {
            window?.decorView?.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        } else {
           window?.decorView?.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
        }
    }
}