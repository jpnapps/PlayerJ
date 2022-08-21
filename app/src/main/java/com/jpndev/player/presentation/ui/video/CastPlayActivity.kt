package com.jpndev.player.presentation.ui.video

/*import com.google.android.exoplayer2.ExoPlayerFactory*/

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ext.cast.CastPlayer
import com.google.android.exoplayer2.ext.cast.MediaItemConverter
import com.google.android.exoplayer2.ext.cast.SessionAvailabilityListener
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.MimeTypes
import com.google.android.exoplayer2.util.Util
import com.google.android.gms.cast.MediaInfo
import com.google.android.gms.cast.MediaMetadata
import com.google.android.gms.cast.MediaQueueItem
import com.google.android.gms.cast.framework.CastContext
import com.google.android.gms.common.images.WebImage
import com.jpndev.player.R
import com.jpndev.player.data.repository.dataSourceImpl.LogSourceImpl
import com.jpndev.player.databinding.ActivityPlayBinding
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import javax.inject.Inject


@AndroidEntryPoint
class CastPlayActivity : AppCompatActivity(), SessionAvailabilityListener {

    @Inject
    lateinit var logSourceImpl: LogSourceImpl
    private lateinit var path: String

    // the local and remote players
    lateinit var exoPlayer: SimpleExoPlayer
    lateinit var castPlayer: CastPlayer
    private var currentPlayer: Player? = null

    // views associated with the players
    private lateinit var playerView: PlayerView

    // the Cast context
    private lateinit var castContext: CastContext
    private lateinit var castButton: MenuItem

    // Player state params
    private var playWhenReady = true
    private var currentWindow = 0
    private var playbackPosition: Long = 0

    var isFullScreen = false
    var isLock = false

    //private lateinit var binding: ActivityCastPlayBinding
    private lateinit var binding2: ActivityPlayBinding

