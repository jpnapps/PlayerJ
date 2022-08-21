package com.jpndev.player.domain.usecase

import android.content.Context
import android.content.SharedPreferences
import com.jpndev.player.data.model.*
import com.jpndev.player.data.repository.dataSourceImpl.LogSourceImpl
import com.jpndev.player.data.util.Resource
import com.jpndev.player.domain.repository.AppRepository
import com.jpndev.player.utils.PrefUtils
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/* @Inject constructor */
class UseCase(
    private val repository: AppRepository,
    private val appContext: Context,
    public val prefUtils: PrefUtils,
    public val logsource: LogSourceImpl
) {

    suspend fun executeTopQA(page: Int): Resource<APIResponse> {
        return repository.getTopQA(page)
    }

    suspend fun executeDeletePItrm(item: PItem) = repository.deletePItem(item)
    suspend fun executeSavePItem(item: PItem): Long = repository.savePItem(item)
    suspend fun executeUpdatePItem(item: PItem): Int = repository.updatePItem(item)

    fun executeGetPList(): Flow<List<PItem>> {
        return repository.getPItems()
    }

    suspend fun executeAppUpdate(): Resource<MUpdateData> {
        return repository.getUpdateData()
    }
/*    suspend fun executeAppUpdate()=repository.getUpdateData()*/


    /*suspend fun execute():Flow<List<Article>>{
        return newsRepository.getSavedNews()
    }*/

    suspend fun executedeletePJUrl(item: PJUrl) = repository.deletePJUrl(item)
    suspend fun executeSavePJUrl(item: PJUrl): Long = repository.savePJUrl(item)
    suspend fun executeDeletePJUrl(item: PJUrl): Int = repository.updatePJUrl(item)

    fun executeGetPJUrl(): Flow<List<PJUrl>> {
        return repository.getPJUrls()
    }

    suspend fun executeGetPJUrlList(): List<PJUrl> {
        return repository.getPJUrlList()
    }

    /**
     * Method save AppData to DB
     * */
    suspend fun saveAPPDatatoDb(item: MUpdateData) = repository.saveAPPDatatoDb(item)

    /**
     * Method get AppData from DB
     * returns MUpdateData
     * */
    suspend fun getAPPDataFromDB() = repository.getAPPDataFromDB()

    fun getAPPDataFromDB2() = repository.getAPPDataFromDB2()
    /**
     * Method delete AppData from DB
     * */
    suspend fun deleteAppDataFromDB() = repository.deleteAppDataFromDB()

}