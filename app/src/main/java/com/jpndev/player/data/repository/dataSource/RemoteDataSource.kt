package com.jpndev.player.data.repository.dataSource

import com.jpndev.player.data.model.APIResponse
import com.jpndev.player.data.model.MUpdateData
import retrofit2.Response

interface RemoteDataSource {
    suspend fun getTopQA(page : Int):Response<APIResponse>
    suspend fun getUpdateData():Response<MUpdateData>
    //suspend fun getSearchedNews(country : String,search_query : String, page : Int):Response<APIResponse>
}