package com.jpndev.player.domain.repository

import com.jpndev.player.data.model.APIResponse
import com.jpndev.player.data.model.MUpdateData
import com.jpndev.player.data.model.PItem
import com.jpndev.player.data.model.PListResponse
import com.jpndev.player.data.util.Resource
import kotlinx.coroutines.flow.Flow

interface AppRepository {

    suspend fun getTopQA( page : Int): Resource<APIResponse>
    //suspend fun getPList( page : Int): PListResponse

    suspend fun savePItem(article: PItem): Long
    suspend fun updatePItem(article: PItem): Int
    suspend fun deletePItem(article: PItem)
    fun getPItems(): Flow<List<PItem>>

    suspend fun getUpdateData( ): Resource<MUpdateData>
}