package com.jpndev.player.presentation.ui.about

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.jpndev.player.R

import com.jpndev.player.databinding.ActivityVersionBinding
import com.jpndev.player.BuildConfig

class VersionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityVersionBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //    setContentView(R.layout.activity_version)
        binding = ActivityVersionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val versionName = BuildConfig.VERSION_NAME
        binding.latestversionCtxv.text = "Version " + versionName
        //close_dimv.setOnClickListener(View.OnClickListener { onBackFinish() })
    }

    override fun onBackPressed() {
        // super.onBackPressed()
        finish()
    }
}