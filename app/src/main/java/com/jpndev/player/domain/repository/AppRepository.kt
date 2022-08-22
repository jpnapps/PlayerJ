package com.jpndev.player.domain.repository

import com.jpndev.player.data.model.*
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
    suspend fun saveAPPDatatoDb(item : MUpdateData):Long
    suspend fun getAPPDataFromDB(): MUpdateData
     fun getAPPDataFromDB2(): MUpdateData
    suspend fun deleteAppDataFromDB()

    suspend fun savePJUrl(item: PJUrl): Long
    suspend fun updatePJUrl(item: PJUrl): Int
    suspend fun deletePJUrl(item: PJUrl)
    fun getPJUrls(): Flow<List<PJUrl>>
    suspend fun getPJUrlList(): List<PJUrl>
}