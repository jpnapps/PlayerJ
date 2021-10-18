package com.jpndev.player.data.repository.dataSourceImpl

import com.jpndev.player.data.db.ArticleDAO
import com.jpndev.player.data.db.DAO
import com.jpndev.player.data.model.Article
import com.jpndev.player.data.model.PItem
import com.jpndev.player.data.model.PJUrl
import com.jpndev.player.data.repository.dataSource.LocalDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class LocalDataSourceImpl(
    private val articleDAO: ArticleDAO,
    private val pitemDAO: DAO,
    private val logSource:LogSourceImpl
):LocalDataSource {


    override suspend fun saveArticletoDb(item: Article) {
        articleDAO.insert(item)
    }

    override  fun getArticlesFromDB(): Flow<List<Article>> {
        return articleDAO.getArticles()
    }

    override suspend fun deleteArticle(item: Article) {
        articleDAO.delete(item)
    }

    override suspend fun clearAll() {
        CoroutineScope(Dispatchers.IO).launch {
            articleDAO.deleteAllArticle()
        }
    }


    override suspend fun savePItemtoDb(item: PItem) : Long{
      return  pitemDAO.insertPItem(item)
    }

    override  fun getPItemsFromDB(): Flow<List<PItem>> {
        return pitemDAO.getPItems()
    }

    override suspend fun updatePItem(pitem: PItem): Int {
        return pitemDAO.updatePItem(pitem)
    }

    override suspend fun deletePItem(item: PItem) {
        pitemDAO.deletePItem(item)
    }

    override suspend fun clearAllPItems() {
        CoroutineScope(Dispatchers.IO).launch {
            pitemDAO.deleteAllPItem()
        }
    }

    override suspend fun savePJUrltoDb(item: PJUrl): Long {
        return  pitemDAO.insertPJUrl(item)
    }

    override fun getPJUrlsFromDB(): Flow<List<PJUrl>> {
        return pitemDAO.getPJUrls()
    }

    override suspend fun getPJUrlListFromDB(): List<PJUrl> {
        return pitemDAO.getPJUrlList()
    }

    override suspend fun updatePJUrl(pitem: PJUrl): Int {
        return pitemDAO.updatePJUrl(pitem)
    }

    override suspend fun deletePJUrl(item: PJUrl) {
        pitemDAO.deletePJUrl(item)
    }

    override suspend fun clearAllPJUrls() {
        CoroutineScope(Dispatchers.IO).launch {
            pitemDAO.deleteAllPJUrl()
        }
    }

}