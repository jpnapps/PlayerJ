package com.jpndev.player.data.api

import com.jpndev.player.data.model.APIResponse
import com.jpndev.player.data.model.MUpdateData
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface APIService {
  @GET("view")
  suspend fun getTopQA(

      @Query("id")
      page:Int
  ): Response<APIResponse>


    @GET("v2/top-headlines")
    suspend fun getTopArticles(

        @Query("page")
        page:Int
    ): Response<APIResponse>


   /* @GET("v2/top-headlines")
    suspend fun getUpdateData(

        @Query("page")
        page:Int
    ): Response<MUpdateData>*/


    @GET
    suspend fun getMUpdateData(@Url url: String): Response<MUpdateData>
}