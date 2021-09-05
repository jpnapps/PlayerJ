package com.jpndev.player.presentation.di

import android.content.Context
import com.jpndev.player.data.repository.dataSourceImpl.LogSourceImpl
import com.jpndev.player.domain.repository.AppRepository
import com.jpndev.player.domain.usecase.UseCase
import com.jpndev.player.utils.PrefUtils

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext

import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class UseCaseModule {
   @Singleton
   @Provides
   fun provideUseCase(
      repository: AppRepository,
      @ApplicationContext appContext: Context,
      prefUtils: PrefUtils,
      logsource: LogSourceImpl
   ): UseCase {
      return UseCase(repository,appContext,prefUtils,logsource)
   }

   @Singleton
   @Provides
   fun providePrefUtils(

      @ApplicationContext appContext: Context
   ): PrefUtils {
      return PrefUtils.with(appContext)
   }


   @Singleton
   @Provides
   fun provideLogSourceImpl(): LogSourceImpl {
      return LogSourceImpl()
   }

}


















