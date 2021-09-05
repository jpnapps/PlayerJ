package com.jpndev.newsapiclient.presentation.di

import com.jpndev.player.data.api.APIService
import com.jpndev.player.data.repository.dataSource.RemoteDataSource
import com.jpndev.player.data.repository.dataSourceImpl.LogSourceImpl
import com.jpndev.player.data.repository.dataSourceImpl.RemoteDataSourceImpl

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn

import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class RemoteDataModule {

    @Singleton
    @Provides
    fun provideNewsRemoteDataSource(
        apiService: APIService,
        logSourceImpl: LogSourceImpl
    ): RemoteDataSource {
       return RemoteDataSourceImpl(apiService,logSourceImpl)
    }

}












