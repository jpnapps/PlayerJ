package com.jpndev.player.data.repository

import com.jpndev.player.data.model.*
import com.jpndev.player.data.repository.dataSource.LocalDataSource
import com.jpndev.player.data.repository.dataSource.RemoteDataSource
import com.jpndev.player.data.util.Resource
import com.jpndev.player.domain.repository.AppRepository
import kotlinx.coroutines.flow.Flow

import retrofit2.Response

class AppRepositoryImpl(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource
):AppRepository {


    override suspend fun getTopQA(page : Int): Resource<APIResponse> {
        return responseToResource(remoteDataSource.getTopQA(page))
    }

 /*   override suspend fun getPList(page: Int): PListResponse {
        return PListResponse()
    }*/

    override suspend fun savePItem(item: PItem) : Long{
      return  localDataSource.savePItemtoDb(item)
    }

    override suspend fun updatePItem(item: PItem): Int {
        return  localDataSource.updatePItem(item)
    }

    override suspend fun deletePItem(item: PItem) {
        localDataSource.deletePItem(item)
    }

    override fun getPItems(): Flow<List<PItem>> {
        return localDataSource.getPItemsFromDB()
    }

    override suspend fun getUpdateData(): Resource<MUpdateData> {
        return responseToUpdateResource(remoteDataSource.getUpdateData())
    }

    override suspend fun savePJUrl(item: PJUrl): Long {
        return  localDataSource.savePJUrltoDb(item)
    }

    override suspend fun updatePJUrl(item: PJUrl): Int {
        return  localDataSource.updatePJUrl(item)
    }

    override suspend fun deletePJUrl(item: PJUrl) {
        localDataSource.deletePJUrl(item)
    }

    override fun getPJUrls(): Flow<List<PJUrl>> {
        return localDataSource.getPJUrlsFromDB()
    }

    override suspend fun getPJUrlList(): List<PJUrl> {
        return localDataSource.getPJUrlListFromDB()
    }


    private fun responseToResource(response:Response<APIResponse>):Resource<APIResponse>{
        if(response.isSuccessful){
            response.body()?.let {result->
                return Resource.Success(result)
            }
        }
        return Resource.Error(response.message())
    }

    private fun responseToUpdateResource(response:Response<MUpdateData>):Resource<MUpdateData>{
        if(response.isSuccessful){
            response.body()?.let {result->
                return Resource.Success(result)
            }
        }
        return Resource.Error(response.message())
    }



   /* override suspend fun saveNews(QA: QA) {
        localDataSource.saveArticletoDb(QA)
    }

    override suspend fun deleteNews(QA: QA) {
        localDataSource.deleteArticle(QA)
    }

    override  fun getSavedNews(): Flow<List<QA>> {
      return  localDataSource.getArticlesFromDB()
    }*/
}