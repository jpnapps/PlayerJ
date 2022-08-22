package com.jpndev.player.presentation.di

import android.app.Application
import com.jpndev.player.domain.usecase.UseCase
import com.jpndev.player.presentation.ui.SplashVMFactory
import com.jpndev.player.presentation.ui.manage_log.ViewLogosViewModelFactory
import com.jpndev.player.presentation.ui.topqa.QAViewModelFactory
import com.jpndev.player.ui.manage_log.ContactUsVMFactory
import com.jpndev.player.ui.manage_log.MainVMFactory
import com.jpndev.player.ui.manage_log.PlayEditVMFactory
import com.jpndev.player.ui.manage_log.VideoFolderVMFactory


import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn

import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class FactoryModule {

    @Singleton
    @Provides
  fun provideQAViewModelFactory(
        application: Application,
        useCase: UseCase
  ): QAViewModelFactory {
      return QAViewModelFactory(
          application,
          useCase
      )
  }
    @Singleton
    @Provides
    fun provideMainVMFactory(
        application: Application,
        useCase: UseCase
    ): MainVMFactory {
        return MainVMFactory(
            application,
            useCase
        )
    }
    @Singleton
    @Provides
    fun provideViewLogosViewModelFactory(
        application: Application,
        useCase: UseCase
    ): ViewLogosViewModelFactory {
        return ViewLogosViewModelFactory(
            application,
            useCase
        )
    }
    @Singleton
    @Provides
    fun provideSplashVMFactory(
        application: Application,
        useCase: UseCase
    ): SplashVMFactory {
        return SplashVMFactory(
            application,
            useCase
        )
    }

    @Singleton
    @Provides
    fun providePlayEditVMFactory(
        application: Application,
        useCase: UseCase
    ): PlayEditVMFactory {
        return PlayEditVMFactory(
            application,
            useCase
        )
    }

    @Singleton
    @Provides
    fun provideContactUsVMFactory(
        application: Application,
        useCase: UseCase
    ): ContactUsVMFactory {
        return ContactUsVMFactory(
            application,
            useCase
        )
    }

    @Singleton
    @Provides
    fun provideVideoFolderVMFactory(
        application: Application,
        useCase: UseCase
    ): VideoFolderVMFactory {
        return VideoFolderVMFactory(
            application,
            useCase
        )
    }
  /*  @Singleton
    @Provides
    fun providePManageViewModelFactory(
        application: Application,
        useCase: UseCase
    ): PManageViewModelFactory {
        return PManageViewModelFactory(
                application,
                useCase

        )
    }

    @Singleton
    @Provides
    fun provideAddPItemViewModelFactory(
        application: Application,
        useCase: UseCase
    ): AddPItemViewModelFactory {
        return AddPItemViewModelFactory(
                application,
                useCase

        )
    }

    @Singleton
    @Provides
    fun provideLifeCycleViewModelFactory(
        application: Application,
        useCase: UseCase
    ): LifeCycleViewModelFactory {
        return LifeCycleViewModelFactory(
                application,
                useCase

        )
    }





    @Singleton
    @Provides
    fun provideVVideoPlayViewModelFactory(
        application: Application,
        useCase: UseCase
    ): VideoPlayViewModelFactory {
        return VideoPlayViewModelFactory(
                application,
                useCase

        )
    }*/
}








