package com.jpndev.player.data.db

import androidx.room.*
import com.jpndev.player.data.model.Article
import kotlinx.coroutines.flow.Flow

@Dao
interface ArticleDAO {

    @Insert(onConflict=OnConflictStrategy.REPLACE)
    suspend fun insert(QA: Article): Long

    @Query("DELETE FROM article_table")
    suspend fun deleteAllArticle(): Integer

    @Delete
    suspend fun delete(QA: Article): Integer


    @Query("SELECT * FROM article_table")
     fun getArticles(): Flow<List<Article>>
}