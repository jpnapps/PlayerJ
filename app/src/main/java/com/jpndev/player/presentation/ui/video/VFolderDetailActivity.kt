package com.jpndev.player.presentation.ui.video

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jpndev.player.databinding.ActivityVideoFolderDetailBinding
import com.jpndev.player.ui.manage_log.VideoFolderVMFactory
import com.jpndev.player.utils.constants.Common
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * Activity class for handling video folder detail screen
 * */
@AndroidEntryPoint
class VFolderDetailActivity : AppCompatActivity() {

    @Inject
    lateinit var factory: VideoFolderVMFactory
    private lateinit var viewModel: VideoFolderViewModel
    private lateinit var binding: ActivityVideoFolderDetailBinding

    @Inject
    lateinit var itemAdapter: VFolderFilesAdapter

    /**
     * Method to onOptionsItemSelected
     * */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        onBackPressed()
        return true
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this, factory).get(VideoFolderViewModel::class.java)
        binding = ActivityVideoFolderDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initRcv(binding.rcv)
        intent.getStringExtra(Common.ID_VIDEO_FOLDER_PATH)?.let {
            binding.leftTitleTxv.text=it
            viewModel.refreshLocalFolderVideos(this@VFolderDetailActivity, it)
        }
        viewModel.mld_videofiles.observe(this, {
            viewModel.addLog("VFDA mld_videofiles observe size " + it?.size)
            itemAdapter.setList(it)
        })
        setTitleViews()
    }

    /**
     * Method to init setTitleViews
     * */
    private fun setTitleViews() {
        binding.backDimv.setOnClickListener {
            onBackPressed()
        }
    }

    /**
     * Method to init RecyclerView
     * @param rcv: RecyclerView. to apply manager ,and datas
     * */
    private fun initRcv(rcv: RecyclerView) {
        rcv.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = itemAdapter
        }
        itemAdapter.setViewModel(viewModel)
        itemAdapter.setList(viewModel.mld_videofiles.value)
    }
    /**
     * onBackPressed Method
     * */
    override fun onBackPressed() {
        finish()
    }
}