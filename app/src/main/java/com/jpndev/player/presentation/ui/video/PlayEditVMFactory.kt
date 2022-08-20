package com.jpndev.player.ui.manage_log

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

import com.jpndev.player.domain.usecase.UseCase
import com.jpndev.player.presentation.ui.MainViewModel
import com.jpndev.player.presentation.ui.video.PlayEditViewModel

class PlayEditVMFactory (
    private val app: Application,
    public val usecase: UseCase

): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return PlayEditViewModel(
            app,
            usecase,

        ) as T
    }


}