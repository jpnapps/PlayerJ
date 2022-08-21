package com.jpndev.player.presentation.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.jpndev.player.R
import com.jpndev.player.data.util.Resource
import com.jpndev.player.databinding.ActivityMainBinding
import com.jpndev.player.databinding.ActivitySplashBinding
import com.jpndev.player.presentation.ui.topqa.TopQAViewModel
import com.jpndev.player.ui.manage_log.MainVMFactory
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    @Inject
    lateinit var  factory: SplashVMFactory
    lateinit var viewModel: SplashViewModel

    private lateinit var binding: ActivitySplashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel= ViewModelProvider(this,factory).get(SplashViewModel::class.java)
        viewModel.activity=this@SplashActivity
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Handler(Looper.getMainLooper()).postDelayed({  viewModel.refreshAppUpdate()}, 2000)
        observeDatas()
    }

    private fun observeDatas() {
        viewModel.app_update_mld.observe(this,{response->
            when(response)
            {
                is Resource.Loading->{
                    showProgressBar()
                }
                is Resource.Success->{
                    hideProgressBar()
                    response.data?.let { viewModel.checkUpdate(it) }
                }
                is Resource.ServerError->{
                    hideProgressBar()
                    viewModel.showMainAcivity(this@SplashActivity)
                  //  response.data?.let { viewModel.checkUpdate(it) }
                }
                is Resource.Error->{
                    hideProgressBar()
                    response.message?.let{
                        Toast.makeText(this,"Error : "+it, Toast.LENGTH_SHORT).show()
                    }
                }
            }

        })
    }

    private fun hideProgressBar() {
        //isLoading=false
        binding.progressBar.visibility= View.GONE
    }

    private fun showProgressBar() {
        //isLoading=true
        binding.progressBar.visibility= View.VISIBLE
    }
}