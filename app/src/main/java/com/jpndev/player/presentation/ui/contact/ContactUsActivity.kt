package com.jpndev.player.presentation.ui.contact

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.jpndev.player.R
import com.jpndev.player.databinding.ActivityContactUsBinding
import com.jpndev.player.databinding.ActivityWebBinding
import com.jpndev.player.presentation.ui.MainViewModel
import com.jpndev.player.presentation.ui.SplashVMFactory
import com.jpndev.player.presentation.ui.SplashViewModel
import com.jpndev.player.ui.manage_log.ContactUsVMFactory
import com.jpndev.player.utils.ToastHandler
import com.jpndev.player.utils.constants.Common
import com.jpndev.player.utils.extensions.getAppNameReplacedString
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ContactUsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityContactUsBinding
    @Inject
    lateinit var  factory: ContactUsVMFactory
    lateinit var viewModel: ContactUsViewModel
    /**
     * onCreate Method
     * */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel= ViewModelProvider(this,factory).get(ContactUsViewModel::class.java)
        viewModel.activity=this@ContactUsActivity
        binding = ActivityContactUsBinding.inflate(layoutInflater)
        binding.viewmodel=viewModel
        setContentView(binding.root)
        binding.backDimv.setOnClickListener {
            onBackPressed()
        }
    }
    /**
     * onBackPressed Method
     * */
    override fun onBackPressed() {
        finish()
    }
}