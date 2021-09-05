package com.jpndev.player.data.db

import androidx.room.*
import com.jpndev.player.data.model.Article
import com.jpndev.player.data.model.PItem
import kotlinx.coroutines.flow.Flow

@Dao
interface DAO {

    @Insert(onConflict=OnConflictStrategy.REPLACE)
    suspend fun insertPItem(pitem: PItem) : Long

    @Update
    suspend fun updatePItem(pitem: PItem) : Int

    @Query("DELETE FROM pitem_table")
    suspend fun deleteAllPItem()

    @Delete
    suspend fun deletePItem(item: PItem)


    @Query("SELECT * FROM pitem_table")
     fun getPItems(): Flow<List<PItem>>
}