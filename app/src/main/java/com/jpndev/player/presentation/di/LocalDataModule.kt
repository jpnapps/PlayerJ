package com.jpndev.newsapiclient.presentation.di

import com.jpndev.player.data.db.ArticleDAO
import com.jpndev.player.data.db.DAO
import com.jpndev.player.data.repository.dataSource.LocalDataSource
import com.jpndev.player.data.repository.dataSourceImpl.LocalDataSourceImpl
import com.jpndev.player.data.repository.dataSourceImpl.LogSourceImpl

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn

import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class LocalDataModule {

    @Singleton
    @Provides
    fun provideLocalDataSource(
        articleDAO: ArticleDAO,
        pitemDAO: DAO,
        logSource: LogSourceImpl
    ): LocalDataSource {
       return LocalDataSourceImpl(articleDAO,pitemDAO,logSource)
    }

}












