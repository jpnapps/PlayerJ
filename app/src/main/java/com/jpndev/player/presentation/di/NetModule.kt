package com.jpndev.player.presentation.di


import com.jpndev.player.BuildConfig
import com.jpndev.player.data.api.APIService

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn

import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import com.google.gson.GsonBuilder
import com.google.gson.Gson





//update  ApplicationComponent replace singletoncompoennt from 2,3
@Module
@InstallIn(SingletonComponent::class)
class NetModule {
    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient:OkHttpClient,gson:Gson): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl(BuildConfig.BASE_URL)
            .client(okHttpClient)

            .build()
    }



    @Singleton
    @Provides
    fun provideGSON(): Gson {
        return GsonBuilder()
            .setLenient()
            .create()
    }


    @Singleton
    @Provides
    fun provideAPIService(retrofit: Retrofit): APIService {
        return retrofit.create(APIService::class.java)
    }

    @Singleton
    @Provides
    fun provideOkHTTPClient(): OkHttpClient {
        return  OkHttpClient. Builder()
            .readTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS)
            .build()
    }

}













