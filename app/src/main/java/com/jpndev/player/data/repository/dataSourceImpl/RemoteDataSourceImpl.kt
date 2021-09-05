package com.jpndev.player.data.repository.dataSourceImpl

import com.jpndev.player.BuildConfig
import com.jpndev.player.data.api.APIService
import com.jpndev.player.data.model.APIResponse
import com.jpndev.player.data.model.MUpdateData
import com.jpndev.player.data.repository.dataSource.RemoteDataSource
import retrofit2.Response

class RemoteDataSourceImpl(
        private val apiService: APIService,
        private val logSource:LogSourceImpl
):RemoteDataSource {
    override suspend fun getTopQA(page: Int): Response<APIResponse> {

        return  apiService.getTopQA(page)
    }

    override suspend fun getUpdateData(): Response<MUpdateData> {

        var url = BuildConfig.BASE_URL + BuildConfig.MID_URL +  "23"
        logSource.addLog("\n Url  = " + url+"\n Request obj  = "+null)
        return  apiService.getMUpdateData(url)
    }

}