package com.jpndev.player.data.repository.dataSource

import com.jpndev.player.data.model.Article
import com.jpndev.player.data.model.PItem
import com.jpndev.player.data.model.PJUrl
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {
     suspend fun saveArticletoDb(item : Article)
     fun getArticlesFromDB(): Flow<List<Article>>
     suspend fun deleteArticle(item : Article)
     suspend fun clearAll()


     suspend fun savePItemtoDb(item : PItem): Long
     fun getPItemsFromDB(): Flow<List<PItem>>
     suspend fun updatePItem(pitem: PItem) : Int
     suspend fun deletePItem(item : PItem)
     suspend fun clearAllPItems()


     suspend fun savePJUrltoDb(item : PJUrl): Long
     fun getPJUrlsFromDB(): Flow<List<PJUrl>>
     suspend fun getPJUrlListFromDB(): List<PJUrl>
     suspend fun updatePJUrl(pitem: PJUrl) : Int
     suspend fun deletePJUrl(item : PJUrl)
     suspend fun clearAllPJUrls()
}