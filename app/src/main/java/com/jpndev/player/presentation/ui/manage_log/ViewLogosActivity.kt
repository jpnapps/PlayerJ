package com.jpndev.player.presentation.ui.manage_log

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.jpndev.player.databinding.ActivityViewLogosBinding
import com.jpndev.player.utils.PrefUtils
import com.jpndev.player.utils.ToastHandler
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ViewLogosActivity : AppCompatActivity() {


    @Inject
    lateinit var factory: ViewLogosViewModelFactory
    lateinit var viewModel: ViewLogosViewModel
    private lateinit var binding: ActivityViewLogosBinding

    @Inject
    lateinit var prefUtils: PrefUtils

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewLogosBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this, factory).get(ViewLogosViewModel::class.java)
        binding.viewmodel = viewModel
        binding.lifecycleOwner = this
        setClicks()
    }

    private fun setClicks() {

        binding.saveBtn.setOnClickListener {
            viewModel.showPManageActivity(activity = this)
        }
        binding.logosTxv.setOnClickListener {
            viewModel.usecase.logsource.addLog("VLogosA logosTxv click ")
        }
        binding.closeDimv.setOnClickListener {
            onBackPressed()
        }
        binding.refreshDimv.setOnClickListener {
            viewModel.reFresh()
        }
        binding.deleteDimv.setOnClickListener {
            viewModel.deleteLogs()
        }
        binding.copyDimv.visibility = View.VISIBLE
        binding.copyDimv.setOnClickListener {
            var clipboardManager = getSystemService(
                Context.CLIPBOARD_SERVICE
            ) as ClipboardManager?
            val separator = "\n"
            val text = viewModel.text.value
            var clipData = ClipData.newPlainText("Logos " + separator, text)
            clipboardManager!!.setPrimaryClip(clipData)
            ToastHandler.newInstance(this@ViewLogosActivity)
                .mustShowToast("Copied Logos")
        }
    }

    override fun onBackPressed() {
        finish()
    }
}