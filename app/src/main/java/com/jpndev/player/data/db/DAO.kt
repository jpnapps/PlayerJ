package com.jpndev.player.data.db

import androidx.room.*
import com.jpndev.player.data.model.Article
import com.jpndev.player.data.model.PItem
import com.jpndev.player.data.model.PJUrl
import kotlinx.coroutines.flow.Flow

@Dao
interface DAO {

    @Insert(onConflict=OnConflictStrategy.REPLACE)
    suspend fun insertPItem(pitem: PItem) : Long

    @Update
    suspend fun updatePItem(pitem: PItem) : Int

    @Query("DELETE FROM pitem_table")
    suspend fun deleteAllPItem(): Integer

    @Delete
    suspend fun deletePItem(item: PItem): Integer


    @Query("SELECT * FROM pitem_table")
     fun getPItems(): Flow<List<PItem>>



    @Insert(onConflict=OnConflictStrategy.REPLACE)
    suspend fun insertPJUrl(pitem: PJUrl) : Long

    @Update
    suspend fun updatePJUrl(pitem: PJUrl) : Int

    @Query("DELETE FROM url_table")
    suspend fun deleteAllPJUrl(): Integer

    @Delete
    suspend fun deletePJUrl(item: PJUrl): Integer


    @Query("SELECT * FROM url_table")
    fun getPJUrls(): Flow<List<PJUrl>>

    @Query("SELECT * FROM url_table")
    suspend fun getPJUrlList(): List<PJUrl>
}