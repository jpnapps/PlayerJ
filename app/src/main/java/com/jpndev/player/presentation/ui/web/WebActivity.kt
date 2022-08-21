package com.jpndev.player.presentation.ui.web

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.jpndev.player.R
import com.jpndev.player.databinding.ActivityWebBinding
import com.jpndev.player.utils.constants.Common

/**
 * Activity for handling load url in Webview
 * */
class WebActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWebBinding
    /**
     * onCreate Method
     * */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWebBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupURLWebView(intent.getStringExtra(Common.PRIVACY_POLICY_URL) ?: "")
        binding.leftTitleTXV.setText(getString(R.string.privacy_policy))
        binding.backDimv.setOnClickListener {
            onBackPressed()
        }
    }

    /**
     * Method to load url in webview
     * @param url: String, url to load webview
     * */
    private fun setupURLWebView(url: String = Common.PRIVACY_POLICY_URL) {
        val webView = binding.webView
        webView.loadUrl(url)
    }

    /**
     * onBackPressed Method
     * */
    override fun onBackPressed() {
        finish()
    }

}