package com.jpndev.player.presentation.ui.manage_log

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.jpndev.player.R

import com.jpndev.player.databinding.ActivityViewLogosBinding

import com.jpndev.player.utils.LogUtils
import com.jpndev.player.utils.PrefUtils
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ViewLogosActivity : AppCompatActivity() {


    @Inject
    lateinit var  factory: ViewLogosViewModelFactory
    lateinit var viewModel: ViewLogosViewModel
    private lateinit var binding: ActivityViewLogosBinding

    @Inject
    lateinit var  prefUtils: PrefUtils

    var count=0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewLogosBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel= ViewModelProvider(this,factory).get(ViewLogosViewModel::class.java)
        binding.viewmodel=viewModel
        binding.lifecycleOwner = this
        // setContentView(R.layout.activity_life_cycle)

        //viewModel. text.value=viewModel.text.value+"\n OnCreate"

        binding.saveBtn.setOnClickListener{

            viewModel.showPManageActivity(activity = this)

        }

        binding.logosTxv.setOnClickListener{

            prefUtils.save("lifecycle","jp "+"lifecyle= "+      ++count)

            LogUtils.LOGD("pref_lc","\n pref = "+ prefUtils.getString("lifecycle","Nothing found"))


        }
        binding.closeDimv.setOnClickListener{

            onBackPressed()

        }
    }
    override fun onBackPressed() {
        // super.onBackPressed()
        finish()
    }


}