    // private lateinit var bindingCustomController: CustomControllerViewBinding
    private lateinit var imageViewFullScreen: ImageView
    private lateinit var imageViewLock: ImageView
    private lateinit var linearLayoutControlUp: LinearLayout
    private lateinit var linearLayoutControlBottom: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        binding2 = ActivityPlayBinding.inflate(layoutInflater)
        val view = binding2.root
        setContentView(view)
        setWindowFullScreen()
        /* supportActionBar?.hide()*/
        playerView = findViewById(R.id.exoplayer_video)
        imageViewFullScreen = findViewById(R.id.imageViewFullScreen)
        imageViewLock = findViewById(R.id.imageViewLock)
        linearLayoutControlUp = findViewById(R.id.linearLayoutControlUp)
        linearLayoutControlBottom = findViewById(R.id.linearLayoutControlBottom)
        setLockScreen()
        setFullScreen()
        path = intent.getStringExtra("path") ?: "http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4"
        logSourceImpl.addLog("CPA path = " + path)
        initCastSetup()
    }

    /**
     * Method to  init CastSetup
     * */
    private fun initCastSetup() {
        castContext = CastContext.getSharedInstance(this)
        castPlayer = CastPlayer(castContext, CustomConverter())
        castPlayer?.setSessionAvailabilityListener(this)
    }

    /**
     * Method to set WindowFullScreen
     * */
    private fun setWindowFullScreen() {
        window?.let {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
            hideStatusBar()
            /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                window.setDecorFitsSystemWindows(false)
            }*/
        }
    }

    fun hideStatusBar() {
        logSourceImpl.addLog("hideStatusBar ")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
        }
    }
    /**
     * Starting with API level 24 Android supports multiple windows. As our app can be visible but
     * not active in split window mode, we need to initialize the player in onStart. Before API level
     * 24 we wait as long as possible until we grab resources, so we wait until onResume before
     * initializing the player.
     */
    override fun onStart() {
        super.onStart()
        if (Util.SDK_INT >= 24) {
            initializePlayers()
        }
    }

    override fun onResume() {
        super.onResume()
        if (Util.SDK_INT < 24 || exoPlayer == null) {
            initializePlayers()
        }
    }

    /**
     * Before API Level 24 there is no guarantee of onStop being called. So we have to release the
     * player as early as possible in onPause. Starting with API Level 24 (which brought multi and
     * split window mode) onStop is guaranteed to be called. In the paused state our activity is still
     * visible so we wait to release the player until onStop.
     */
    override fun onPause() {
        super.onPause()
        if (Util.SDK_INT < 24) {
            currentPlayer?.rememberState()
            releaseLocalPlayer()
        }
    }

    override fun onStop() {
        super.onStop()
        if (Util.SDK_INT >= 24) {
            currentPlayer?.rememberState()
            releaseLocalPlayer()
        }
    }

    /**
     * We release the remote player when activity is destroyed
     */
    override fun onDestroy() {
        releaseRemotePlayer()
        currentPlayer = null
        super.onDestroy()
    }

    /**
     * We need to populate the Cast button across all activities as suggested by Google Cast Guide:
     * https://developers.google.com/cast/docs/design_checklist/cast-button#sender-cast-icon-available
     */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val result = super.onCreateOptionsMenu(menu)
        /*  menuInflater.inflate(R.menu.menu_cast, menu)
          castButton = CastButtonFactory.setUpMediaRouteButton(
              applicationContext,
              menu,
              R.id.media_route_menu_item
          )*/
        return result
    }

    /**
     * When back button is pressed, close this activity, which will go back to previous screen
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        /*if (item.itemId == android.R.id.home) {
            finish()
            return true
        }*/
        return super.onOptionsItemSelected(item)
    }

    /**
     * CastPlayer [SessionAvailabilityListener] implementation.
     */
    override fun onCastSessionAvailable() {
        playOnPlayer(castPlayer)
    }

    override fun onCastSessionUnavailable() {
        playOnPlayer(exoPlayer)
    }

    /**
     * Prepares the local and remote players for playback.
     */
    private fun initializePlayers() {
        // first thing to do is set up the player to avoid the double initialization that happens
        // sometimes if onStart() runs and then onResume() checks if the player is null
        //exoPlayer = SimpleExoPlayer.Builder(this).build()

        exoPlayer = SimpleExoPlayer.Builder(this).setSeekForwardIncrementMs(3000)
            .setSeekBackIncrementMs(1000)
            .build()
        playerView.player = exoPlayer

        // start the playback
        if (castPlayer?.isCastSessionAvailable == true) {
            playOnPlayer(castPlayer)
        } else {
            playOnPlayer(exoPlayer)
        }
    }

    /**
     * Sets the video on the current player (local or remote), whichever is active.
     */
    private fun startPlayback() {
        //val uri = Uri.parse(videoClipUrl)
        val uri = Uri.fromFile(File(path))
        // if the current player is the ExoPlayer, play from it
        if (currentPlayer == exoPlayer) {
            // build the MediaSource from the URI

            val dataSourceFactory =
                DefaultDataSourceFactory(this@CastPlayActivity, getString(R.string.app_name))
            val mediaSource: MediaSource =
                ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(
                    MediaItem.fromUri(uri)
                )
            // use stored state (if any) to resume (or start) playback
            exoPlayer?.playWhenReady = playWhenReady
            exoPlayer?.seekTo(currentWindow, playbackPosition)
            exoPlayer?.prepare(mediaSource, false, false)
        }

        // if the current player is the CastPlayer, play from it
        if (currentPlayer == castPlayer) {
            val metadata = MediaMetadata(MediaMetadata.MEDIA_TYPE_MOVIE)
            metadata.putString(MediaMetadata.KEY_TITLE, "Title")
            metadata.putString(MediaMetadata.KEY_SUBTITLE, "Subtitle")
            metadata.addImage(WebImage(Uri.parse("any-image-url")))

            val mediaInfo = MediaInfo.Builder(path)
                .setStreamType(MediaInfo.STREAM_TYPE_BUFFERED)
                .setContentType(MimeTypes.VIDEO_MP4)
                .setMetadata(metadata)
                .build()
            val mediaqueueItem = MediaQueueItem.Builder(mediaInfo).build()
            val mediaItem: MediaItem =
                MediaItem.Builder().setUri(uri).setTag(mediaqueueItem).build()
            //  val mediaItem = MediaItem.Builder(mediaInfo).build()
            //   castPlayer?.loadItem(mediaItem, playbackPosition)
            castPlayer?.setMediaItem(mediaItem, playbackPosition)
            exoPlayer.setMediaItem(mediaItem);
            //castPlayer.setMediaItem(mediaItem,playbackPosition.asJava().toLong() )
        }
    }


    class CustomConverter : MediaItemConverter {
        override fun toMediaQueueItem(mediaItem: MediaItem): MediaQueueItem {
            // The MediaQueueItem you build is expected to be in the tag.
            return mediaItem.playbackProperties?.tag as MediaQueueItem
        }

        override fun toMediaItem(item: MediaQueueItem): MediaItem {
            // This should give the same as when you build your media item to be passed to ExoPlayer.
            return MediaItem.Builder()
                .setUri(item.getMedia().getContentUrl())
                .setTag(item)
                .build()
        }
    }

    /**
     * Sets the current player to the selected player and starts playback.
     */
    private fun playOnPlayer(player: Player?) {
        if (currentPlayer == player) {
            return
        }

        // save state from the existing player
        currentPlayer?.let {
            if (it.playbackState != Player.STATE_ENDED) {
                it.rememberState()
            }
            it.stop(true)
        }
        // set the new player
        currentPlayer = player
        // set up the playback
        startPlayback()
    }

    /**
     * Remembers the state of the playback of this Player.
     */
    private fun Player.rememberState() {
        this@CastPlayActivity.playWhenReady = playWhenReady
        this@CastPlayActivity.playbackPosition = currentPosition
        this@CastPlayActivity.currentWindow = currentWindowIndex
    }

    /**
     * Releases the resources of the local player back to the system.
     */
    private fun releaseLocalPlayer() {
        exoPlayer?.release()
        // exoPlayer = null
        playerView.player = null
    }

    /**
     * Releases the resources of the remote player back to the system.
     */
    private fun releaseRemotePlayer() {
        castPlayer?.setSessionAvailabilityListener(null)
        castPlayer?.release()
        // castPlayer = null
    }

    /**
     * Method to show/hide Lock layout
     * @param lock: Boolean
     * */
    private fun lockScreen(lock: Boolean) {
        if (lock) {
            linearLayoutControlUp.visibility = View.INVISIBLE
            linearLayoutControlBottom.visibility = View.INVISIBLE
        } else {
            linearLayoutControlUp.visibility = View.VISIBLE
            linearLayoutControlBottom.visibility = View.VISIBLE
        }
    }

    /**
     * Method to handling Lock
     * */
    private fun setLockScreen() {
        logSourceImpl.addLog("setLockScreen ")
        imageViewLock.setOnClickListener {
            logSourceImpl.addLog("imageViewLock clicked " + isLock)
            if (!isLock) {
                imageViewLock.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.ic_baseline_lock
                    )
                )
            } else {
                imageViewLock.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.ic_baseline_lock_open
                    )
                )
            }
            isLock = !isLock
            lockScreen(isLock)
        }
    }

    /**
     * Method to handling FullScreen
     * */
    @SuppressLint("SourceLockedOrientationActivity")
    private fun setFullScreen() {
        logSourceImpl.addLog("setFullScreen ")
        imageViewFullScreen.setOnClickListener {
            logSourceImpl.addLog("imageViewFullScreen clicked " + isFullScreen)
            if (!isFullScreen) {
                imageViewFullScreen.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.ic_baseline_fullscreen_exit
                    )
                )
                requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
            } else {
                imageViewFullScreen.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.ic_baseline_fullscreen
                    )
                )
                requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            }
            isFullScreen = !isFullScreen
        }
    }

    /**
     * Method to onBackPressed
     * */
    override fun onBackPressed() {
        if (isLock) return
        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            imageViewFullScreen.performClick()
        } else super.onBackPressed()
    }

}