package com.jpndev.player.presentation.di



import android.content.Context
import com.jpndev.player.VFolderAdapter
import com.jpndev.player.presentation.ui.topqa.QAAdapter
import com.jpndev.player.presentation.ui.video.VideoAdapter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext

import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AdpaterModule {

  @Singleton
   @Provides
   fun provideGetNewsAdapter(

   ): QAAdapter {
      return QAAdapter()
   }

    @Singleton
    @Provides
    fun provideVideoAdapter(
        @ApplicationContext appContext: Context
    ): VideoAdapter {
        return VideoAdapter( appContext)
    }

    @Singleton
    @Provides
    fun provideVFolderAdapter(
        @ApplicationContext appContext: Context
    ): VFolderAdapter {
        return VFolderAdapter(appContext)
    }


/*
   @Singleton
   @Provides
   fun providePItemAdapter(

   ): PItemAdapter {
      return PItemAdapter()
   }*/
}


















