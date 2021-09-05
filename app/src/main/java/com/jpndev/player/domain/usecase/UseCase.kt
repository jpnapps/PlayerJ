package com.jpndev.player.domain.usecase

import android.content.Context
import android.content.SharedPreferences
import com.jpndev.player.data.model.APIResponse
import com.jpndev.player.data.model.PItem
import com.jpndev.player.data.model.PListResponse
import com.jpndev.player.data.repository.dataSourceImpl.LogSourceImpl
import com.jpndev.player.data.util.Resource
import com.jpndev.player.domain.repository.AppRepository
import com.jpndev.player.utils.PrefUtils
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/* @Inject constructor */
class UseCase (private val repository: AppRepository, private val appContext: Context,public val prefUtils:PrefUtils,public val logsource:LogSourceImpl) {

    suspend fun executeTopQA(page : Int): Resource<APIResponse> {
        return repository.getTopQA(page)
    }

    suspend fun executeDeletePItrm(item: PItem)=repository.deletePItem(item)
    suspend fun executeSavePItem(item: PItem): Long=repository.savePItem(item)
    suspend fun executeUpdatePItem(item: PItem): Int=repository.updatePItem(item)

    fun executeGetPList(): Flow<List<PItem>> {
        return repository.getPItems()
    }

    suspend fun executeAppUpdate()=repository.getUpdateData()
    /*suspend fun execute():Flow<List<Article>>{
        return newsRepository.getSavedNews()
    }*/
}