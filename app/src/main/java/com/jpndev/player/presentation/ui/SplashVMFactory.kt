package com.jpndev.player.presentation.ui

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

import com.jpndev.player.domain.usecase.UseCase
import com.jpndev.player.presentation.ui.MainViewModel
import com.jpndev.player.presentation.ui.SplashViewModel

class SplashVMFactory (
    private val app: Application,
    public val usecase: UseCase

): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return SplashViewModel(
            app,
            usecase,

        ) as T
    }


}