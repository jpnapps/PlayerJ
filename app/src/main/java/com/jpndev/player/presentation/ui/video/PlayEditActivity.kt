package com.jpndev.player.presentation.ui.video

import android.content.Context
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util

import com.jpndev.player.R
import com.jpndev.player.data.repository.dataSourceImpl.LogSourceImpl
import com.jpndev.player.databinding.ActivityMainBinding
import com.jpndev.player.databinding.ActivityPlayEditBinding
import com.jpndev.player.presentation.ui.HOME_WEBURL
import com.jpndev.player.presentation.ui.IS_WEBURL
import com.jpndev.player.presentation.ui.MainViewModel
import com.jpndev.player.presentation.ui.topqa.QAAdapter
import com.jpndev.player.presentation.ui.topqa.TopQAViewModel
import com.jpndev.player.ui.manage_log.MainVMFactory
import com.jpndev.player.ui.manage_log.PlayEditVMFactory
import com.jpndev.player.utils.ClassA
import com.jpndev.player.utils.ClassC
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import javax.inject.Inject

@AndroidEntryPoint
class PlayEditActivity : AppCompatActivity() {


    @Inject
    lateinit var logSourceImpl: LogSourceImpl



    @Inject
    lateinit var factory: PlayEditVMFactory
    lateinit var viewModel: PlayEditViewModel
    @Inject
    lateinit var qa_adpater: QAAdapter
    private lateinit var binding: ActivityPlayEditBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Llamamos el metodo fullScreen
        setFullScreen()
        binding = ActivityPlayEditBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel= ViewModelProvider(this,factory).get(PlayEditViewModel::class.java)
        supportActionBar?.hide()

        val path = intent.getStringExtra("path")
        val isWebpath = intent.getBooleanExtra(IS_WEBURL,false)
        logSourceImpl.addLog("PEA path = "+path+" isWebpath = "+isWebpath)
        binding.lifecycleOwner = this
        binding.viewmodel = viewModel
        viewModel.activity=this
        viewModel.initExoPlayer(binding)
        viewModel.setVideoPath2(HOME_WEBURL,true)

        setEditLisnter()


    }





    private fun setEditLisnter() {
        binding.urlAtv.setOnEditorActionListener { v: TextView?, actionId: Int, event: KeyEvent? ->
            if (actionId == EditorInfo.IME_ACTION_GO) {
                val temp_Url =   binding.urlAtv.text.toString()
                viewModel.setVideoPath2(temp_Url,true)
                viewModel.saveVideoPath(temp_Url)

               // logSourceImpl.addLog("PEA setEditLisnter   temp_Url "+temp_Url)
                return@setOnEditorActionListener true
            }
            false
        }
    }

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
        viewModel.simpleExoPlayer?.release()
    }

    override fun onBackPressed() {
        //super.onBackPressed()
        viewModel.backFn()
    }
}