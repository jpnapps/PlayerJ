package com.jpndev.player.presentation.ui.video

import android.app.PictureInPictureParams
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.support.v4.media.session.MediaSessionCompat
import android.view.View
import android.view.Window
import android.view.WindowInsets
import android.view.WindowManager
import androidx.annotation.RequiresApi
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util

import com.jpndev.player.R
import com.jpndev.player.data.repository.dataSourceImpl.LogSourceImpl
import com.jpndev.player.presentation.ui.IS_WEBURL
import com.jpndev.player.utils.ClassA
import com.jpndev.player.utils.ClassC
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import javax.inject.Inject

@AndroidEntryPoint
class PlayActivity : AppCompatActivity() {
    @Inject
    lateinit var logSourceImpl: LogSourceImpl
    lateinit var simpleExoPlayer: SimpleExoPlayer
    lateinit var playerView: PlayerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_play)
        setFullScreen()
        supportActionBar?.hide()

        playerView = findViewById(R.id.exoplayer_video)

        val path = intent.getStringExtra("path")
        val isWebpath = intent.getBooleanExtra(IS_WEBURL, false)
        logSourceImpl.addLog("PA path = " + path + " isWebpath = " + isWebpath)
        if (path != null) {
            //val uri = Uri.parse(path)
            val uri = if (isWebpath) Uri.parse(path) else Uri.fromFile(File(path))
            logSourceImpl.addLog("PA uri = " + uri)
            simpleExoPlayer = SimpleExoPlayer.Builder(this).setSeekForwardIncrementMs(6000)
                .setSeekBackIncrementMs(6000)
                .build()
            val factory = DefaultDataSourceFactory(
                this,
                Util.getUserAgent(this, getString(R.string.app_name))
            )
            logSourceImpl.addLog("PA app name = " + getString(R.string.app_name))
            val mediaSource: MediaSource =
                ProgressiveMediaSource.Factory(factory).createMediaSource(
                    MediaItem.fromUri(uri)
                )
            playerView.player = simpleExoPlayer
            playerView.keepScreenOn = true
            simpleExoPlayer.prepare(mediaSource)
            //  simpleExoPlayer.addMediaSource(mediaSource)
            simpleExoPlayer.playWhenReady = true

            //Use Media Session Connector from the EXT library to enable MediaSession Controls in PIP.
            val mediaSession = MediaSessionCompat(this, packageName)
            val mediaSessionConnector = MediaSessionConnector(mediaSession)
            mediaSessionConnector.setPlayer(simpleExoPlayer)
            mediaSession.isActive = true
        }
    }

    // Metodo para crear el reproductor en FullScreen (Si se gira la pantalla).
    private fun setFullScreen() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }

    }

    fun enableFullScreen(isEnabled: Boolean) {
        if (isEnabled) {
            window?.decorView?.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        } else {
            window?.decorView?.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        simpleExoPlayer?.release()
    }


    companion object {
        @JvmField
        //  val ARG_VIDEO_URL = "VideoActivity.URL"
        val ARG_VIDEO_POSITION = "VideoActivity.POSITION"
    }

    var isPIPModeeEnabled: Boolean = true //Has the user disabled PIP mode in AppOpps?
    private var videoPosition: Long = 0L
    var isInPipMode: Boolean = false
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState.apply {
            this?.putLong(ARG_VIDEO_POSITION, simpleExoPlayer.currentPosition)
        })
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        videoPosition = savedInstanceState!!.getLong(ARG_VIDEO_POSITION)
    }

    override fun onBackPressed() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N
            && packageManager.hasSystemFeature(PackageManager.FEATURE_PICTURE_IN_PICTURE)
            && isPIPModeeEnabled
        ) {
            enterPIPMode()
        } else {
            super.onBackPressed()
        }
    }

    override fun onPictureInPictureModeChanged(
        isInPictureInPictureMode: Boolean,
        newConfig: Configuration?
    ) {
        if (newConfig != null) {
            videoPosition = simpleExoPlayer.currentPosition
            isInPipMode = !isInPictureInPictureMode
        }
        super.onPictureInPictureModeChanged(isInPictureInPictureMode, newConfig)
    }

    //Called when the user touches the Home or Recents button to leave the app.
    override fun onUserLeaveHint() {
        super.onUserLeaveHint()
        enterPIPMode()
    }

    @Suppress("DEPRECATION")
    fun enterPIPMode() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N
            && packageManager.hasSystemFeature(PackageManager.FEATURE_PICTURE_IN_PICTURE)
        ) {
            videoPosition = simpleExoPlayer.currentPosition
            playerView.useController = false
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val params = PictureInPictureParams.Builder()
                this.enterPictureInPictureMode(params.build())
               // this.setAutoEnterEnabled(true)
            } else {
                this.enterPictureInPictureMode()
            }
            /* We need to check this because the system permission check is publically hidden for integers for non-manufacturer-built apps
               https://github.com/aosp-mirror/platform_frameworks_base/blob/studio-3.1.2/core/java/android/app/AppOpsManager.java#L1640

               ********* If we didn't have that problem *********
                val appOpsManager = getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
                if(appOpsManager.checkOpNoThrow(AppOpManager.OP_PICTURE_IN_PICTURE, packageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA).uid, packageName) == AppOpsManager.MODE_ALLOWED)

                30MS window in even a restricted memory device (756mb+) is more than enough time to check, but also not have the system complain about holding an action hostage.
             */
            Handler().postDelayed({ checkPIPPermission() }, 30)
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun checkPIPPermission() {
        isPIPModeeEnabled = isInPictureInPictureMode
        if (!isInPictureInPictureMode) {
            onBackPressed()
        }
    }
}